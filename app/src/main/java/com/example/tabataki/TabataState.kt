package com.example.tabataki

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// --- Tabata State & Logic ---

enum class TimerState { IDLE, PREPARE, WORK, REST, DONE }

class TabataState(context: Context) {
    private val settingsPrefs = context.getSharedPreferences("tabataki_settings", Context.MODE_PRIVATE)
    var appLang by mutableStateOf(
        try {
            Language.valueOf(settingsPrefs.getString("app_lang", "EN") ?: "EN")
        } catch (_: Exception) {
            Language.EN 
        }
    )
    
    fun setLanguage(l: Language) {
        appLang = l
        settingsPrefs.edit().putString("app_lang", l.name).apply()
    }

    var isManualMode by mutableStateOf(true)
    var manualWorkTime by mutableIntStateOf(20)
    var manualRestTime by mutableIntStateOf(10)
    var manualTotalRounds by mutableIntStateOf(8)

    var activeDayName by mutableStateOf(" ")
    var activePlaylist by mutableStateOf<kotlin.collections.List<Routine>>(emptyList())
    var currentRoutineIndex by mutableIntStateOf(0)

    var currentState by mutableStateOf(TimerState.IDLE)
        private set
    var currentRound by mutableIntStateOf(1)
        private set
    var timeLeft by mutableIntStateOf(0)
        private set
        
    var isPaused by mutableStateOf(false)
        private set

    val currentWorkTime: Int get() = if (isManualMode) manualWorkTime else activePlaylist.getOrNull(currentRoutineIndex)?.work ?: 20
    val currentRestTime: Int get() = if (isManualMode) manualRestTime else activePlaylist.getOrNull(currentRoutineIndex)?.rest ?: 10
    val currentTotalRounds: Int get() = if (isManualMode) manualTotalRounds else activePlaylist.getOrNull(currentRoutineIndex)?.rounds ?: 8
    val currentRoutineName: String get() = if (isManualMode) AppStrings.get(appLang, "custom") else activePlaylist.getOrNull(currentRoutineIndex)?.name ?: " "

    private val toneGenerator: ToneGenerator? = try {
        ToneGenerator(AudioManager.STREAM_MUSIC, 100)
    } catch (_: RuntimeException) {
        null
    }

    fun release() {
        toneGenerator?.release()
    }

    fun applyPlaylist(day: WorkoutDay) {
        if (currentState != TimerState.IDLE && currentState != TimerState.DONE) stopTimer()
        activePlaylist = day.routines
        activeDayName = day.name
        isManualMode = false
        currentRoutineIndex = 0
    }

    fun setManualMode() {
        if (currentState != TimerState.IDLE && currentState != TimerState.DONE) stopTimer()
        isManualMode = true
        activePlaylist = emptyList()
        activeDayName = " "
        currentRoutineIndex = 0
    }

    fun startTimer() {
        if (!isManualMode && activePlaylist.isEmpty()) return
        currentState = TimerState.PREPARE
        timeLeft = 5
        currentRound = 1
        isPaused = false
    }

    fun togglePause() {
        isPaused = !isPaused
    }

    fun stopTimer() {
        currentState = TimerState.IDLE
        timeLeft = 0
        currentRound = 1
        isPaused = false
        if (!isManualMode) currentRoutineIndex = 0
    }

    fun tick() {
        if (isPaused) return
        if (timeLeft > 0) {
            if (timeLeft in 1..3) {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
            }
            timeLeft -= 1
        } else {
            when (currentState) {
                TimerState.PREPARE -> {
                    playOpeningBell()
                    currentState = TimerState.WORK
                    timeLeft = currentWorkTime
                }
                TimerState.WORK -> {
                    playFinishBell()
                    if (currentRound < currentTotalRounds) {
                        currentState = TimerState.REST
                        timeLeft = currentRestTime
                    } else {
                        if (!isManualMode && currentRoutineIndex < activePlaylist.size - 1) {
                            currentRoutineIndex++
                            currentRound = 1
                            currentState = TimerState.PREPARE
                            timeLeft = 5
                        } else {
                            currentState = TimerState.DONE
                        }
                    }
                }
                TimerState.REST -> {
                    playOpeningBell()
                    currentRound += 1
                    currentState = TimerState.WORK
                    timeLeft = currentWorkTime
                }
                TimerState.DONE -> {
                    currentState = TimerState.IDLE
                }
                else -> {}
            }
        }
    }

    private fun playOpeningBell() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 500)
    }

    private fun playFinishBell() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 800)
    }
}

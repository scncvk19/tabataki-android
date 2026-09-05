package com.example.tabataki

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun TabataMainScreen(tabataState: TabataState) {
    val state = tabataState.currentState

    LaunchedEffect(state) {
        if (state != TimerState.IDLE && state != TimerState.DONE) {
            while (tabataState.currentState != TimerState.IDLE && tabataState.currentState != TimerState.DONE) {
                delay(1000L)
                tabataState.tick()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state) {
            TimerState.IDLE, TimerState.DONE -> SettingsScreen(tabataState)
            else -> ActiveTimerScreen(tabataState)
        }
    }
}

@Composable
fun SettingsScreen(tabataState: TabataState) {
    val lang = tabataState.appLang
    fun str(key: String) = AppStrings.get(lang, key)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(str("lang"), color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(8.dp))
            var langExpanded by remember { mutableStateOf(false) }
            Box {
                Button(
                    onClick = { langExpanded = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) { Text(lang.name + " ▼") }
                DropdownMenu(
                    expanded = langExpanded,
                    onDismissRequest = { langExpanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    Language.entries.forEach { language ->
                        DropdownMenuItem(
                            text = { Text(language.name, color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                tabataState.setLanguage(language)
                                langExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(str("timer"), fontSize = 36.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(32.dp))

        if (tabataState.isManualMode) {
            TimeNumberSelector(str("work"), tabataState.manualWorkTime, lang) { tabataState.manualWorkTime = it }
            Spacer(modifier = Modifier.height(16.dp))
            TimeNumberSelector(str("rest"), tabataState.manualRestTime, lang) { tabataState.manualRestTime = it }
            Spacer(modifier = Modifier.height(16.dp))
            NumberSelector(str("rounds"), tabataState.manualTotalRounds) { tabataState.manualTotalRounds = it }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(tabataState.activeDayName.ifBlank { " " }, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("${str("total_ex")} ${tabataState.activePlaylist.size}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    LazyColumn {
                        itemsIndexed(tabataState.activePlaylist) { index, routine ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${index + 1}. ${routine.name}", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                                Text("${routine.work}s / ${routine.rest}s", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        if (!tabataState.isManualMode) {
            OutlinedButton(
                onClick = { tabataState.setManualMode() },
                modifier = Modifier.fillMaxWidth(0.8f).height(48.dp)
            ) { Text(str("switch_manual")) }
            Spacer(modifier = Modifier.height(12.dp))
        }

        val context = LocalContext.current
        OutlinedButton(
            onClick = {
                runCatching {
                    context.startActivity(
                        Intent.createChooser(
                            Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/scncvk19/tabataki-android/issues")),
                            "Feedback"
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(0.8f).height(48.dp)
        ) { Text(str("feedback")) }

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { tabataState.startTimer() },
            modifier = Modifier.fillMaxWidth(0.8f).height(72.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            enabled = tabataState.isManualMode || tabataState.activePlaylist.isNotEmpty()
        ) {
            Text(str("start"), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Composable
fun ActiveTimerScreen(tabataState: TabataState) {
    val lang = tabataState.appLang
    fun str(key: String) = AppStrings.get(lang, key)
    val state = tabataState.currentState

    val (bgColor, textColor, label) = when (state) {
        TimerState.PREPARE -> Triple(Color(0xFFC3B091), Color(0xFF1E2019), str("prepare"))
        TimerState.WORK -> Triple(Color(0xFF4A5D23), Color.White, str("work_phase"))
        TimerState.REST -> Triple(Color(0xFF704214), Color.White, str("rest_phase"))
        else -> Triple(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.onBackground, " ")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1.5f).background(bgColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(tabataState.currentRoutineName.ifEmpty { " " }, fontSize = 32.sp, color = textColor, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(16.dp))
            Text("${str("round")} ${tabataState.currentRound} / ${tabataState.currentTotalRounds}", fontSize = 24.sp, color = textColor, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(if (tabataState.isPaused) str("pause") else label, fontSize = 48.sp, color = textColor, fontWeight = FontWeight.Black)
            Text(
                text = String.format(Locale.US, "%02d:%02d", tabataState.timeLeft / 60, tabataState.timeLeft % 60),
                fontSize = 96.sp,
                color = textColor,
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { tabataState.togglePause() }) {
                    Text(if (tabataState.isPaused) str("resume") else str("pause"), fontSize = 20.sp)
                }
                Button(onClick = { tabataState.stopTimer() }) {
                    Text(str("stop"), fontSize = 20.sp)
                }
            }
        }

        if (!tabataState.isManualMode) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f).background(MaterialTheme.colorScheme.surface),
                contentPadding = PaddingValues(16.dp)
            ) {
                itemsIndexed(tabataState.activePlaylist) { index, routine ->
                    val isCurrent = index == tabataState.currentRoutineIndex
                    val isDone = index < tabataState.currentRoutineIndex
                    val color = if (isCurrent) MaterialTheme.colorScheme.primary else if (isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${index + 1}. ${routine.name}", color = color, fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal)
                        Text("${routine.work}s / ${routine.rest}s × ${routine.rounds}", color = color)
                    }
                }
            }
        }
    }
}

@Composable
fun NumberSelector(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                FilledIconButton(onClick = { if (value > 1) onValueChange(value - 1) }) { Text("-") }
                var textValue by remember(value) { mutableStateOf(value.toString()) }
                BasicTextField(
                    value = textValue,
                    onValueChange = { raw ->
                        val filtered = raw.filter(Char::isDigit).take(3)
                        textValue = filtered
                        filtered.toIntOrNull()?.let { onValueChange(it.coerceAtLeast(1)) }
                    },
                    textStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(IntrinsicSize.Min).defaultMinSize(minWidth = 56.dp)
                )
                FilledIconButton(onClick = { onValueChange(value + 1) }) { Text("+") }
            }
        }
    }
}

@Composable
fun TimeNumberSelector(label: String, totalSeconds: Int, lang: Language, onValueChange: (Int) -> Unit) {
    var useMinutes by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val displayValue = if (useMinutes) totalSeconds / 60 else totalSeconds

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(8.dp))
                Box {
                    TextButton(onClick = { expanded = true }) {
                        Text(if (useMinutes) AppStrings.get(lang, "in_min") else AppStrings.get(lang, "in_sec"))
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text(AppStrings.get(lang, "sec")) }, onClick = { useMinutes = false; expanded = false })
                        DropdownMenuItem(text = { Text(AppStrings.get(lang, "min")) }, onClick = {
                            useMinutes = true
                            if (totalSeconds < 60) onValueChange(60)
                            expanded = false
                        })
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                FilledIconButton(onClick = {
                    val step = if (useMinutes) 60 else 1
                    onValueChange((totalSeconds - step).coerceAtLeast(step))
                }) { Text("-") }

                var textValue by remember(displayValue) { mutableStateOf(displayValue.toString()) }
                BasicTextField(
                    value = textValue,
                    onValueChange = { raw ->
                        val filtered = raw.filter(Char::isDigit).take(4)
                        textValue = filtered
                        filtered.toIntOrNull()?.let { parsed ->
                            onValueChange(if (useMinutes) parsed.coerceAtLeast(1) * 60 else parsed.coerceAtLeast(1))
                        }
                    },
                    textStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(IntrinsicSize.Min).defaultMinSize(minWidth = 56.dp)
                )

                FilledIconButton(onClick = { onValueChange(totalSeconds + if (useMinutes) 60 else 1) }) { Text("+") }
            }
        }
    }
}

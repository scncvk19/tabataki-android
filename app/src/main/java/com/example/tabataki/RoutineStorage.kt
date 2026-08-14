package com.example.tabataki

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

// --- Data Models & Storage ---

data class Routine(val id: String, var name: String, var work: Int, var rest: Int, var rounds: Int)
data class WorkoutDay(val id: String, var name: String, val routines: MutableList<Routine>)

class RoutineRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("tabataki_routines", Context.MODE_PRIVATE)
    private val KEY_DAYS = "workout_days_json"

    private val _daysFlow = kotlinx.coroutines.flow.MutableStateFlow<kotlin.collections.List<WorkoutDay>>(emptyList())
    val daysFlow: kotlinx.coroutines.flow.StateFlow<kotlin.collections.List<WorkoutDay>> = _daysFlow

    init {
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            _daysFlow.value = getDaysSync()
        }
    }

    private fun getDaysSync(): MutableList<WorkoutDay> {
        val days = mutableListOf<WorkoutDay>()
        val data = prefs.getString(KEY_DAYS, "[]") ?: "[]"
        try {
            val jsonArray = JSONArray(data)
            for (i in 0 until jsonArray.length()) {
                val dayObj = jsonArray.getJSONObject(i)
                val routines = mutableListOf<Routine>()
                if (dayObj.has("routines")) {
                    val rArr = dayObj.getJSONArray("routines")
                    for (j in 0 until rArr.length()) {
                        val rObj = rArr.getJSONObject(j)
                        routines.add(
                            Routine(
                                rObj.getString("id"),
                                rObj.getString("name"),
                                rObj.getInt("work"),
                                rObj.getInt("rest"),
                                rObj.getInt("rounds")
                            )
                        )
                    }
                }
                days.add(WorkoutDay(dayObj.getString("id"), dayObj.getString("name"), routines))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return days
    }

    fun getDays(): MutableList<WorkoutDay> {
        return _daysFlow.value.toMutableList()
    }

    fun saveDays(days: kotlin.collections.List<WorkoutDay>) {
        _daysFlow.value = days.toList()
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            val jsonArray = JSONArray()
        for (day in days) {
            val dayObj = JSONObject()
            dayObj.put("id", day.id)
            dayObj.put("name", day.name)
            val rArr = JSONArray()
            for (r in day.routines) {
                val rObj = JSONObject()
                rObj.put("id", r.id)
                rObj.put("name", r.name)
                rObj.put("work", r.work)
                rObj.put("rest", r.rest)
                rObj.put("rounds", r.rounds)
                rArr.put(rObj)
            }
            dayObj.put("routines", rArr)
            jsonArray.put(dayObj)
        }
        prefs.edit().putString(KEY_DAYS, jsonArray.toString()).apply()
        }
    }

    fun exportToJson(customExercises: kotlin.collections.List<Exercise>): String {
        val root = JSONObject()
        val daysJson = prefs.getString(KEY_DAYS, "[]") ?: "[]"
        root.put("days", JSONArray(daysJson))
        
        val exArray = JSONArray()
        for (ex in customExercises) {
            val exObj = JSONObject()
            exObj.put("name", ex.name)
            exObj.put("category", ex.category)
            exObj.put("description", ex.description)
            exObj.put("focus", ex.focus)
            exArray.put(exObj)
        }
        root.put("custom_exercises", exArray)
        return root.toString()
    }
    
    fun importFromJson(jsonString: String): kotlin.collections.List<Exercise> {
        val importedExercises = mutableListOf<Exercise>()
        try {
            val trimmed = jsonString.trim()
            if (trimmed.startsWith("{")) {
                val root = JSONObject(trimmed)
                val daysArr = root.optJSONArray("days") ?: JSONArray()
                prefs.edit().putString(KEY_DAYS, daysArr.toString()).apply()
                
                val exArr = root.optJSONArray("custom_exercises") ?: JSONArray()
                for (i in 0 until exArr.length()) {
                    val exObj = exArr.getJSONObject(i)
                    importedExercises.add(
                        Exercise(
                            name = exObj.getString("name"),
                            category = exObj.getString("category"),
                            description = exObj.getString("description"),
                            focus = exObj.getString("focus"),
                            isCustom = true
                        )
                    )
                }
            } else {
                // Fallback to old pure-days array
                val arr = JSONArray(trimmed)
                prefs.edit().putString(KEY_DAYS, arr.toString()).apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _daysFlow.value = getDaysSync()
        return importedExercises
    }
}

package com.example.tabataki

import android.content.Context
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    val allExercises: Flow<List<Exercise>> = exerciseDao.getAllExercises()

    fun getExercisesByCategory(category: String): Flow<List<Exercise>> =
        exerciseDao.getExercisesByCategory(category)

    suspend fun insert(exercise: Exercise) {
        exerciseDao.insertExercise(exercise)
    }

    suspend fun update(exercise: Exercise) {
        exerciseDao.updateExercise(exercise)
    }

    suspend fun delete(exercise: Exercise) {
        exerciseDao.deleteExercise(exercise)
    }

    suspend fun deleteByCategory(category: String) {
        exerciseDao.deleteExercisesByCategory(category)
    }

    suspend fun populateInitialDataIfNeeded(
        currentCount: Int,
        context: Context,
        lang: Language
    ) {
        if (currentCount > 0) return

        try {
            val jsonString = context.assets
                .open("exercises.json")
                .bufferedReader()
                .use { it.readText() }

            val jsonArray = JSONArray(jsonString)
            val initialExercises = buildList {
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    add(
                        Exercise(
                            name = obj.getString("name"),
                            category = obj.getString("category"),
                            description = obj.getString("description"),
                            focus = obj.getString("focus")
                        )
                    )
                }
            }

            exerciseDao.insertExercises(initialExercises)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

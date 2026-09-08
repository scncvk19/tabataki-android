package com.example.tabataki

import kotlinx.coroutines.flow.Flow

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    val allExercises: Flow<List<Exercise>> = exerciseDao.getAllExercises()

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
        context: android.content.Context,
        lang: Language
    ) {
        if (currentCount > 0) return

        try {
            val jsonString = context.assets.open("exercises.json").bufferedReader().use { it.readText() }
            val jsonArray = org.json.JSONArray(jsonString)
            val initialExercises = mutableListOf<Exercise>()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                initialExercises.add(
                    Exercise(
                        name = obj.getString("name"),
                        category = obj.getString("category"),
                        description = obj.getString("description"),
                        focus = obj.getString("focus")
                    )
                )
            }

            if (initialExercises.isNotEmpty()) {
                exerciseDao.insertExercises(initialExercises)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

package com.example.tabataki

import kotlinx.coroutines.flow.Flow

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    val allExercises: Flow<List<Exercise>> = exerciseDao.getAllExercises()

    fun getExercisesByCategory(category: String): Flow<List<Exercise>> {
        return exerciseDao.getExercisesByCategory(category)
    }

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

    suspend fun populateInitialDataIfNeeded(currentCount: Int, context: android.content.Context, lang: Language) {
        val prefs = context.getSharedPreferences("tabataki_exercise_prefs", android.content.Context.MODE_PRIVATE)
        val isPopulated = prefs.getBoolean("is_initial_data_populated", false)
        
        // Only run if database is empty AND we haven't populated it before
        if (currentCount > 0 || isPopulated) {
            if (currentCount > 0 && !isPopulated) {
                // If it has data but flag wasn't set, set the flag
                prefs.edit().putBoolean("is_initial_data_populated", true).apply()
            }
            return
        }
        
        try {
            val jsonString = context.assets.open("exercises.json").bufferedReader().use { it.readText() }
            val jsonArray = org.json.JSONArray(jsonString)
            val initialExercises = mutableListOf<Exercise>()
            
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val category = obj.getString("category")
                val name = obj.getString("name")
                val desc = obj.getString("description")
                val focus = obj.getString("focus")

                initialExercises.add(Exercise(name = name, category = category, description = desc, focus = focus))
            }
            
            exerciseDao.insertExercises(initialExercises)
            // Mark as populated so it never runs again even if database is emptied
            prefs.edit().putBoolean("is_initial_data_populated", true).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

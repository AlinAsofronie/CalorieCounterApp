package com.fittrackpro.app.data.local.database

import android.content.Context
import com.fittrackpro.app.data.local.entities.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseTest {
    fun testDatabase(context: Context) {
        val database = FitTrackDatabase.getDatabase(context)
        val userProfileDao = database.userProfileDao()
        
        // Test insert
        CoroutineScope(Dispatchers.IO).launch {
            val testProfile = UserProfile(
                id = 1,
                name = "Test User",
                age = 30,
                heightCm = 175,
                currentWeightKg = 80f,
                goalWeightKg = 75f,
                activityLevel = "moderate",
                goal = "lose_weight",
                dailyCalorieGoal = 2000,
                proteinGoalGrams = 150,
                carbsGoalGrams = 200,
                fatGoalGrams = 65
            )
            
            userProfileDao.insertUserProfile(testProfile)
            println("✅ Database test: User profile inserted successfully!")
        }
    }
}
package com.fittrackpro.app.data.repository

import com.fittrackpro.app.data.local.dao.UserProfileDao
import com.fittrackpro.app.data.local.entities.UserProfile
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userProfileDao: UserProfileDao) {
    
    fun getUserProfile(): Flow<UserProfile?> = userProfileDao.getUserProfile()
    
    suspend fun hasUserProfile(): Boolean = userProfileDao.hasUserProfile()
    
    suspend fun createUserProfile(profile: UserProfile) {
        userProfileDao.insertUserProfile(profile)
    }
    
    suspend fun updateUserProfile(profile: UserProfile) {
        userProfileDao.updateUserProfile(profile)
    }
    
    // Calculate BMR (Basal Metabolic Rate) using Mifflin-St Jeor Equation
    fun calculateBMR(
        weightKg: Float,
        heightCm: Int,
        age: Int,
        isMale: Boolean = true
    ): Int {
        val bmr = if (isMale) {
            (10 * weightKg) + (6.25 * heightCm) - (5 * age) + 5
        } else {
            (10 * weightKg) + (6.25 * heightCm) - (5 * age) - 161
        }
        return bmr.toInt()
    }
    
    // Calculate TDEE (Total Daily Energy Expenditure)
    fun calculateTDEE(bmr: Int, activityLevel: String): Int {
        val multiplier = when (activityLevel) {
            "sedentary" -> 1.2          // Little or no exercise
            "light" -> 1.375             // Exercise 1-3 days/week
            "moderate" -> 1.55           // Exercise 3-5 days/week
            "active" -> 1.725            // Exercise 6-7 days/week
            "very_active" -> 1.9         // Very intense exercise daily
            else -> 1.2
        }
        return (bmr * multiplier).toInt()
    }
    
    // Calculate daily calorie goal based on weight goal
    fun calculateCalorieGoal(tdee: Int, goal: String): Int {
        return when (goal) {
            "lose_weight" -> tdee - 500      // 500 cal deficit = ~0.5kg/week loss
            "lose_weight_fast" -> tdee - 750 // 750 cal deficit = ~0.75kg/week loss
            "maintain" -> tdee
            "gain_weight" -> tdee + 300      // 300 cal surplus = ~0.3kg/week gain
            "gain_muscle" -> tdee + 500      // 500 cal surplus = ~0.5kg/week gain
            else -> tdee
        }
    }
    
    // Calculate macro goals (protein, carbs, fat) in grams
    fun calculateMacros(calorieGoal: Int, goal: String): Triple<Int, Int, Int> {
        // Returns: (protein grams, carbs grams, fat grams)
        return when (goal) {
            "lose_weight", "lose_weight_fast" -> {
                // High protein for muscle preservation
                val proteinGrams = (calorieGoal * 0.30 / 4).toInt()  // 30% of cals, 4 cal/g
                val fatGrams = (calorieGoal * 0.30 / 9).toInt()      // 30% of cals, 9 cal/g
                val carbsGrams = (calorieGoal * 0.40 / 4).toInt()    // 40% of cals, 4 cal/g
                Triple(proteinGrams, carbsGrams, fatGrams)
            }
            "maintain" -> {
                // Balanced macros
                val proteinGrams = (calorieGoal * 0.25 / 4).toInt()  // 25%
                val fatGrams = (calorieGoal * 0.30 / 9).toInt()      // 30%
                val carbsGrams = (calorieGoal * 0.45 / 4).toInt()    // 45%
                Triple(proteinGrams, carbsGrams, fatGrams)
            }
            "gain_weight", "gain_muscle" -> {
                // Higher carbs and protein for muscle growth
                val proteinGrams = (calorieGoal * 0.25 / 4).toInt()  // 25%
                val fatGrams = (calorieGoal * 0.25 / 9).toInt()      // 25%
                val carbsGrams = (calorieGoal * 0.50 / 4).toInt()    // 50%
                Triple(proteinGrams, carbsGrams, fatGrams)
            }
            else -> {
                val proteinGrams = (calorieGoal * 0.25 / 4).toInt()
                val fatGrams = (calorieGoal * 0.30 / 9).toInt()
                val carbsGrams = (calorieGoal * 0.45 / 4).toInt()
                Triple(proteinGrams, carbsGrams, fatGrams)
            }
        }
    }
}
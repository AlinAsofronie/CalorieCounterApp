package com.fittrackpro.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // Single user for MVP
    val name: String,
    val age: Int,
    val heightCm: Int,
    val currentWeightKg: Float,
    val goalWeightKg: Float,
    val activityLevel: String, // sedentary, light, moderate, active, very_active
    val goal: String, // lose_weight, maintain, gain_weight
    val dailyCalorieGoal: Int,
    val proteinGoalGrams: Int,
    val carbsGoalGrams: Int,
    val fatGoalGrams: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
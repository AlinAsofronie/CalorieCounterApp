package com.fittrackpro.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_logs",
    foreignKeys = [
        ForeignKey(
            entity = FoodItem::class,
            parentColumns = ["id"],
            childColumns = ["foodItemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["foodItemId"]), Index(value = ["date"])]
)
data class FoodLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val foodItemId: Long,
    val date: String, // Format: YYYY-MM-DD
    val mealType: String, // breakfast, lunch, dinner, snack
    val servings: Float, // 1.0 = 1 serving, 0.5 = half serving
    val totalCalories: Float,
    val totalProtein: Float,
    val totalCarbs: Float,
    val totalFat: Float,
    val timestamp: Long = System.currentTimeMillis()
)
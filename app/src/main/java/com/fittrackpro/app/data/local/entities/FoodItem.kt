package com.fittrackpro.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val brand: String? = null,
    val barcode: String? = null,
    val servingSize: String, // e.g., "100g", "1 cup"
    val calories: Float,
    val proteinGrams: Float,
    val carbsGrams: Float,
    val fatGrams: Float,
    val fiberGrams: Float? = null,
    val sugarGrams: Float? = null,
    val sodiumMg: Float? = null,
    val isCustom: Boolean = false, // User created vs from API
    val source: String = "manual", // manual, open_food_facts, usda
    val createdAt: Long = System.currentTimeMillis()
)
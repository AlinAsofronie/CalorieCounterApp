package com.fittrackpro.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_entries")
data class WeightEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weightKg: Float,
    val date: String, // Format: YYYY-MM-DD
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
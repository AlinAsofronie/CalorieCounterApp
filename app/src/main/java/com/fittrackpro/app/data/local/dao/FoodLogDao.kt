package com.fittrackpro.app.data.local.dao

import androidx.room.*
import com.fittrackpro.app.data.local.entities.FoodLog
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodLogDao {
    @Query("SELECT * FROM food_logs WHERE date = :date ORDER BY timestamp ASC")
    fun getFoodLogsForDate(date: String): Flow<List<FoodLog>>
    
    @Query("SELECT * FROM food_logs WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC, timestamp DESC")
    fun getFoodLogsForDateRange(startDate: String, endDate: String): Flow<List<FoodLog>>
    
    @Insert
    suspend fun insertFoodLog(foodLog: FoodLog): Long
    
    @Update
    suspend fun updateFoodLog(foodLog: FoodLog)
    
    @Delete
    suspend fun deleteFoodLog(foodLog: FoodLog)
    
    // Get total calories for a specific date
    @Query("SELECT SUM(totalCalories) FROM food_logs WHERE date = :date")
    fun getTotalCaloriesForDate(date: String): Flow<Float?>
    
    // Get meal type totals for a date
    @Query("SELECT mealType, SUM(totalCalories) as calories, SUM(totalProtein) as protein, SUM(totalCarbs) as carbs, SUM(totalFat) as fat FROM food_logs WHERE date = :date GROUP BY mealType")
    fun getMealTypeTotalsForDate(date: String): Flow<List<MealTypeSummary>>
}
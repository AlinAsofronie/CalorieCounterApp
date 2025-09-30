package com.fittrackpro.app.data.repository

import com.fittrackpro.app.data.local.dao.FoodItemDao
import com.fittrackpro.app.data.local.dao.FoodLogDao
import com.fittrackpro.app.data.local.entities.FoodItem
import com.fittrackpro.app.data.local.entities.FoodLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FoodRepository(
    private val foodItemDao: FoodItemDao,
    private val foodLogDao: FoodLogDao
) {
    
    // FoodItem operations
    fun getAllFoodItems(): Flow<List<FoodItem>> = foodItemDao.getAllFoodItems()
    
    fun searchFoodItems(query: String): Flow<List<FoodItem>> = foodItemDao.searchFoodItems(query)
    
    suspend fun getFoodItemById(id: Long): FoodItem? = foodItemDao.getFoodItemById(id)
    
    suspend fun getFoodItemByBarcode(barcode: String): FoodItem? = 
        foodItemDao.getFoodItemByBarcode(barcode)
    
    suspend fun insertFoodItem(foodItem: FoodItem): Long = foodItemDao.insertFoodItem(foodItem)
    
    suspend fun updateFoodItem(foodItem: FoodItem) = foodItemDao.updateFoodItem(foodItem)
    
    suspend fun deleteFoodItem(foodItem: FoodItem) = foodItemDao.deleteFoodItem(foodItem)
    
    fun getRecentFoods(): Flow<List<FoodItem>> = foodItemDao.getRecentFoods()
    
    // FoodLog operations
    fun getFoodLogsForDate(date: String): Flow<List<FoodLog>> = 
        foodLogDao.getFoodLogsForDate(date)
    
    fun getFoodLogsForToday(): Flow<List<FoodLog>> {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        return foodLogDao.getFoodLogsForDate(today)
    }
    
    fun getFoodLogsForDateRange(startDate: String, endDate: String): Flow<List<FoodLog>> =
        foodLogDao.getFoodLogsForDateRange(startDate, endDate)
    
    suspend fun logFood(
        foodItem: FoodItem,
        mealType: String,
        servings: Float,
        date: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    ): Long {
        val foodLog = FoodLog(
            foodItemId = foodItem.id,
            date = date,
            mealType = mealType,
            servings = servings,
            totalCalories = foodItem.calories * servings,
            totalProtein = foodItem.proteinGrams * servings,
            totalCarbs = foodItem.carbsGrams * servings,
            totalFat = foodItem.fatGrams * servings
        )
        return foodLogDao.insertFoodLog(foodLog)
    }
    
    suspend fun updateFoodLog(foodLog: FoodLog) = foodLogDao.updateFoodLog(foodLog)
    
    suspend fun deleteFoodLog(foodLog: FoodLog) = foodLogDao.deleteFoodLog(foodLog)
    
    fun getTotalCaloriesForDate(date: String): Flow<Float?> = 
        foodLogDao.getTotalCaloriesForDate(date)
    
    fun getTotalCaloriesForToday(): Flow<Float?> {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        return foodLogDao.getTotalCaloriesForDate(today)
    }
    
    fun getTotalProteinForToday(): Flow<Float?> {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        return foodLogDao.getTotalProteinForDate(today)
    }
    
    fun getTotalCarbsForToday(): Flow<Float?> {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        return foodLogDao.getTotalCarbsForDate(today)
    }
    
    fun getTotalFatForToday(): Flow<Float?> {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        return foodLogDao.getTotalFatForDate(today)
    }
    
    // Helper to get today's date
    fun getTodayDate(): String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
}
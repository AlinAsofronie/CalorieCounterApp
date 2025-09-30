package com.fittrackpro.app.data.local.dao

import androidx.room.*
import com.fittrackpro.app.data.local.entities.FoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemDao {
    @Query("SELECT * FROM food_items ORDER BY createdAt DESC")
    fun getAllFoodItems(): Flow<List<FoodItem>>
    
    @Query("SELECT * FROM food_items WHERE id = :id")
    suspend fun getFoodItemById(id: Long): FoodItem?
    
    @Query("SELECT * FROM food_items WHERE name LIKE '%' || :query || '%' OR brand LIKE '%' || :query || '%' ORDER BY createdAt DESC LIMIT 20")
    fun searchFoodItems(query: String): Flow<List<FoodItem>>
    
    @Query("SELECT * FROM food_items WHERE barcode = :barcode LIMIT 1")
    suspend fun getFoodItemByBarcode(barcode: String): FoodItem?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItem(foodItem: FoodItem): Long
    
    @Update
    suspend fun updateFoodItem(foodItem: FoodItem)
    
    @Delete
    suspend fun deleteFoodItem(foodItem: FoodItem)
    
    // Get recently used foods (last 30 days)
    @Query("""
        SELECT DISTINCT f.* FROM food_items f
        INNER JOIN food_logs fl ON f.id = fl.foodItemId
        WHERE fl.timestamp > :thirtyDaysAgo
        ORDER BY fl.timestamp DESC
        LIMIT 10
    """)
    fun getRecentFoods(thirtyDaysAgo: Long = System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000): Flow<List<FoodItem>>
}
package com.fittrackpro.app.data.local.dao

import androidx.room.*
import com.fittrackpro.app.data.local.entities.WeightEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightEntryDao {
    @Query("SELECT * FROM weight_entries ORDER BY date DESC")
    fun getAllWeightEntries(): Flow<List<WeightEntry>>
    
    @Query("SELECT * FROM weight_entries ORDER BY date DESC LIMIT 1")
    fun getLatestWeightEntry(): Flow<WeightEntry?>
    
    @Query("SELECT * FROM weight_entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getWeightEntriesForDateRange(startDate: String, endDate: String): Flow<List<WeightEntry>>
    
    @Insert
    suspend fun insertWeightEntry(weightEntry: WeightEntry)
    
    @Update
    suspend fun updateWeightEntry(weightEntry: WeightEntry)
    
    @Delete
    suspend fun deleteWeightEntry(weightEntry: WeightEntry)
}
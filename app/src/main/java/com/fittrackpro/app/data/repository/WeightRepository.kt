package com.fittrackpro.app.data.repository

import com.fittrackpro.app.data.local.dao.WeightEntryDao
import com.fittrackpro.app.data.local.entities.WeightEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeightRepository(private val weightEntryDao: WeightEntryDao) {
    
    fun getAllWeightEntries(): Flow<List<WeightEntry>> = weightEntryDao.getAllWeightEntries()
    
    fun getLatestWeightEntry(): Flow<WeightEntry?> = weightEntryDao.getLatestWeightEntry()
    
    fun getWeightEntriesForDateRange(startDate: String, endDate: String): Flow<List<WeightEntry>> =
        weightEntryDao.getWeightEntriesForDateRange(startDate, endDate)
    
    suspend fun addWeightEntry(
        weightKg: Float,
        note: String? = null,
        date: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    ) {
        val entry = WeightEntry(
            weightKg = weightKg,
            date = date,
            note = note
        )
        weightEntryDao.insertWeightEntry(entry)
    }
    
    suspend fun updateWeightEntry(weightEntry: WeightEntry) = 
        weightEntryDao.updateWeightEntry(weightEntry)
    
    suspend fun deleteWeightEntry(weightEntry: WeightEntry) = 
        weightEntryDao.deleteWeightEntry(weightEntry)
}
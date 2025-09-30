package com.fittrackpro.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fittrackpro.app.data.local.dao.*
import com.fittrackpro.app.data.local.entities.*

@Database(
    entities = [
        UserProfile::class,
        FoodItem::class,
        FoodLog::class,
        WeightEntry::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FitTrackDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun foodItemDao(): FoodItemDao
    abstract fun foodLogDao(): FoodLogDao
    abstract fun weightEntryDao(): WeightEntryDao
    
    companion object {
        @Volatile
        private var INSTANCE: FitTrackDatabase? = null
        
        fun getDatabase(context: Context): FitTrackDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitTrackDatabase::class.java,
                    "fittrack_database"
                )
                    .fallbackToDestructiveMigration() // For MVP only, remove in production
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
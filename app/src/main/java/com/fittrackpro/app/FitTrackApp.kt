package com.fittrackpro.app

import android.app.Application
import com.fittrackpro.app.data.local.database.FitTrackDatabase
import com.fittrackpro.app.data.repository.FoodRepository
import com.fittrackpro.app.data.repository.UserRepository
import com.fittrackpro.app.data.repository.WeightRepository

class FitTrackApp : Application() {
    
    // Database instance
    val database by lazy { FitTrackDatabase.getDatabase(this) }
    
    // Repositories
    val userRepository by lazy { 
        UserRepository(database.userProfileDao()) 
    }
    
    val foodRepository by lazy { 
        FoodRepository(
            database.foodItemDao(),
            database.foodLogDao()
        ) 
    }
    
    val weightRepository by lazy { 
        WeightRepository(database.weightEntryDao()) 
    }
}
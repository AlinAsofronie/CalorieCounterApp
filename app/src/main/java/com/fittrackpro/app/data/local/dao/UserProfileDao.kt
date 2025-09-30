package com.fittrackpro.app.data.local.dao

import androidx.room.*
import com.fittrackpro.app.data.local.entities.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfile)
    
    @Update
    suspend fun updateUserProfile(profile: UserProfile)
    
    @Query("SELECT EXISTS(SELECT 1 FROM user_profile WHERE id = 1)")
    suspend fun hasUserProfile(): Boolean
}
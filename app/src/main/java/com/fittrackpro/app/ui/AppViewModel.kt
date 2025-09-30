package com.fittrackpro.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.fittrackpro.app.FitTrackApp

// Base ViewModel that provides easy access to repositories
open class AppViewModel(application: Application) : AndroidViewModel(application) {
    
    protected val userRepository get() = 
        (getApplication<Application>() as FitTrackApp).userRepository
    
    protected val foodRepository get() = 
        (getApplication<Application>() as FitTrackApp).foodRepository
    
    protected val weightRepository get() = 
        (getApplication<Application>() as FitTrackApp).weightRepository
}
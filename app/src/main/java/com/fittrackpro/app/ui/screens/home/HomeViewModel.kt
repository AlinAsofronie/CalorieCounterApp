package com.fittrackpro.app.ui.screens.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.fittrackpro.app.data.local.entities.UserProfile
import com.fittrackpro.app.data.local.entities.FoodItem
import com.fittrackpro.app.ui.AppViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class HomeState(
    val userProfile: UserProfile? = null,
    val currentDate: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
    val totalCaloriesToday: Float = 0f,
    val caloriesRemaining: Int = 0,
    val proteinConsumed: Float = 0f,
    val carbsConsumed: Float = 0f,
    val fatConsumed: Float = 0f,
    val recentFoods: List<FoodItem> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(application: Application) : AppViewModel(application) {
    
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()
    
    init {
        loadData()
        loadRecentFoods()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            // Load user profile
            val profileFlow = userRepository.getUserProfile()
            
            // Load today's food logs
            val today = foodRepository.getTodayDate()
            val foodLogsFlow = foodRepository.getFoodLogsForDate(today)
            
            // Combine both
            combine(profileFlow, foodLogsFlow) { profile, foodLogs ->
                if (profile != null) {
                    // Calculate totals from food logs
                    var totalCalories = 0f
                    var totalProtein = 0f
                    var totalCarbs = 0f
                    var totalFat = 0f
                    
                    foodLogs.forEach { log ->
                        totalCalories += log.totalCalories
                        totalProtein += log.totalProtein
                        totalCarbs += log.totalCarbs
                        totalFat += log.totalFat
                    }
                    
                    HomeState(
                        userProfile = profile,
                        totalCaloriesToday = totalCalories,
                        caloriesRemaining = profile.dailyCalorieGoal - totalCalories.toInt(),
                        proteinConsumed = totalProtein,
                        carbsConsumed = totalCarbs,
                        fatConsumed = totalFat,
                        isLoading = false
                    )
                } else {
                    _state.value.copy(isLoading = false)
                }
            }.collect { newState ->
                _state.value = newState
            }
        }
    }
    
    private fun loadRecentFoods() {
        viewModelScope.launch {
            foodRepository.getRecentFoods().collect { foods ->
                _state.value = _state.value.copy(recentFoods = foods)
            }
        }
    }
    
    fun refreshData() {
        loadData()
        loadRecentFoods()
    }
}
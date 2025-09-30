package com.fittrackpro.app.ui.screens.food

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.fittrackpro.app.data.local.entities.FoodLog
import com.fittrackpro.app.ui.AppViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TodaysFoodLogState(
    val foodLogs: List<FoodLog> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class TodaysFoodLogViewModel(application: Application) : AppViewModel(application) {
    
    private val _state = MutableStateFlow(TodaysFoodLogState())
    val state: StateFlow<TodaysFoodLogState> = _state.asStateFlow()
    
    init {
        loadTodaysFoodLogs()
    }
    
    private fun loadTodaysFoodLogs() {
        viewModelScope.launch {
            try {
                foodRepository.getFoodLogsForToday().collect { logs ->
                    _state.value = _state.value.copy(
                        foodLogs = logs,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load food logs"
                )
            }
        }
    }
    
    fun deleteFoodLog(foodLog: FoodLog) {
        viewModelScope.launch {
            try {
                foodRepository.deleteFoodLog(foodLog)
                // No need to refresh - the Flow will automatically update
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to delete food log"
                )
            }
        }
    }
    
    fun refreshData() {
        loadTodaysFoodLogs()
    }
}
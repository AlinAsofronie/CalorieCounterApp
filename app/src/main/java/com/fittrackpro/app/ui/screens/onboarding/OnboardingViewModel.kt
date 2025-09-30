package com.fittrackpro.app.ui.screens.onboarding

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.fittrackpro.app.data.local.entities.UserProfile
import com.fittrackpro.app.ui.AppViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OnboardingState(
    val currentStep: Int = 0,
    val name: String = "",
    val age: String = "",
    val gender: String = "male", // male, female
    val heightCm: String = "",
    val currentWeightKg: String = "",
    val goalWeightKg: String = "",
    val activityLevel: String = "moderate",
    val goal: String = "lose_weight",
    val isLoading: Boolean = false,
    val error: String? = null
)

class OnboardingViewModel(application: Application) : AppViewModel(application) {
    
    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()
    
    fun updateName(name: String) {
        _state.value = _state.value.copy(name = name)
    }
    
    fun updateAge(age: String) {
        _state.value = _state.value.copy(age = age)
    }
    
    fun updateGender(gender: String) {
        _state.value = _state.value.copy(gender = gender)
    }
    
    fun updateHeight(height: String) {
        _state.value = _state.value.copy(heightCm = height)
    }
    
    fun updateCurrentWeight(weight: String) {
        _state.value = _state.value.copy(currentWeightKg = weight)
    }
    
    fun updateGoalWeight(weight: String) {
        _state.value = _state.value.copy(goalWeightKg = weight)
    }
    
    fun updateActivityLevel(level: String) {
        _state.value = _state.value.copy(activityLevel = level)
    }
    
    fun updateGoal(goal: String) {
        _state.value = _state.value.copy(goal = goal)
    }
    
    fun nextStep() {
        _state.value = _state.value.copy(currentStep = _state.value.currentStep + 1)
    }
    
    fun previousStep() {
        if (_state.value.currentStep > 0) {
            _state.value = _state.value.copy(currentStep = _state.value.currentStep - 1)
        }
    }
    
    fun completeOnboarding(onComplete: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                val currentState = _state.value
                
                // Validate inputs
                val age = currentState.age.toIntOrNull() ?: throw Exception("Invalid age")
                val heightCm = currentState.heightCm.toIntOrNull() ?: throw Exception("Invalid height")
                val currentWeight = currentState.currentWeightKg.toFloatOrNull() 
                    ?: throw Exception("Invalid current weight")
                val goalWeight = currentState.goalWeightKg.toFloatOrNull() 
                    ?: throw Exception("Invalid goal weight")
                
                // Calculate calorie goals using repository
                val bmr = userRepository.calculateBMR(
                    weightKg = currentWeight,
                    heightCm = heightCm,
                    age = age,
                    isMale = currentState.gender == "male"
                )
                
                val tdee = userRepository.calculateTDEE(bmr, currentState.activityLevel)
                val calorieGoal = userRepository.calculateCalorieGoal(tdee, currentState.goal)
                val macros = userRepository.calculateMacros(calorieGoal, currentState.goal)
                
                // Create user profile
                val profile = UserProfile(
                    id = 1,
                    name = currentState.name,
                    age = age,
                    heightCm = heightCm,
                    currentWeightKg = currentWeight,
                    goalWeightKg = goalWeight,
                    activityLevel = currentState.activityLevel,
                    goal = currentState.goal,
                    dailyCalorieGoal = calorieGoal,
                    proteinGoalGrams = macros.first,
                    carbsGoalGrams = macros.second,
                    fatGoalGrams = macros.third
                )
                
                userRepository.createUserProfile(profile)
                
                // Add initial weight entry
                weightRepository.addWeightEntry(currentWeight)
                
                _state.value = _state.value.copy(isLoading = false)
                onComplete()
                
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to create profile"
                )
            }
        }
    }
}
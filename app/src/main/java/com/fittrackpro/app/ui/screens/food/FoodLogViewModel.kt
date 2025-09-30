package com.fittrackpro.app.ui.screens.food

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.fittrackpro.app.data.local.entities.FoodItem
import com.fittrackpro.app.ui.AppViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FoodLogState(
    val foodName: String = "",
    val brand: String = "",
    val servingSize: String = "",
    val calories: String = "",
    val protein: String = "",
    val carbs: String = "",
    val fat: String = "",
    val selectedMealType: String = "breakfast",
    val servings: String = "1",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class FoodLogViewModel(application: Application) : AppViewModel(application) {
    
    private val _state = MutableStateFlow(FoodLogState())
    val state: StateFlow<FoodLogState> = _state.asStateFlow()
    
    fun updateFoodName(value: String) {
        _state.value = _state.value.copy(foodName = value)
    }
    
    fun updateBrand(value: String) {
        _state.value = _state.value.copy(brand = value)
    }
    
    fun updateServingSize(value: String) {
        _state.value = _state.value.copy(servingSize = value)
    }
    
    fun updateCalories(value: String) {
        _state.value = _state.value.copy(calories = value)
    }
    
    fun updateProtein(value: String) {
        _state.value = _state.value.copy(protein = value)
    }
    
    fun updateCarbs(value: String) {
        _state.value = _state.value.copy(carbs = value)
    }
    
    fun updateFat(value: String) {
        _state.value = _state.value.copy(fat = value)
    }
    
    fun updateMealType(value: String) {
        _state.value = _state.value.copy(selectedMealType = value)
    }
    
    fun updateServings(value: String) {
        _state.value = _state.value.copy(servings = value)
    }
    
    fun saveFood(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                val currentState = _state.value
                
                // Validate
                val calories = currentState.calories.toFloatOrNull() 
                    ?: throw Exception("Invalid calories")
                val protein = currentState.protein.toFloatOrNull() ?: 0f
                val carbs = currentState.carbs.toFloatOrNull() ?: 0f
                val fat = currentState.fat.toFloatOrNull() ?: 0f
                val servings = currentState.servings.toFloatOrNull() ?: 1f
                
                // Create food item
                val foodItem = FoodItem(
                    name = currentState.foodName,
                    brand = currentState.brand.ifBlank { null },
                    servingSize = currentState.servingSize.ifBlank { "1 serving" },
                    calories = calories,
                    proteinGrams = protein,
                    carbsGrams = carbs,
                    fatGrams = fat,
                    isCustom = true,
                    source = "manual"
                )
                
                // Save food item
                val foodItemId = foodRepository.insertFoodItem(foodItem)
                
                // Log the food
                foodRepository.logFood(
                    foodItem = foodItem.copy(id = foodItemId),
                    mealType = currentState.selectedMealType,
                    servings = servings
                )
                
                _state.value = _state.value.copy(isLoading = false, success = true)
                onSuccess()
                
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to save food"
                )
            }
        }
    }
}
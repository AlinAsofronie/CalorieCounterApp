package com.fittrackpro.app.ui.screens.food

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodLogScreen(
    viewModel: FoodLogViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Food") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Meal type selection
            Text("Meal Type", style = MaterialTheme.typography.titleMedium)
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("breakfast", "lunch", "dinner", "snack").forEach { meal ->
                    FilterChip(
                        selected = state.selectedMealType == meal,
                        onClick = { viewModel.updateMealType(meal) },
                        label = { Text(meal.replaceFirstChar { it.uppercase() }) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            Divider()
            
            // Food details
            OutlinedTextField(
                value = state.foodName,
                onValueChange = { viewModel.updateFoodName(it) },
                label = { Text("Food Name *") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = state.brand,
                onValueChange = { viewModel.updateBrand(it) },
                label = { Text("Brand (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = state.servingSize,
                onValueChange = { viewModel.updateServingSize(it) },
                label = { Text("Serving Size") },
                placeholder = { Text("e.g., 100g, 1 cup") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = state.servings,
                onValueChange = { viewModel.updateServings(it) },
                label = { Text("Number of Servings") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            
            Divider()
            
            Text("Nutritional Information (per serving)", 
                style = MaterialTheme.typography.titleMedium)
            
            OutlinedTextField(
                value = state.calories,
                onValueChange = { viewModel.updateCalories(it) },
                label = { Text("Calories *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.protein,
                    onValueChange = { viewModel.updateProtein(it) },
                    label = { Text("Protein (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                
                OutlinedTextField(
                    value = state.carbs,
                    onValueChange = { viewModel.updateCarbs(it) },
                    label = { Text("Carbs (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                
                OutlinedTextField(
                    value = state.fat,
                    onValueChange = { viewModel.updateFat(it) },
                    label = { Text("Fat (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }
            
            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { viewModel.saveFood(onNavigateBack) },
                enabled = !state.isLoading && state.foodName.isNotBlank() && 
                         state.calories.toFloatOrNull() != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Save Food")
                }
            }
        }
    }
}
package com.fittrackpro.app.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fittrackpro.app.data.local.entities.FoodItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToFoodLog: () -> Unit,
    onNavigateToTodaysFoodLog: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FitTrack Pro") },
                actions = {
                    TextButton(onClick = onNavigateToProfile) {
                        Text("Profile")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToFoodLog
            ) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Greeting
                Text(
                    text = "Hello, ${state.userProfile?.name ?: "there"}!",
                    style = MaterialTheme.typography.headlineMedium
                )
                
                // Main calorie card
                CalorieCard(state)
                
                // Macros card
                MacrosCard(state)
                
                // Recent Foods Section
                if (state.recentFoods.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Recent Foods",
                                style = MaterialTheme.typography.titleMedium
                            )
                            
                            state.recentFoods.take(5).forEach { food ->
                                RecentFoodItem(
                                    food = food,
                                    onClick = { /* TODO: Quick add this food */ }
                                )
                            }
                        }
                    }
                }
                
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateToFoodLog,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add Food")
                    }
                    
                    OutlinedButton(
                        onClick = onNavigateToTodaysFoodLog,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("View Log")
                    }
                }
            }
        }
    }
}

@Composable
fun CalorieCard(state: HomeState) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Calories Remaining",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "${state.caloriesRemaining}",
                style = MaterialTheme.typography.displayLarge,
                color = if (state.caloriesRemaining >= 0) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.error
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalorieInfo(
                    label = "Goal",
                    value = "${state.userProfile?.dailyCalorieGoal ?: 0}"
                )
                CalorieInfo(
                    label = "Eaten",
                    value = "${state.totalCaloriesToday.toInt()}"
                )
                CalorieInfo(
                    label = "Burned",
                    value = "0" // TODO: Add exercise tracking
                )
            }
        }
    }
}

@Composable
fun CalorieInfo(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun MacrosCard(state: HomeState) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Macronutrients",
                style = MaterialTheme.typography.titleMedium
            )
            
            MacroProgress(
                label = "Protein",
                current = state.proteinConsumed,
                goal = state.userProfile?.proteinGoalGrams?.toFloat() ?: 0f,
                unit = "g",
                color = MaterialTheme.colorScheme.primary
            )
            
            MacroProgress(
                label = "Carbs",
                current = state.carbsConsumed,
                goal = state.userProfile?.carbsGoalGrams?.toFloat() ?: 0f,
                unit = "g",
                color = MaterialTheme.colorScheme.secondary
            )
            
            MacroProgress(
                label = "Fat",
                current = state.fatConsumed,
                goal = state.userProfile?.fatGoalGrams?.toFloat() ?: 0f,
                unit = "g",
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun MacroProgress(
    label: String,
    current: Float,
    goal: Float,
    unit: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${current.toInt()} / ${goal.toInt()} $unit",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        LinearProgressIndicator(
            progress = if (goal > 0) (current / goal).coerceIn(0f, 1f) else 0f,
            modifier = Modifier.fillMaxWidth(),
            color = color
        )
    }
}

@Composable
fun RecentFoodItem(
    food: FoodItem,
    onClick: () -> Unit
) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = food.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (food.brand != null) {
                    Text(
                        text = food.brand,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = "${food.calories.toInt()} cal",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
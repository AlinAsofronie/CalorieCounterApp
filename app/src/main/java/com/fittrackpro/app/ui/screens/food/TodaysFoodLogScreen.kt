package com.fittrackpro.app.ui.screens.food

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fittrackpro.app.data.local.entities.FoodLog
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodaysFoodLogScreen(
    viewModel: TodaysFoodLogViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToAddFood: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Today's Food Log") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                },
                actions = {
                    TextButton(onClick = onNavigateToAddFood) {
                        Text("Add Food")
                    }
                }
            )
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
        } else if (state.foodLogs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "No food logged today",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Button(onClick = onNavigateToAddFood) {
                        Text("Add Your First Food")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Group by meal type
                val groupedLogs = state.foodLogs.groupBy { it.mealType }
                
                groupedLogs.forEach { (mealType, logs) ->
                    item {
                        Text(
                            text = mealType.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    items(logs) { foodLog ->
                        FoodLogItem(
                            foodLog = foodLog,
                            onDelete = { viewModel.deleteFoodLog(it) }
                        )
                    }
                }
                
                // Summary
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    DailySummaryCard(state.foodLogs)
                }
            }
        }
    }
    
    // Show error if any
    state.error?.let { error ->
        LaunchedEffect(error) {
            // You could show a snackbar here
        }
    }
}

@Composable
fun FoodLogItem(
    foodLog: FoodLog,
    onDelete: (FoodLog) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Food Item", // TODO: Get actual food name from FoodItem
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${foodLog.servings}x servings",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${foodLog.totalCalories.toInt()} cal | ${foodLog.totalProtein.toInt()}g protein",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Food Log") },
            text = { Text("Are you sure you want to delete this food entry?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(foodLog)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun DailySummaryCard(foodLogs: List<FoodLog>) {
    val totalCalories = foodLogs.sumOf { it.totalCalories.toDouble() }.toFloat()
    val totalProtein = foodLogs.sumOf { it.totalProtein.toDouble() }.toFloat()
    val totalCarbs = foodLogs.sumOf { it.totalCarbs.toDouble() }.toFloat()
    val totalFat = foodLogs.sumOf { it.totalFat.toDouble() }.toFloat()
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Daily Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryItem("Calories", "${totalCalories.toInt()}")
                SummaryItem("Protein", "${totalProtein.toInt()}g")
                SummaryItem("Carbs", "${totalCarbs.toInt()}g")
                SummaryItem("Fat", "${totalFat.toInt()}g")
            }
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
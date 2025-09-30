package com.fittrackpro.app.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = viewModel(),
    onComplete: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Setup Profile") },
                navigationIcon = {
                    if (state.currentStep > 0) {
                        IconButton(onClick = { viewModel.previousStep() }) {
                            Text("←")
                        }
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress indicator
            LinearProgressIndicator(
                progress = { (state.currentStep + 1) / 5f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
            
            when (state.currentStep) {
                0 -> BasicInfoStep(state, viewModel)
                1 -> PhysicalStatsStep(state, viewModel)
                2 -> GoalStep(state, viewModel)
                3 -> ActivityLevelStep(state, viewModel)
                4 -> SummaryStep(state, viewModel, onComplete)
            }
        }
    }
}

@Composable
fun BasicInfoStep(state: OnboardingState, viewModel: OnboardingViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Welcome to FitTrack Pro!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Let's get to know you",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text("Your Name") },
            modifier = Modifier.fillMaxWidth()
        )
        
        OutlinedTextField(
            value = state.age,
            onValueChange = { viewModel.updateAge(it) },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        
        Text(
            text = "Gender",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = state.gender == "male",
                onClick = { viewModel.updateGender("male") },
                label = { Text("Male") },
                modifier = Modifier.weight(1f)
            )
            
            FilterChip(
                selected = state.gender == "female",
                onClick = { viewModel.updateGender("female") },
                label = { Text("Female") },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { viewModel.nextStep() },
            enabled = state.name.isNotBlank() && state.age.toIntOrNull() != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun PhysicalStatsStep(state: OnboardingState, viewModel: OnboardingViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Your Physical Stats",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        OutlinedTextField(
            value = state.heightCm,
            onValueChange = { viewModel.updateHeight(it) },
            label = { Text("Height (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            supportingText = { Text("Example: 175") }
        )
        
        OutlinedTextField(
            value = state.currentWeightKg,
            onValueChange = { viewModel.updateCurrentWeight(it) },
            label = { Text("Current Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            supportingText = { Text("Example: 80.5") }
        )
        
        OutlinedTextField(
            value = state.goalWeightKg,
            onValueChange = { viewModel.updateGoalWeight(it) },
            label = { Text("Goal Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            supportingText = { Text("Example: 75.0") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { viewModel.nextStep() },
            enabled = state.heightCm.toIntOrNull() != null &&
                    state.currentWeightKg.toFloatOrNull() != null &&
                    state.goalWeightKg.toFloatOrNull() != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun GoalStep(state: OnboardingState, viewModel: OnboardingViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "What's your goal?",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        GoalOption(
            title = "Lose Weight",
            description = "Reduce body fat with a calorie deficit",
            selected = state.goal == "lose_weight",
            onClick = { viewModel.updateGoal("lose_weight") }
        )
        
        GoalOption(
            title = "Maintain Weight",
            description = "Stay at your current weight",
            selected = state.goal == "maintain",
            onClick = { viewModel.updateGoal("maintain") }
        )
        
        GoalOption(
            title = "Gain Weight",
            description = "Build muscle and increase mass",
            selected = state.goal == "gain_weight",
            onClick = { viewModel.updateGoal("gain_weight") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { viewModel.nextStep() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun GoalOption(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ActivityLevelStep(state: OnboardingState, viewModel: OnboardingViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Activity Level",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Text(
            text = "How active are you?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        ActivityOption(
            title = "Sedentary",
            description = "Little or no exercise",
            selected = state.activityLevel == "sedentary",
            onClick = { viewModel.updateActivityLevel("sedentary") }
        )
        
        ActivityOption(
            title = "Lightly Active",
            description = "Exercise 1-3 days/week",
            selected = state.activityLevel == "light",
            onClick = { viewModel.updateActivityLevel("light") }
        )
        
        ActivityOption(
            title = "Moderately Active",
            description = "Exercise 3-5 days/week",
            selected = state.activityLevel == "moderate",
            onClick = { viewModel.updateActivityLevel("moderate") }
        )
        
        ActivityOption(
            title = "Very Active",
            description = "Exercise 6-7 days/week",
            selected = state.activityLevel == "active",
            onClick = { viewModel.updateActivityLevel("active") }
        )
        
        ActivityOption(
            title = "Extra Active",
            description = "Very intense exercise daily",
            selected = state.activityLevel == "very_active",
            onClick = { viewModel.updateActivityLevel("very_active") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { viewModel.nextStep() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun ActivityOption(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SummaryStep(
    state: OnboardingState,
    viewModel: OnboardingViewModel,
    onComplete: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "You're All Set!",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Your Profile",
                    style = MaterialTheme.typography.titleMedium
                )
                HorizontalDivider()
                SummaryRow("Name", state.name)
                SummaryRow("Age", "${state.age} years")
                SummaryRow("Height", "${state.heightCm} cm")
                SummaryRow("Current Weight", "${state.currentWeightKg} kg")
                SummaryRow("Goal Weight", "${state.goalWeightKg} kg")
                SummaryRow("Activity Level", state.activityLevel.replace("_", " ").replaceFirstChar { it.uppercase() })
                SummaryRow("Goal", state.goal.replace("_", " ").replaceFirstChar { it.uppercase() })
            }
        }
        
        if (state.error != null) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = { viewModel.completeOnboarding(onComplete) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Start Tracking!")
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
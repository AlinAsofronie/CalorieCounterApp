package com.fittrackpro.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fittrackpro.app.ui.screens.onboarding.OnboardingScreen
import com.fittrackpro.app.ui.screens.home.HomeScreen
import com.fittrackpro.app.ui.screens.food.FoodLogScreen
import com.fittrackpro.app.ui.screens.food.TodaysFoodLogScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToFoodLog = {
                    navController.navigate(Screen.FoodLog.route)
                },
                onNavigateToTodaysFoodLog = {
                    navController.navigate(Screen.TodaysFoodLog.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
        
        composable(Screen.Profile.route) {
            // Placeholder for profile screen
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Profile Screen - Coming Soon!")
            }
        }
        
        composable(Screen.FoodLog.route) {
            FoodLogScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.TodaysFoodLog.route) {
            TodaysFoodLogScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddFood = { navController.navigate(Screen.FoodLog.route) }
            )
        }
        
        // We'll add more screens as we build them
    }
}
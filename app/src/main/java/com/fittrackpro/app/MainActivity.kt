package com.fittrackpro.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.fittrackpro.app.ui.navigation.NavGraph
import com.fittrackpro.app.ui.navigation.Screen
import com.fittrackpro.app.ui.theme.FitTrackProTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val userRepository = (application as FitTrackApp).userRepository
        
        setContent {
            FitTrackProTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var startDestination by remember { mutableStateOf<String?>(null) }
                    
                    // Check if user has completed onboarding
                    LaunchedEffect(Unit) {
                        val hasProfile = userRepository.hasUserProfile()
                        startDestination = if (hasProfile) {
                            Screen.Home.route
                        } else {
                            Screen.Onboarding.route
                        }
                    }
                    
                    // Show navigation once we know where to start
                    startDestination?.let { destination ->
                        val navController = rememberNavController()
                        NavGraph(
                            navController = navController,
                            startDestination = destination
                        )
                    }
                }
            }
        }
    }
}
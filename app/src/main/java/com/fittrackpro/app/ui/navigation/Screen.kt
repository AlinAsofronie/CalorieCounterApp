package com.fittrackpro.app.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object FoodLog : Screen("food_log")
    object TodaysFoodLog : Screen("todays_food_log")
    object FoodSearch : Screen("food_search")
    object BarcodeScanner : Screen("barcode_scanner")
    object Profile : Screen("profile")
    object WeightLog : Screen("weight_log")
}
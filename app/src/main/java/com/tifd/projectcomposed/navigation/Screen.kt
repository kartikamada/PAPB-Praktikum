package com.tifd.projectcomposed.navigation

sealed class Screen(val route: String) {
    object Schedule : Screen(route = "Schedule")
    object Task : Screen(route = "Task")
    object Profile : Screen(route = "Profile")
    object Camera : Screen(route = "Camera")
}
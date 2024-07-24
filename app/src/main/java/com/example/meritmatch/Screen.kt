package com.example.meritmatch

sealed class Screen (val route : String) {
    data object Login : Screen("login_page")
    data object SignUp : Screen("signup_page")
    data object Home : Screen("home_page")
    data object Search : Screen("search_page")
    data object Settings : Screen("settings_page")
    data object CreateTasks : Screen("create_page")
    data object ModifyTasks : Screen("modify_tasks")
    data object SubmittedTasks : Screen("submitted_tasks")
    data object WaitingTasks : Screen("waiting_tasks")
    data object AvailableTasks : Screen("available_tasks")
    data object ReservedTasks : Screen("reserved_tasks")
    data object PostedTasks : Screen("posted_tasks")
}
package com.example.meritmatch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

var allTasks = mutableStateOf(listOf<Task>())
var reservedTasks = mutableStateOf(listOf<Task>())
var postedTasks = mutableStateOf(listOf<Task>())

@Composable
fun Navigation (modifier : Modifier) {
    val navController = rememberNavController()
    val viewModel : MainViewModel = viewModel()

    NavHost (
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable (
            route = Screen.Login.route
        ) {
            LoginPage (
                modifier = modifier,
                onLogin = {
                    viewModel.getReservedTasks(user_id.value)
                    viewModel.getAvailableTasks(user_id.value)
                    viewModel.getPostedTasks(user_id.value)
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                goToSignUp = {
                    navController.navigate(Screen.SignUp.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                dataViewModel = viewModel
            )
        }

        composable (
            route = Screen.SignUp.route
        ) {
            SignUpPage (
                modifier = modifier,
                onSignUp = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                goToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                dataViewModel = viewModel
            )
        }

        composable (
            route = Screen.Home.route
        ) {
            HomePage (
                modifier = modifier,
                navController = navController,
                onLogout = {
                    navController.navigate(Screen.Login.route)
                },
                toAvailableTasks = {
                    setValues(viewModel)
                    navController.navigate(Screen.AvailableTasks.route)
                },
                toReservedTasks = {
                    setValues(viewModel)
                    navController.navigate(Screen.ReservedTasks.route)
                },
                toPostedTasks = {
                    setValues(viewModel)
                    navController.navigate(Screen.PostedTasks.route)
                },
                toCreateTask = {
                    navController.navigate(Screen.CreateTasks.route)
                },
                dataViewModel = viewModel
            )
        }

        composable (
            route = Screen.Search.route
        ) {
            SearchPage (
                modifier = modifier,
                navController = navController
            )
        }

        composable (
            route = Screen.Settings.route
        ) {
            SettingsPage (
                modifier = modifier,
                navController = navController
            ) {
                navController.navigate(Screen.Login.route)
            }
        }

        composable (
            route = Screen.AvailableTasks.route
        ) {
            TaskListPage (
                modifier = modifier,
                navController = navController,
                label = "Available Tasks"
            )
        }

        composable (
            route = Screen.ReservedTasks.route
        ) {
            TaskListPage (
                modifier = modifier,
                navController = navController,
                label = "Reserved Tasks"
            )
        }

        composable (
            route = Screen.PostedTasks.route
        ) {
            TaskListPage (
                modifier = modifier,
                navController = navController,
                label = "Posted Tasks"
            )
        }

        composable (
            route = Screen.CreateTasks.route
        ) {
            CreateTask (
                dataViewModel = viewModel,
                goHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.CreateTasks.route) { inclusive = true }
                    }
                }
            )
        }

    }
}

fun setValues (
    dataViewModel: MainViewModel
) {
    allTasks = dataViewModel.stateOfAllTasks.value.status
    postedTasks = dataViewModel.stateOfPostedTasks.value.status
    reservedTasks = dataViewModel.stateOfReservedTasks.value.status
}
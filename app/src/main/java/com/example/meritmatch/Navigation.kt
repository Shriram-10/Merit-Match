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
var submittedTasks = mutableStateOf(listOf<Task>())
var waitingTasks = mutableStateOf(listOf<Task>())

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
                    viewModel.getSubmittedTasks(user_id.value)
                    viewModel.getWaitingTasks(user_id.value)
                    viewModel.getBalance(user_id.value)
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
                toSubmittedTasks = {
                    navController.navigate(Screen.SubmittedTasks.route)
                },
                toWaitingTasks = {
                    navController.navigate(Screen.WaitingTasks.route)
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
                label = "Available Tasks",
                dataViewModel = viewModel,
                toModify = {}
            )
        }

        composable (
            route = Screen.ReservedTasks.route
        ) {
            TaskListPage (
                modifier = modifier,
                navController = navController,
                label = "Reserved Tasks",
                dataViewModel = viewModel,
                toModify = {}
            )
        }

        composable (
            route = Screen.PostedTasks.route
        ) {
            TaskListPage (
                modifier = modifier,
                navController = navController,
                label = "Posted Tasks",
                dataViewModel = viewModel,
                toModify = {
                    navController.navigate(Screen.ModifyTasks.route)
                }
            )
        }

        composable (
            route = Screen.SubmittedTasks.route
        ) {
            TaskListPage (
                modifier = modifier,
                navController = navController,
                label = "Submitted Tasks",
                dataViewModel = viewModel,
                toModify = {}
            )
        }

        composable (
            route = Screen.WaitingTasks.route
        ) {
            TaskListPage (
                modifier = modifier,
                navController = navController,
                label = "Pending Approvals",
                dataViewModel = viewModel,
                toModify = {}
            )
        }

        composable (
            route = Screen.CreateTasks.route
        ) {
            CreateTask (
                dataViewModel = viewModel,
                label = "Create Task",
                task = draft.value,
                goHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.CreateTasks.route) { inclusive = true }
                    }
                }
            )
        }

        composable (
            route = Screen.ModifyTasks.route
        ) {
            CreateTask (
                dataViewModel = viewModel,
                label = "Modify Task",
                task = modifyDraft.value,
                goHome = {
                    navController.navigate(Screen.PostedTasks.route) {
                        popUpTo(Screen.ModifyTasks.route) { inclusive = true }
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
    submittedTasks = dataViewModel.stateOfSubmittedTasks.value.status
    waitingTasks = dataViewModel.stateOfWaitingTasks.value.status
    karma_points.value = dataViewModel.stateOfGettingBalance.value.status.balance
}
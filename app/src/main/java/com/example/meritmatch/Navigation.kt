package com.example.meritmatch

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation

@Composable
fun Navigation (modifier : Modifier) {
    val navController = rememberNavController()

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
                    navController.navigate(Screen.Home.route)
                },
                goToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable (
            route = Screen.SignUp.route
        ) {
            SignUpPage (
                modifier = modifier,
                onSignUp = {
                    navController.navigate(Screen.Login.route)
                },
                goToLogin = {
                    navController.navigate(Screen.Login.route)
                }
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
                    navController.navigate(Screen.AvailableTasks.route)
                },
                toReservedTasks = {
                    navController.navigate(Screen.ReservedTasks.route)
                },
                toPostedTasks = {
                    navController.navigate(Screen.PostedTasks.route)
                }
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
                modifier = modifier
            )
        }

        composable (
            route = Screen.ReservedTasks.route
        ) {
            TaskListPage (
                modifier = modifier
            )
        }

        composable (
            route = Screen.PostedTasks.route
        ) {
            TaskListPage (
                modifier = modifier
            )
        }

    }
}
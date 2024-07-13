package com.example.meritmatch

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(modifier : Modifier){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login_page"
    ) {
        composable (
            route = "login_page"
        ) {
            LoginPage(
                modifier = modifier,
                onLogin = {
                    navController.navigate("home_page")
                },
                goToSignUp = {
                    navController.navigate("sign_up_page")
                }
            )
        }

        composable (
            route = "sign_up_page"
        ) {
            SignUpPage(
                modifier = modifier,
                onSignUp = {
                    navController.navigate("login_page")
                },
                goToLogin = {
                    navController.navigate("login_page")
                }
            )
        }

        composable (
            route = "home_page"
        ) {
            HomePage(
                modifier = modifier,
                navController = navController
            ) {
                navController.navigate("login_page")
            }
        }

        composable (
            route = "search_page"
        ) {
            SearchPage(
                modifier = modifier,
                navController = navController
            )
        }

        composable (
            route = "settings_page"
        ) {
            SettingsPage(
                modifier = modifier,
                navController = navController
            ) {
                navController.navigate("login_page")
            }
        }
    }
}
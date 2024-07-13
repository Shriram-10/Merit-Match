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
        composable(
            route = "login_page"
        ){
            LoginPage(modifier = modifier){
                navController.navigate("home_page")
            }
        }

        composable(
            route = "home_page"
        ) {
            HomePage(modifier = modifier){
                navController.navigate("login_page")
            }
        }
    }
}
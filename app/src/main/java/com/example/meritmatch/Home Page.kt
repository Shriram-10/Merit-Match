package com.example.meritmatch

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun HomePage(
    modifier: Modifier,
    navController: NavController,
    onLogout: () -> Unit
) {
    val color = MaterialTheme.colorScheme
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    val selected = navBackStackEntry?.destination?.route === item.route
                    NavigationBarItem(
                        selected = selected,
                        label = {
                            Text(
                                text = item.title
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {

    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = "Welcome, Username",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "What would you like to do today?",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            ) {
                drawLine(
                    color = color.primaryContainer,
                    start = Offset(size.width * 0.05f, size.height / 2),
                    end = Offset(size.width * 0.95f, size.height / 2),
                    strokeWidth = 4f
                )
            }
        }
    }
}
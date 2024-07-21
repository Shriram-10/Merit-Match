package com.example.meritmatch

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun TaskListPage (
    modifier: Modifier,
    navController: NavController,
    label: String
) {
    val color = MaterialTheme.colorScheme
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    BackHandler {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) {
                inclusive = true
            }
        }
    }

    Scaffold (
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    val selected = navBackStackEntry?.destination?.route === item.route
                    NavigationBarItem (
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
    ) { innerPadding ->
        Column (
            modifier = modifier
                .padding(
                    bottom = innerPadding.calculateBottomPadding() * 0.60f,
                    top = innerPadding.calculateTopPadding() * 0.60f
                )
                .fillMaxSize()
        ) {
            Headline(text = label)

            HorizontalLine(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                start = 0.05f,
                end = 0.95f
            )

            Box (
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn {
                    items (
                        count = if (label === "Available Tasks") allTasks.value.size
                        else if (label === "Reserved Tasks") reservedTasks.value.size
                        else if (label === "Posted Tasks") postedTasks.value.size
                        else 1
                    ) { item ->
                        Row (
                            modifier = Modifier
                                .fillMaxSize(0.95f)
                                .height(100.dp)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(20))
                                .background(color.primaryContainer),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                when (label) {
                                    "Available Tasks" -> if (allTasks.value.isNotEmpty()) allTasks.value[item].toString() else "No Available Tasks"
                                    "Reserved Tasks" -> if (reservedTasks.value.isNotEmpty()) reservedTasks.value[item].toString() else "No Reserved Tasks"
                                    "Posted Tasks" -> if (postedTasks.value.isNotEmpty()) postedTasks.value[item].toString() else "No Posted Tasks"
                                    else -> "Nothing"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
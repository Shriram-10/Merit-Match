package com.example.meritmatch

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

var localUsername = mutableStateOf("")
var user_id = mutableStateOf(0)
@Composable
fun HomePage (
    modifier: Modifier,
    navController: NavController,
    onLogout: () -> Unit,
    toAvailableTasks: () -> Unit,
    toReservedTasks: () -> Unit,
    toPostedTasks: () -> Unit,
    dataViewModel: MainViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    BackHandler {
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    Scaffold (
        topBar = @Composable {
            CustomTopAppBar (
                title = "Home",
                searchNavigate = {
                    navController.navigate(Screen.Search.route)
                },
                settingsNavigate = {
                    navController.navigate(Screen.Settings.route)
                },
                endIcon = Icons.Outlined.Settings,
                startIcon =  Icons.Outlined.Search
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    val selected = navBackStackEntry?.destination?.route === item.route
                    NavigationBarItem (
                        selected = selected,
                        label = {
                            Text (
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
        Box (
            modifier = modifier.padding(bottom = innerPadding.calculateBottomPadding() * 0.69f, top = innerPadding.calculateTopPadding() * 0.60f),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn {
                item {
                    Headline (modifier = Modifier.padding(top = 24.dp, bottom = 8.dp), text = "Welcome, ${localUsername.value}")

                    Text (
                        text = "What would you like to do today?",
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    HorizontalLine (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp),
                        start = 0.05f,
                        end = 0.95f
                    )

                    BalanceKP (modifier = Modifier.padding(16.dp), balance = karma_points.value)

                    LabeledTaskView (
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .fillMaxWidth(0.95f)
                            .height(225.dp),
                        label = "Available Tasks",
                        onViewMore = toAvailableTasks
                    )

                    LabeledTaskView (
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .fillMaxWidth(0.95f)
                            .height(225.dp),
                        label = "Reserved Tasks",
                        onViewMore = toReservedTasks
                    )

                    LabeledTaskView (
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .fillMaxWidth(0.95f)
                            .height(225.dp),
                        label = "Posted Tasks",
                        onViewMore = toPostedTasks
                    )
                }
            }
        }
    }
}
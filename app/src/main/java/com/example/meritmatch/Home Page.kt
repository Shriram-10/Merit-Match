package com.example.meritmatch

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

var localUsername = mutableStateOf("")
var user_id = mutableStateOf(0)

@Composable
fun HomePage (
    modifier: Modifier,
    navController: NavController,
    onLogout: () -> Unit,
    toAvailableTasks: () -> Unit,
    toReservedTasks: () -> Unit,
    toSubmittedTasks: () -> Unit,
    toPostedTasks: () -> Unit,
    toCreateTask: () -> Unit,
    toWaitingTasks: () -> Unit,
    toHistoryTasks: () -> Unit,
    toSearch: () -> Unit,
    toSettings: () -> Unit,
    dataViewModel: MainViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val color = MaterialTheme.colorScheme
    val colorList = listOf (
        color.primaryContainer.copy(0.7f),
        color.secondaryContainer,
        color.tertiaryContainer,
        color.background
    )
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }
    var displayToast by remember { mutableStateOf(false ) }
    var newHomePage by remember { mutableStateOf(false) }

    setValues(dataViewModel)

    BackHandler {
        dataViewModel.logOut(localUsername.value)
        displayLoading.value = true
    }

    if (displayLoading.value) {
        LoadingPage()
    }
    if (displayLoading.value && dataViewModel.stateOfLogout.value.status == 1) {
        displayLoading.value = false
        android.os.Process.killProcess(android.os.Process.myPid())
    } else if (displayLoading.value && dataViewModel.stateOfLogout.value.status == -1) {
        message = "Failed to exit app. Try again."
        displayLoading.value = false
        dataViewModel.stateOfLogout.value.status = 0
    }
    if (displayToast) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    Scaffold (
        topBar = @Composable {
            CustomTopAppBar (
                title = "Home",
                searchNavigate = toSearch,
                settingsNavigate = toSettings,
                endIcon = Icons.Outlined.Settings,
                startIcon =  Icons.Outlined.Search
            )
        },
        bottomBar = {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = Brush.verticalGradient(colorList))
            ) {
                NavigationBar (
                    containerColor = Color.Transparent
                ) {
                    items.forEach { item ->
                        val selected = navBackStackEntry?.destination?.route === item.route
                        NavigationBarItem (
                            selected = selected,
                            label = {
                                Text (item.title)
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
        }
    ) { innerPadding ->
        Box (
            modifier = modifier.padding(bottom = innerPadding.calculateBottomPadding() * 0.69f, top = innerPadding.calculateTopPadding() * 0.60f),
            contentAlignment = Alignment.BottomEnd
        ) {
            SwipeRefresh (
                state = rememberSwipeRefreshState(isRefreshing = refreshing1.value),
                onRefresh = { refreshing1.value = true }
            ) {
                LazyColumn {
                    item {
                        Headline(
                            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                            text = "Welcome, ${localUsername.value}"
                        )

                        Text(
                            text = "What would you like to do today?",
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(start = 16.dp),
                            color = MaterialTheme.colorScheme.primary
                        )

                        HorizontalLine(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp),
                            start = 0.05f,
                            end = 0.95f
                        )

                        BalanceKP(modifier = Modifier.padding(16.dp), balance = karma_points.value)

                        LabeledTaskView (
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .fillMaxWidth(0.95f)
                                .height(225.dp),
                            label = "Submitted Tasks",
                            onViewMore = toSubmittedTasks,
                            dataViewModel = dataViewModel
                        )

                        LabeledTaskView (
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .fillMaxWidth(0.95f)
                                .height(225.dp),
                            label = "Pending Approvals",
                            onViewMore = toWaitingTasks,
                            dataViewModel = dataViewModel
                        )

                        LabeledTaskView (
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .fillMaxWidth(0.95f)
                                .height(225.dp),
                            label = "Tasks History",
                            onViewMore = toHistoryTasks,
                            dataViewModel = dataViewModel
                        )
                    }
                }
            }

            Box (
                modifier = Modifier
                    .height(70.dp)
                    .width(78.dp)
                    .padding(bottom = 12.dp, end = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        toCreateTask()
                    },
                    modifier = Modifier.fillMaxSize(),
                    colors = ButtonDefaults.buttonColors (
                        containerColor = color.secondaryContainer
                    ),
                    elevation = ButtonDefaults.buttonElevation (
                        defaultElevation = 8.dp,
                        pressedElevation = 0.dp
                    ),
                    shape = RoundedCornerShape(30)
                ) {

                }

                Icon (
                    imageVector = Icons.Outlined.Create,
                    contentDescription = null,
                    tint = color.surfaceTint
                )
            }
        }
    }

    LaunchedEffect (displayLoading.value) {
        delay(5500)
        if (displayLoading.value && !dataViewModel.isApiConnected()) {
            message = "Unable to access the API."
            displayToast = true
            displayLoading.value = false
        }
    }

    LaunchedEffect(refreshing2.value || refreshing1.value) {
        if (refreshing2.value || refreshing1.value) {
            dataViewModel.getPostedTasks(user_id.value)
            dataViewModel.getReservedTasks(user_id.value)
            dataViewModel.getAvailableTasks(user_id.value)
            dataViewModel.getSubmittedTasks(user_id.value)
            dataViewModel.getWaitingTasks(user_id.value)
            dataViewModel.getHistoryTasks(user_id.value)
            delay(2000)
            setValues.value = true
            newHomePage = true
            if (refreshing2.value) {
                refreshing2.value = false
            } else if (refreshing1.value) {
                refreshing1.value = false
            }
        }
    }

    if (setValues.value) {
        setValues(dataViewModel)
        setValues.value = false
    }

    if (newHomePage) {
        HomePage(
            modifier = modifier,
            navController = navController,
            onLogout = onLogout,
            toAvailableTasks = toAvailableTasks,
            toReservedTasks = toReservedTasks,
            toSubmittedTasks = toSubmittedTasks,
            toPostedTasks = toPostedTasks,
            toCreateTask = toCreateTask,
            toWaitingTasks = toWaitingTasks,
            dataViewModel = dataViewModel,
            toSettings = toSettings,
            toSearch = toSearch,
            toHistoryTasks = toHistoryTasks
        )
    }
}
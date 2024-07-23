package com.example.meritmatch

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

var setValues = mutableStateOf(false)
var displayLoading = mutableStateOf(false)
var refreshing1 = mutableStateOf(false)
var refreshing2 = mutableStateOf(false)


@Composable
fun TaskListPage (
    modifier: Modifier,
    navController: NavController,
    label: String,
    dataViewModel: MainViewModel
) {
    val color = MaterialTheme.colorScheme
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val colorList = listOf (
        color.primaryContainer,
        color.secondaryContainer,
        color.tertiaryContainer,
        color.background
    )

    BackHandler {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) {
                inclusive = true
            }
        }
    }

    Scaffold (
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
        }
    ) { innerPadding ->
        Column(
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
                contentAlignment = Alignment.TopCenter
            ) {
                SwipeRefresh (
                    state = rememberSwipeRefreshState(isRefreshing = refreshing1.value),
                    onRefresh = { refreshing1.value = true }
                ) {
                    LazyColumn {
                        items(
                            count = if (label == "Available Tasks" && allTasks.value.isNotEmpty()) allTasks.value.size
                                else if (label == "Posted Tasks" && postedTasks.value.isNotEmpty()) postedTasks.value.size
                                else if (label == "Reserved Tasks" && reservedTasks.value.isNotEmpty()) reservedTasks.value.size
                                else 1
                        ) { item ->
                            if (label == "Available Tasks" && allTasks.value.isNotEmpty()) {
                                TaskListItem (
                                    task = allTasks.value[item],
                                    isPosted = label == "Posted Tasks",
                                    isReserved = label == "Reserved Tasks",
                                    dataViewModel = dataViewModel
                                )
                            } else if (label == "Posted Tasks" && postedTasks.value.isNotEmpty()) {
                                TaskListItem (
                                    task = postedTasks.value[item],
                                    isPosted = label == "Posted Tasks",
                                    isReserved = label == "Reserved Tasks",
                                    dataViewModel = dataViewModel
                                )
                            } else if (label == "Reserved Tasks" && reservedTasks.value.isNotEmpty()) {
                                TaskListItem (
                                    task = reservedTasks.value[item],
                                    isPosted = label == "Posted Tasks",
                                    isReserved = label == "Reserved Tasks",
                                    dataViewModel = dataViewModel
                                )
                            } else {
                                Spacer(modifier = Modifier.height(250.dp))
                                Text(
                                    text = "No tasks found",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = color.onPrimaryContainer.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(refreshing1.value) {
        if (refreshing1.value) {
            dataViewModel.getPostedTasks(user_id.value)
            dataViewModel.getReservedTasks(user_id.value)
            dataViewModel.getAvailableTasks(user_id.value)
            delay(2000)
            setValues.value = true
            refreshing1.value = false
        }
    }

    LaunchedEffect(refreshing2.value) {
        if (refreshing2.value) {
            dataViewModel.getPostedTasks(user_id.value)
            dataViewModel.getReservedTasks(user_id.value)
            dataViewModel.getAvailableTasks(user_id.value)
            delay(2000)
            setValues.value = true
            refreshing2.value = false
        }
    }

    if (setValues.value) {
        setValues(dataViewModel)
        setValues.value = false
    }

    if (displayLoading.value) {
        LoadingPage()
    }
}

@Composable
fun TaskListItem (
    task: Task,
    isPosted : Boolean,
    isReserved : Boolean,
    dataViewModel: MainViewModel
) {
    val color = MaterialTheme.colorScheme
    var message by remember { mutableStateOf("") }
    var displayToast by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column (
        modifier = Modifier
            .fillMaxSize(0.95f)
            .padding(16.dp)
            .clip(RoundedCornerShape(15))
            .background(color.primaryContainer),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Row (
            verticalAlignment = Alignment.Bottom
        ) {
            Text (
                text = task.title.uppercase(),
                modifier = Modifier.padding(top = 24.dp, start = 24.dp, bottom = 8.dp, end = 16.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )

            if (!isPosted) {
                Text(
                    text = "by ${task.username}",
                    modifier = Modifier.padding(bottom = 8.dp, end = 24.dp)
                )
            }
        }

        Row (
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, end = 6.dp, start = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(30))
                    .background(color.inversePrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Time Given : ${task.deadline.split(" ")[0]} Days and ${
                        task.deadline.split(
                            " "
                        )[1]
                    } Hours",
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20)),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Text (
            text = task.description,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )

        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Button (
                onClick = {
                    if (!isPosted) {
                        if (!isReserved) {
                            dataViewModel.reserveTasks(user_id.value, task.id)
                            displayLoading.value = true
                        } else {
                            dataViewModel.unreserveTasks(user_id.value, task.id)
                            displayLoading.value = true
                        }
                    }
                },
                modifier = Modifier.padding(start = 20.dp, end = 8.dp, top = 10.dp, bottom = 6.dp),
                shape = RoundedCornerShape(20),
                elevation = ButtonDefaults.buttonElevation (
                    defaultElevation = 8.dp,
                    pressedElevation = 0.dp
                ),
                colors = ButtonDefaults.buttonColors (
                    containerColor = color.onErrorContainer
                )
            ) {
                Text (
                    text = if (isPosted) "Modify Post" else if (isReserved) "Unreserve Task" else "Reserve Task"
                )
            }

            if (isReserved) {
                Button(
                    onClick = {},
                    modifier = Modifier.padding (
                        end = 20.dp,
                        top = 10.dp,
                        bottom = 6.dp
                    ),
                    shape = RoundedCornerShape(20),
                    elevation = ButtonDefaults.buttonElevation (
                        defaultElevation = 8.dp,
                        pressedElevation = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = color.onErrorContainer
                    )
                ) {
                    Text (
                        "Submit Task"
                    )
                }
            }
        }
    }

    if (displayLoading.value && dataViewModel.stateOfReservingTask.value.status == 1) {
        message = "Reserved Task successfully."
        displayToast = true
        refreshing2.value = true
        dataViewModel.stateOfReservingTask.value.status = 0
        displayLoading.value = false
    }

    if (displayLoading.value && dataViewModel.stateOfReservingTask.value.status == -1) {
        message = "Failed to reserve task. Try again."
        displayToast = true
        dataViewModel.stateOfReservingTask.value.status = 0
        displayLoading.value = false
    }

    if (displayLoading.value && dataViewModel.stateOfUnReservingTask.value.status == 1) {
        message = "Unreserved Task successfully."
        displayToast = true
        refreshing2.value = true
        dataViewModel.stateOfUnReservingTask.value.status = 0
        displayLoading.value = false
    }

    if (displayLoading.value && dataViewModel.stateOfUnReservingTask.value.status == -1) {
        message = "Failed to unreserve task. Try again."
        displayToast = true
        dataViewModel.stateOfUnReservingTask.value.status = 0
        displayLoading.value = false
    }

    if (displayToast) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        displayToast = false
    }
}
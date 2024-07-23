package com.example.meritmatch

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun TaskListPage (
    modifier: Modifier,
    navController: NavController,
    label: String,
    taskList: List<Task>
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
                contentAlignment = if (taskList.isNotEmpty()) Alignment.TopCenter else Alignment.Center
            ) {
                LazyColumn {
                    items (
                        count = if (taskList.isNotEmpty()) taskList.size
                        else 1
                    ) { item ->
                        if (taskList.isNotEmpty()) {
                            TaskListItem (
                                task = taskList[item],
                                isPosted = label == "Posted Tasks",
                                isReserved = label == "Reserved Tasks"
                            )
                        } else {
                            Text (
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

@Composable
fun TaskListItem (
    task: Task,
    isPosted : Boolean,
    isReserved : Boolean
) {
    val color = MaterialTheme.colorScheme
    Column (
        modifier = Modifier
            .fillMaxSize(0.95f)
            .padding(16.dp)
            .clip(RoundedCornerShape(15))
            .background(color.primaryContainer),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Text (
            text = task.title.uppercase(),
            modifier = Modifier.padding(top = 24.dp, start = 24.dp, bottom = 8.dp).width(80.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold
        )

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
                onClick = {},
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 6.dp),
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
                    text = if (isPosted) "Modify Post" else "Unreserve Post"
                )
            }

            if (isReserved) {
                Button(
                    onClick = {},
                    modifier = Modifier.padding (
                        start = 20.dp,
                        end = 20.dp,
                        top = 10.dp,
                        bottom = 6.dp
                    ),
                    shape = RoundedCornerShape(20),
                    elevation = ButtonDefaults.buttonElevation(
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
}
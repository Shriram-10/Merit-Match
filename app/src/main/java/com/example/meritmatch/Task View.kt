package com.example.meritmatch

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun TaskView (
    modifier: Modifier,
    onViewMore: () -> Unit,
    label: String,
    isUser: Boolean = true,
    dataViewModel : MainViewModel
) {
    val color = MaterialTheme.colorScheme
    var displayLoading by remember { mutableStateOf(false) }
    var displayToast by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current

    val fadeOutColors = listOf (
        Color.Black.copy(alpha = 0f),
        Color.Black.copy(alpha = 0.1f),
        Color.Black.copy(alpha = 0.2f),
        Color.Black.copy(alpha = 0.55f),
    )

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(245.dp),
    ) {
        Box {
            Column (
                modifier = modifier
                    .height(225.dp)
                    .clip(RoundedCornerShape(10)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LazyVerticalGrid (
                    columns = GridCells.Fixed(1),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color.inversePrimary),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items (
                        if (label == "Submitted Tasks" && submittedTasks.value.isNotEmpty()) submittedTasks.value.size
                        else if (label == "Tasks awaiting approval" && waitingTasks.value.isNotEmpty()) waitingTasks.value.size
                        else if (label == "Tasks History" && historyTasks.value.isNotEmpty() && isUser) historyTasks.value.size
                        else if (label == "Tasks History" && !isUser && queryUser.value.history_tasks.isNotEmpty()) queryUser.value.history_tasks.size
                        else 1
                    ) { item ->
                        Box (
                            modifier = Modifier
                                .clip(RoundedCornerShape(20))
                                .background(color.primaryContainer),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            if (label == "Submitted Tasks") {
                                if (submittedTasks.value.isNotEmpty()) {
                                    Column (
                                        horizontalAlignment = Alignment.Start,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        TaskViewItem(taskItem = submittedTasks.value[item])

                                    }
                                } else {
                                    Text (
                                        text = "None of your submitted tasks are awaiting payment.",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(20.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else if (label == "Pending Approvals") {
                                if (waitingTasks.value.isNotEmpty()) {
                                    TaskViewItem(taskItem = waitingTasks.value[item])
                                } else {
                                    Text (
                                        text = "None of your posted tasks are awaiting approval.",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(20.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else if (label == "Tasks History" && isUser) {
                                if (historyTasks.value.isNotEmpty()) {
                                    TaskViewItem(taskItem = historyTasks.value[item])
                                } else {
                                    Text (
                                        text = "Create or do a task to get started.",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(20.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else if (label == "Tasks History" && !isUser) {
                                if (historyTasks.value.isNotEmpty()) {
                                    TaskViewItem(taskItem = queryUser.value.history_tasks[item])
                                } else {
                                    Text (
                                        text = "No Task History as of yet.",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(20.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Box (
                modifier = modifier
                    .clip(RoundedCornerShape(10))
                    .fillMaxSize()
                    .background(brush = Brush.verticalGradient(fadeOutColors))
            ) {

            }
        }

        Box (
            modifier = Modifier
                .clip(RoundedCornerShape(100))
                .size(40.dp)
                .background(color.primaryContainer)
                .align(Alignment.BottomCenter)
                .shadow(elevation = 8.dp, clip = false, shape = RoundedCornerShape(100))
        ) {
            Button (
                onClick = {
                    if (label == "Tasks History") {
                        dataViewModel.getHistoryTasks(user_id.value)
                        displayLoading = true
                    }
                    onViewMore()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .border(
                        BorderStroke(width = 6.dp, color = color.primaryContainer),
                        shape = RoundedCornerShape(100)
                    ),
                colors = ButtonDefaults.buttonColors (
                    containerColor = color.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp
                )
            ) {

            }

            Icon (
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = color.primary,
                modifier = Modifier
                    .align(Alignment.Center)
                    .scale(1.5f)
            )
        }
    }

    LaunchedEffect (displayLoading) {
        if (displayLoading) {
            delay(200)
            displayLoading = false
        }
    }

    if (displayToast) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        displayToast = false
    }
}

@Composable
fun LabeledTaskView (
    modifier: Modifier,
    onViewMore: () -> Unit,
    label: String,
    isUser: Boolean = true,
    dataViewModel: MainViewModel
) {
    Column {
        Text (
            text = label,
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, start = 24.dp, bottom = 8.dp),
            color = MaterialTheme.colorScheme.primary
        )

        TaskView (
            modifier = modifier,
            onViewMore = onViewMore,
            label = label,
            isUser = isUser,
            dataViewModel = dataViewModel
        )
    }
}

@Composable
fun TaskViewItem (
    taskItem : Task
) {
    val color = MaterialTheme.colorScheme
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        Text (
            text = taskItem.title.uppercase(),
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            fontSize = 18.sp,
            color = color.error,
            fontWeight = FontWeight.Bold
        )

        Text (
            text = "by ${if (taskItem.username != localUsername.value) taskItem.username else "you"}",
            modifier = Modifier.padding(bottom = 16.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.Light
        )
    }
}
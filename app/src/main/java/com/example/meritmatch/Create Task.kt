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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

val draft = mutableStateOf(Task(id = 0, description = "", title = "", kp_value = 0.0, user_id = 0, post_time = "", deadline = "0 0", completed = false, active = false, reserved = 0, username = ""))
val modifyDraft = mutableStateOf(Task(id = 0, description = "", title = "", kp_value = 0.0, user_id = 0, post_time = "", deadline = "0 0", completed = false, active = false, reserved = 0, username = ""))

@Composable
fun CreateTask (
    goHome: () -> Unit,
    label : String,
    modify : Boolean = label == "Modify Task",
    task: Task,
    dataViewModel: MainViewModel
) {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current
    var title by remember { mutableStateOf(if (!modify) draft.value.title else modifyDraft.value.title) }
    var description by remember { mutableStateOf(if (!modify) draft.value.description else modifyDraft.value.description) }
    var kPOffering by remember { mutableStateOf(if (!modify) draft.value.kp_value.toString() else modifyDraft.value.kp_value.toString()) }
    var noOfDays by remember { mutableStateOf(if (!modify) draft.value.deadline.split(" ")[0] else modifyDraft.value.deadline.split(" ")[0]) }
    var noOfHours by remember { mutableStateOf(if (!modify) draft.value.deadline.split(" ")[1] else modifyDraft.value.deadline.split(" ")[1]) }
    var displayToast by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var displayLoad by remember { mutableStateOf(false) }
    var setValues by remember { mutableStateOf(false) }

    LaunchedEffect (displayLoading.value) {
        delay(5500)
        if (displayLoading.value && !dataViewModel.isApiConnected()) {
            message = "Unable to access the API."
            displayToast = true
            displayLoading.value = false
        }
    }
    BackHandler {
        draft.value = Task (
            title = title,
            description = description,
            user_id = user_id.value,
            deadline = "$noOfDays $noOfHours",
            kp_value = if (kPOffering.all { it.isDigit() || it == '.' }) kPOffering.toDouble() else 0.0,
            post_time = "",
            reserved = 0,
            completed = false,
            active = false,
            id = 0,
            username = localUsername.value
        )
        message = "Your draft has been saved."
        displayToast = true
        goHome()
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            Headline (
                text = label,
                modifier = Modifier.padding(top = 28.dp)
            )

            LazyColumn (
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    TextField (
                        modifier = Modifier
                            .padding(top = 20.dp, start = 40.dp, end = 40.dp)
                            .clip(RoundedCornerShape(15)),
                        value = title,
                        onValueChange = { newValue ->
                            if (newValue.length > 100) {
                                message = "Title too long."
                                displayToast = true
                            } else {
                                title = newValue
                            }
                        },
                        placeholder = { Text("Enter Title") },
                        label = { Text("Title") },
                        colors = TextFieldDefaults.colors(),
                        singleLine = true
                    )

                    Spacer (Modifier.height(24.dp))

                    TextField (
                        value = description,
                        onValueChange = {
                            if (description.length <= 5000) {
                                description = it
                            } else {
                                message = "Description too long."
                                displayToast = true
                            }
                        },
                        placeholder = { Text("Enter Description") },
                        label = { Text("Description") },
                        colors = TextFieldDefaults.colors(),
                        singleLine = false,
                        minLines = 4,
                        modifier = Modifier
                            .padding(start = 40.dp, end = 40.dp)
                            .clip(RoundedCornerShape(15))
                    )

                    Spacer (Modifier.height(12.dp))

                    HorizontalLine (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 40.dp),
                        start = 0f,
                        end = 1f,
                        width = 2f
                    )

                    Spacer (Modifier.height(12.dp))

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 40.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text ("Karma Points")

                        OutlinedTextField (
                            value = kPOffering,
                            onValueChange = { newValue ->
                                try {
                                    if (newValue.all { it.isDigit() || it == '.' } && newValue.toDouble() >= 0 && newValue.toDouble() <= karma_points.value) {
                                        kPOffering = newValue
                                    } else {
                                        if (newValue.toDouble() > karma_points.value) {
                                            message = "Karma points must not exceed your current balance."
                                        }
                                        displayToast = true
                                    }
                                } catch (e : Exception) {
                                    kPOffering = ""
                                    message = "Karma points must be a positive number less than your current karma points."
                                    displayToast = true
                                }
                            },
                            singleLine = false,
                            modifier = Modifier
                                .padding(start = 40.dp)
                                .width(80.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(50),
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Spacer (Modifier.height(12.dp))

                    HorizontalLine (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 40.dp),
                        start = 0f,
                        end = 1f,
                        width = 2f
                    )

                    Spacer (Modifier.height(12.dp))

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 40.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(0.dp))

                        Text (
                            text = "DAYS",
                            modifier = Modifier.padding(start = 28.dp, end = 8.dp)
                        )

                        Text (
                            text = "HOURS",
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }

                    Spacer (Modifier.height(12.dp))

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 40.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text ("Time")

                        OutlinedTextField (
                            value = noOfDays,
                            onValueChange = { newValue ->
                                try {
                                    if (newValue.all { it.isDigit() } && newValue.toInt() >= 0 && newValue.toInt() <= 30) {
                                        noOfDays = newValue
                                    } else {
                                        if (newValue.toInt() > 30) {
                                            message = "No. of days must not exceed 30."
                                        }
                                        displayToast = true
                                    }
                                } catch (e : Exception) {
                                    noOfDays = ""
                                    message = "No. of days must be a positive integer less than 30."
                                    displayToast = true
                                }
                            },
                            singleLine = false,
                            modifier = Modifier
                                .padding(start = 40.dp)
                                .width(80.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(50),
                            textStyle = LocalTextStyle.current.copy (
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField (
                            value = noOfHours,
                            onValueChange = { newValue ->
                                try {
                                    if (newValue.all { it.isDigit() } && newValue.toInt() >= 0 && newValue.toInt() < 24) {
                                        noOfHours = newValue
                                    } else {
                                        if (newValue.toInt() > 24) {
                                            message = "No. of hours must be less than 24."
                                        }
                                        displayToast = true
                                    }
                                } catch (e : Exception) {
                                    noOfHours = ""
                                    message = "No. of hours must be a positive integer less than 24."
                                    displayToast = true
                                }
                            },
                            singleLine = false,
                            modifier = Modifier
                                .padding(start = 40.dp)
                                .width(80.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(50),
                            textStyle = LocalTextStyle.current.copy (
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Spacer (modifier = Modifier.height(48.dp))

                    Button (
                        modifier = Modifier
                            .height(60.dp),
                        onClick = {
                            if (title == "") {
                                message = "Title is empty."
                                displayToast = true
                            } else if (description == "") {
                                message = "Description is empty"
                                displayToast = true
                            } else if (noOfDays == "" && noOfHours == "") {
                                message = "Give a deadline."
                                displayToast = true
                            } else if (kPOffering == "") {
                                message = "Offer Karma Points."
                                displayToast = true
                            } else {
                                if (noOfDays == "") {
                                    noOfDays = "0"
                                } else if (noOfHours == "") {
                                    noOfHours = "0"
                                }
                                if (!modify) {
                                    dataViewModel.postTasks(
                                        userId = user_id.value,
                                        post = Task(
                                            title = title,
                                            description = description,
                                            kp_value = kPOffering.toDouble(),
                                            deadline = "$noOfDays $noOfHours",
                                            post_time = "",
                                            reserved = 0,
                                            user_id = user_id.value,
                                            id = 0,
                                            username = localUsername.value
                                        )
                                    )
                                } else {
                                    dataViewModel.modifyPost(
                                        userId = user_id.value,
                                        task = Task (
                                            title = title,
                                            description = description,
                                            kp_value = kPOffering.toDouble(),
                                            deadline = "$noOfDays $noOfHours",
                                            post_time = "",
                                            reserved = 0,
                                            user_id = user_id.value,
                                            id = 0,
                                            username = localUsername.value
                                        ),
                                        taskId = task.id
                                    )
                                }
                                displayLoad = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.onSecondaryContainer
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 0.dp
                        ),
                        shape = RoundedCornerShape(25)
                    ) {
                        Text("Post Task")
                    }
                }
            }
        }
    }

    if (displayToast) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        displayToast = false
    }

    if (displayLoad) {
        LoadingPage()
    }

    if (dataViewModel.stateOfPost.value.status == 1 && displayLoad) {
        dataViewModel.getPostedTasks(user_id.value)
        displayLoad = false
        message = "Task created successfully."
        displayToast = true
        setValues = true
        dataViewModel.stateOfPost.value.status = 0
        refreshing2.value = true
        goHome()
    } else if (dataViewModel.stateOfPost.value.status == -1 && !modify) {
        message = "Failed to create task. Try again."
        displayToast = true
    }

    if (dataViewModel.stateOfModifyingPost.value.status == 1 && displayLoad) {
        dataViewModel.getPostedTasks(user_id.value)
        displayLoad = false
        message = "Task modified successfully."
        displayToast = true
        setValues = true
        dataViewModel.stateOfPost.value.status = 0
        refreshing2.value
        goHome()
    } else if (dataViewModel.stateOfModifyingPost.value.status == -1 && modify) {
        message = "Failed to modify task. Try again."
        displayToast = true
    }

    LaunchedEffect(setValues) {
        delay(1000)
        if (setValues) {
            setValues(dataViewModel)
            setValues = false
        }
    }
}
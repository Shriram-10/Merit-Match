package com.example.meritmatch

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.ceil
import kotlin.math.floor

val rating = mutableStateOf(0f)

@Composable
fun PostReviewPage (
    dataViewModel: MainViewModel,
    task : Task,
    goToHome: () -> Unit
) {
    var displayLoading by remember { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme
    var description by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var displayToast by remember { mutableStateOf(false) }
    val context = LocalContext.current

    BackHandler {
        rating.value = 0f
        reviewTask.value = Task(id = 0, description = "", title = "", kp_value = 0.0, user_id = 0, post_time = "", deadline = "0 0", completed = false, active = false, reserved = 0, username = "")
        showPostReview.value = false
    }
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Headline(text = "Post Review", modifier = Modifier.padding(top = 32.dp, start = 16.dp))
        }

        HorizontalLine (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
            start = 0.05f,
            end = 0.95f
        )

        StarRating(rating = rating.value) {
            rating.value = it
        }

        TextField (
            value = description,
            onValueChange = {
                if (description.isEmpty()) {
                    description = ""
                }
                if (description.length <= 500) {
                    description = it
                } else {
                    message = "Description too long."
                    displayToast = true
                }
            },
            placeholder = { Text("Enter Description (Optional)") },
            label = { Text("Description") },
            colors = TextFieldDefaults.colors(),
            singleLine = false,
            minLines = 4,
            modifier = Modifier
                .padding(start = 40.dp, end = 40.dp)
                .clip(RoundedCornerShape(15))
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button (
            modifier = Modifier
                .height(50.dp),
            onClick = {
                if (rating.value == 0f) {
                    message = "Please give a rating."
                    displayToast = true
                } else {
                    dataViewModel.postingReview (
                        Review (
                            description = description,
                            rating = rating.value.toInt(),
                            task_id = task.id,
                            poster_id = user_id.value,
                            subject_id = task.reserved,
                            post_time = ""
                        )
                    )
                    if (dataViewModel.stateOfPostingReview.value.status == 0) {
                        displayLoading = true
                    }
                }
            },
            colors = ButtonDefaults.buttonColors (
                containerColor = colors.onSecondaryContainer
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 0.dp
            ),
            shape = RoundedCornerShape(25)
        ) {
            Text("Post Review")
        }
    }

    if (displayToast) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        displayToast = false
    }

    if (displayLoading) {
        LoadingPage()
    }

    if (displayLoading && dataViewModel.stateOfPostingReview.value.status == 1) {
        message = "Review posted successfully."
        displayToast = true
        refreshing2.value = true
        goToHome()
        dataViewModel.stateOfPostingReview.value.status = 0
        displayLoading = false
    } else if (displayLoading && dataViewModel.stateOfPostingReview.value.status == -1) {
        message = "Couldn't post review. Try again."
        displayToast = true
        dataViewModel.stateOfPostingReview.value.status = 0
        displayLoading = false
    }

    LaunchedEffect(refreshing2.value || refreshing1.value) {
        if (refreshing2.value || refreshing1.value) {
            dataViewModel.getPostedTasks(user_id.value)
            dataViewModel.getReservedTasks(user_id.value)
            dataViewModel.getAvailableTasks(user_id.value)
            dataViewModel.getSubmittedTasks(user_id.value)
            dataViewModel.getWaitingTasks(user_id.value)
            dataViewModel.getHistoryTasks(user_id.value)
            delay(400)
            setValues.value = true
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
}

@Composable
fun StarRating (
    modifier: Modifier = Modifier,
    rating: Float,
    onRatingChanged: (Float) -> Unit
) {
    var size by remember { mutableStateOf(Size.Zero) }
    val colors = MaterialTheme.colorScheme

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(150.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val starWidth = size.width / 5
                        val newRating = ceil(offset.x / starWidth)
                        onRatingChanged(newRating)
                    }
                }
        ) {
            size = this.size
            val starWidth = size.width / 5

            for (i in 1..5) {
                val starCenter = Offset(x = i * starWidth - starWidth / 2, y = size.height / 2)

                drawStar (
                    center = starCenter,
                    size = starWidth * 0.8f,
                    color = if (i <= floor(rating)) colors.secondary else Color.Gray
                )
            }
        }
    }
}

private fun DrawScope.drawStar(center: Offset, size: Float, color: Color) {
    val outerRadius = size / 2
    val innerRadius = outerRadius * 0.4f
    val path = androidx.compose.ui.graphics.Path()

    for (i in 0..10) {
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val angle = i * 36f - 90
        val x = center.x + radius * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat()
        val y = center.y + radius * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()

    drawPath(path, color, style = Fill)
    drawPath(path, Color.Black, style = Stroke(width = 1f))
}
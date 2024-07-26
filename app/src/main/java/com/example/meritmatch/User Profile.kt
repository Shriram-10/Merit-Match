package com.example.meritmatch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserProfile (
    user: UserDetails,
    dataViewModel: MainViewModel,
    toUserHistory: () -> Unit,
) {
    val color = MaterialTheme.colorScheme

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        Headline(text = "User Profile", modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 16.dp))

        HorizontalLine(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp), start = 0.05f, end = 0.95f)

        LazyColumn {
            item {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text (
                        text = "USERNAME",
                        fontSize = 18.sp,
                        color = color.error,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = user.user.username,
                        fontSize = 18.sp,
                        color = color.outline,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text (
                        text = "DATE JOINED",
                        fontSize = 18.sp,
                        color = color.error,
                        fontWeight = FontWeight.Bold
                    )

                    Text (
                        text = user.user.date,
                        fontSize = 18.sp,
                        color = color.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text (
                        text = "AVERAGE RATING",
                        fontSize = 18.sp,
                        color = color.error,
                        fontWeight = FontWeight.Bold
                    )

                    Text (
                        text = if (user.user.avg_rating != 0.0) "%.2f".format(user.user.avg_rating) else "No reviews yet",
                        fontSize = 18.sp,
                        color = color.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }

                LabeledTaskView (
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .fillMaxWidth(0.95f)
                        .height(225.dp),
                    label = "Tasks History",
                    onViewMore = toUserHistory,
                    dataViewModel = dataViewModel,
                    isUser = false
                )

                LabelledReviewView (
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .fillMaxWidth(0.95f)
                        .height(225.dp),
                    label = "Reviews"
                )

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

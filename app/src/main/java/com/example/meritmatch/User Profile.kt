package com.example.meritmatch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserProfile (
    user: User,
    dataViewModel: MainViewModel,
    toUserHistory: () -> Unit,
    toReviewsUser: () -> Unit
) {
    val color = MaterialTheme.colorScheme

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        Headline(text = "User Profile", modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 16.dp))

        HorizontalLine(modifier = Modifier.fillMaxWidth(), start = 0.05f, end = 0.95f)

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text (
                text = "USERNAME",
                fontSize = 18.sp,
                color = color.primary
            )

            Text(
                text = user.username,
                fontSize = 18.sp,
                color = color.primary
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text (
                text = "DATE JOINED",
                fontSize = 18.sp,
                color = color.primary
            )

            Text (
                text = user.date,
                fontSize = 18.sp,
                color = color.primary
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text (
                text = "AVERAGE RATING",
                fontSize = 18.sp,
                color = color.primary
            )

            Text (
                text = if (user.avg_rating != 0.0) "%.2f".format(user.avg_rating) else "No reviews yet",
                fontSize = 18.sp,
                color = color.primary
            )
        }

        LabeledTaskView (
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(0.95f)
                .height(225.dp),
            label = "Tasks History",
            onViewMore = toUserHistory,
            dataViewModel = dataViewModel
        )

        LabelledReviewView (
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(0.95f)
                .height(225.dp),
            label = "Reviews",
            dataViewModel = dataViewModel,
            onViewMore = toReviewsUser
        )
    }
}

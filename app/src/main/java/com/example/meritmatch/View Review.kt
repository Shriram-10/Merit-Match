package com.example.meritmatch

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ReviewView (
    modifier: Modifier
) {
    val color = MaterialTheme.colorScheme
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
                        queryUser.value.reviews.size
                    ) { item ->
                        Box (
                            modifier = Modifier
                                .clip(RoundedCornerShape(20))
                                .background(color.primaryContainer),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            if (queryUser.value.reviews.isNotEmpty()) {
                                ReviewViewItem(review = queryUser.value.reviews[item])
                            } else {
                                Text (
                                    text = "This user has no reviews yet.",
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

            Box (
                modifier = modifier
                    .clip(RoundedCornerShape(10))
                    .fillMaxSize()
                    .background(brush = Brush.verticalGradient(fadeOutColors))
            ) {

            }
        }
    }

    LaunchedEffect (displayLoading.value) {
        if (displayLoading.value) {
            delay(200)
            displayLoading.value = false
        }
    }

    if (displayToast) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        displayToast = false
    }
}

@Composable
fun LabelledReviewView (
    modifier: Modifier,
    label: String,
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

        ReviewView (
            modifier = modifier
        )
    }
}

@Composable
fun ReviewViewItem (
    review: Review
) {
    val color = MaterialTheme.colorScheme
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        Text (
            text = review.description,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            fontSize = 18.sp,
            color = color.error,
            fontWeight = FontWeight.Bold
        )

        Text (
            text = "by ${if (review.poster_name != localUsername.value) review.poster_name else "you"}",
            modifier = Modifier.padding(bottom = 16.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.Light
        )
    }
}
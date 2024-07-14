package com.example.meritmatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun TaskListPage (
    modifier: Modifier
) {
    val color = MaterialTheme.colorScheme
    Column (
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Headline(text = "Available Tasks")

        LazyColumn {
            items (10) { item ->

                Row (
                    modifier = Modifier
                        .fillMaxSize(0.95f)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20))
                        .background(color.primaryContainer)
                ){
                    Text(item.toString())
                }

            }
        }
    }
}
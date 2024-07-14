package com.example.meritmatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
    modifier: Modifier,
    label: String = "Available Tasks"
) {
    val color = MaterialTheme.colorScheme
    Column (
        modifier = modifier.fillMaxSize()
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
            contentAlignment = Alignment.Center
        ){
            LazyColumn {
                items (10) { item ->

                    Row (
                        modifier = Modifier
                            .fillMaxSize(0.95f)
                            .height(100.dp)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(20))
                            .background(color.primaryContainer),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(item.toString())
                    }

                }
            }
        }
    }
}
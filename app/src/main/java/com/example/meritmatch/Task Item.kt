package com.example.meritmatch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskItem (
    i : Int,
    taskList : MutableState<List<Task>>
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = taskList.value[i].title,
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 10.dp),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = taskList.value[i].description,
            fontSize = 16.sp,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Normal
        )
    }
}
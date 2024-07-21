package com.example.meritmatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun LoadingPage () {
    val colors = MaterialTheme.colorScheme

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(colors.surface.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator (
            modifier = Modifier
                .size(80.dp),
            color = colors.primary,
            trackColor = colors.primaryContainer,
            strokeWidth = 12.dp,
            strokeCap = StrokeCap.Round
        )
    }
}
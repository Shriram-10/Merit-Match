package com.example.meritmatch

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalLine(
    modifier: Modifier,
    start : Float,
    end : Float,
    width : Float = 4f
){
    val color = MaterialTheme.colorScheme
    Canvas(
        modifier = modifier
    ) {
        drawLine(
            color = color.primaryContainer,
            start = Offset(size.width * start, size.height / 2),
            end = Offset(size.width * end, size.height / 2),
            strokeWidth = width
        )
    }
}
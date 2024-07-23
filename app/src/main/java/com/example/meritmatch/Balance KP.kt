package com.example.meritmatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BalanceKP (
    modifier: Modifier,
    balance: Double
) {
    val color = MaterialTheme.colorScheme
    Row (
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(
                color = color.secondaryContainer,
                shape = RoundedCornerShape(20)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text (
            text = "Available Balance",
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text (
            text = "%.2f".format(balance),
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
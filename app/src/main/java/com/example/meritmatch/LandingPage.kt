package com.example.meritmatch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay

@Composable
fun LandingPage(modifier : Modifier, onTimeOut:() -> Unit){

    val currentTimeOut by rememberUpdatedState(newValue = onTimeOut)

    LaunchedEffect(Unit) {
        delay(2000)
        currentTimeOut()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.yin_yang),
            contentDescription = "Logo"
        )
    }
}
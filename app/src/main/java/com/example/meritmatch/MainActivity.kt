package com.example.meritmatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import com.example.meritmatch.ui.theme.MeritMatchTheme

enum class SplashState {
    Shown,
    Completed
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeritMatchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier : Modifier) {

    val transitionState = remember {
        MutableTransitionState(SplashState.Shown)
    }

    val transition = updateTransition(
        transitionState = transitionState,
        label = "splashTransition"
    )

    val splashAlpha by transition.animateFloat(
        label = "splashAlpha",
        transitionSpec = { tween(durationMillis = 100) }
    ) { state ->
        if (state == SplashState.Shown) 1f else 0f
    }

    val contentAlpha by transition.animateFloat(
        label = "contentAlpha",
        transitionSpec = { tween(durationMillis = 100) }
    ) { state ->
        if (state == SplashState.Shown) 0f else 1f
    }

    Box {
        LandingPage(modifier = modifier.alpha(splashAlpha)){
            transitionState.targetState = SplashState.Completed
        }

        ContentScreen(modifier = modifier.alpha(contentAlpha))
    }
}

@Composable
fun ContentScreen(modifier : Modifier) {
    Column(modifier.fillMaxSize()){
        Text(text = "Hello World")
    }
}
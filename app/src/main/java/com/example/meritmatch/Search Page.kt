package com.example.meritmatch

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun SearchPage (
    modifier: Modifier,
    navController: NavController
) {
    Scaffold { innerPadding ->
        Box(
            modifier = modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            contentAlignment = Alignment.Center
        ){
            Column {
                Headline(
                    text = "Search",
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                )

                HorizontalLine(
                    modifier = Modifier
                        .fillMaxWidth().
                        height(20.dp),
                    start = 0.05f,
                    end = 0.95f
                )
            }
        }
    }
}
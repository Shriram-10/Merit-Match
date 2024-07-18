package com.example.meritmatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar (
   title : String,
   navigate : () -> Unit,
   startIcon: ImageVector,
   endIcon: ImageVector
) {
   val colors = MaterialTheme.colorScheme
   val fadeOutPrimary = listOf (
      colors.primary.copy(alpha = 0f),
      colors.primary.copy(alpha = 0.4f),
      colors.primary.copy(alpha = 0.7f)
   )

   TopAppBar(
      title = { Text(title) },
      modifier = Modifier
         .background(brush = Brush.verticalGradient(fadeOutPrimary))
         .shadow(elevation = 8.dp, shape = RectangleShape),
      actions = {
         Row {
            IconButton(
               onClick = navigate,
               modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            ) {
               Icon(
                  imageVector = startIcon,
                  contentDescription = "Localized description"
               )
            }

            IconButton(
               onClick = navigate,
               modifier = Modifier.padding(end = 8.dp)
            ) {
               Icon(
                  imageVector = endIcon,
                  contentDescription = "Localized description"
               )
            }
         }
      }
   )
}
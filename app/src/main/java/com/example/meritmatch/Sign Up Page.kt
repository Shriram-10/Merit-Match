package com.example.meritmatch

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignUpPage(
    modifier: Modifier,
    onSignUp: () -> Unit,
    goToLogin: () -> Unit
) {
    val color = MaterialTheme.colorScheme
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Create Account",
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                textAlign = TextAlign.Center
            )

            InputField(
                modifier = Modifier.padding(24.dp),
                label = "Create Username",
                placeholder = "Enter your username"
            )

            InputField(
                modifier = Modifier.padding(bottom = 0.dp),
                label = "Password",
                placeholder = "Create your password",
                password = true
            )

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = { onSignUp() },
                modifier = Modifier
                    .height(42.dp)
                    .width(120.dp),
                shape = RoundedCornerShape(50),
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Box(
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(60.dp)
                ) {
                    drawLine(
                        color = color.primary,
                        start = Offset(size.width * 0.1f, size.height / 2),
                        end = Offset(size.width * 0.9f, size.height / 2),
                        strokeWidth = 3f
                    )
                }

                Box (
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = color.primaryContainer,
                            contentColor = color.primary
                        ),
                        shape = RoundedCornerShape(100)
                    ) {

                    }

                    Text(
                        text = "OR",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Text(
                text = "Already have an account?",
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Login",
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable{
                        goToLogin()
                    },
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
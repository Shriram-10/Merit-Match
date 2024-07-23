package com.example.meritmatch

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun LoginPage (
    modifier: Modifier,
    onLogin: @Composable () -> Unit,
    goToSignUp: () -> Unit,
    dataViewModel: MainViewModel,
){
    val color = MaterialTheme.colorScheme
    val displayLoading = remember {
        mutableStateOf(false)
    }
    var message by remember {
        mutableStateOf("")
    }
    var displayToast by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    Box (
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Login",
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
                label = "Username",
                placeholder = "Enter your username",
                dataViewModel = dataViewModel
            )

            Column {
                InputField(
                    modifier = Modifier.padding(bottom = 0.dp),
                    label = "Password",
                    placeholder = "Enter your password",
                    password = true,
                    dataViewModel = dataViewModel
                )

                Text(
                    text = "Forgot Password?",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    dataViewModel.loginUser (
                        user.value.username,
                        user.value.password,
                        karma_points.value
                    )
                    if (user_id.value == 0) {
                        displayLoading.value = true
                    }
                },
                modifier = Modifier
                    .height(42.dp)
                    .width(96.dp),
                shape = RoundedCornerShape(50),
            ) {
                Text(
                    text = "Login",
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

                Box(
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
                text = "Don't have an account?",
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Sign Up",
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable {
                        goToSignUp()
                    },
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    if (user_id.value == 0 && displayLoading.value && dataViewModel.stateOfLogin.value.status.code == 0) {
        LoadingPage()
    } else if (user_id.value != 0) {
        onLogin()
        displayLoading.value = false
    } else if (dataViewModel.stateOfLogin.value.status.code == -1) {
        message = "Bad Credentials."
        displayToast = true
    }

    if (displayToast) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
package com.example.meritmatch

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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

val karma_points = mutableStateOf(350.00)
val user = mutableStateOf(User("", "", false, karma_points.value, "", "", 0.0))
val referralCode = mutableStateOf("#")

@Composable
fun SignUpPage (
    modifier: Modifier,
    onSignUp: () -> Unit,
    goToLogin: () -> Unit,
    dataViewModel: MainViewModel
) {
    val color = MaterialTheme.colorScheme
    val context = LocalContext.current
    var displayToast by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    BackHandler {
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    Box (
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text (
                text = "Create Account",
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                textAlign = TextAlign.Center
            )

            InputField (
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 8.dp),
                label = "Create Username",
                placeholder = "Enter your username",
                dataViewModel = dataViewModel
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp, bottom = 8.dp),
            ) {
                Text (
                    text = "Username ${dataViewModel.stateOfCheckUsername.value.status}",
                    fontSize = 14.sp,
                    color = color.secondary.copy(alpha = if (user.value.username == "") 0f else 1f)
                )

                if (dataViewModel.stateOfCheckUsername.value.loading) {
                    CircularProgressIndicator (
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .size(10.dp),
                        color = color.primary,
                        trackColor = color.primaryContainer,
                        strokeWidth = 2.dp,
                    )
                }
            }


            InputField (
                modifier = Modifier.padding(bottom = 0.dp),
                label = "Password",
                placeholder = "Create your password",
                dataViewModel = dataViewModel,
                password = true
            )

            InputField (
                modifier = Modifier.padding(bottom = 0.dp),
                label = "Referral Code",
                placeholder = "Enter referral code if you got one.",
                dataViewModel = dataViewModel,
                referral = true
            )

            Spacer (modifier = Modifier.height(50.dp))

            Button (
                onClick = {
                    dataViewModel.createNewUser(username = user.value.username, password = user.value.password, login = user.value.login, referralCode = referralCode.value)
                    displayLoading.value = true
                    user_id.value = 0
                },
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

            Box (
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
                    .clickable {
                        goToLogin()
                    },
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    LaunchedEffect (displayLoading.value) {
        delay(5500)
        if (displayLoading.value && !dataViewModel.isApiConnected()) {
            message = "Unable to access the API."
            displayToast = true
            displayLoading.value = false
        }
    }

    if (displayLoading.value && dataViewModel.stateOfUserRetrieval.value.value == 1) {
        message = "Signed up successfully."
        displayToast = true
        displayLoading.value = false
        dataViewModel.stateOfUserRetrieval.value.value = 0
        onSignUp()
    } else if (displayLoading.value && dataViewModel.stateOfUserRetrieval.value.value == -1) {
        message = "Failed to sign up. Try again."
        displayToast = true
        displayLoading.value = false
        dataViewModel.stateOfUserRetrieval.value.value = 0
    }

    if (displayToast) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
package com.example.meritmatch

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun SettingsPage (
    modifier: Modifier,
    navController: NavController,
    dataViewModel : MainViewModel,
    onLogout: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var displayToast by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold { innerPadding ->
        Box(
            modifier = modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            contentAlignment = Alignment.Center
        ){
            Column {
                Headline (
                    text = "Settings",
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                )

                HorizontalLine(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    start = 0.05f,
                    end = 0.95f
                )

                Row (
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { onLogout() },
                        shape = RoundedCornerShape(20),
                        modifier = Modifier
                            .padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.surfaceContainerHighest
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 12.dp,
                            pressedElevation = 0.dp
                        )
                    ) {
                        Text (
                            text = "LOGOUT",
                            color = colors.error,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row (
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(15))
                        .background(colors.surfaceContainerHighest),
                ) {
                    Column {
                        Text(
                            text = "Referral Code",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(20.dp, top = 20.dp, bottom = 0.dp),
                            color = colors.onErrorContainer
                        )

                        Text (
                            text = "Get more Karma Points by sharing your referral code with your friends!\n",
                            modifier = Modifier.padding(20.dp),
                            color = colors.onSecondaryContainer
                        )

                        Box (
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column (
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text (
                                    text = "YOUR CODE IS : ",
                                    modifier = Modifier.padding(start = 40.dp, bottom = 8.dp),
                                    color = colors.error,
                                    fontWeight = FontWeight.Bold
                                )

                                Row {
                                    Text (
                                        text = "#",
                                        modifier = Modifier.padding(bottom = 20.dp, start = 40.dp),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp
                                    )

                                    Text (
                                        text = referralCode.value,
                                        modifier = Modifier.padding(start = 2.dp),
                                        color = colors.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (displayLoading.value) {
        LoadingPage()
    }
    if (displayLoading.value && dataViewModel.stateOfLogout.value.status == 1) {
        message = "Logged out successfully."
        displayToast = true
        dataViewModel.stateOfLogout.value.status = 0
        displayLoading.value = false
        navController.navigate(Screen.SignUp.route)
        localUsername.value = ""
        user.value.username = ""
        user.value.password = ""
        user.value.karma_points = 0.0
        karma_points.value = 0.0
        user_id.value = 0
        referralCode.value = ""
    } else if (displayLoading.value && dataViewModel.stateOfLogout.value.status == -1) {
        message = "Failed to logout. Please try again."
        displayToast = true
        displayLoading.value = false
        dataViewModel.stateOfLogout.value.status = 0
    }

    if (displayToast) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
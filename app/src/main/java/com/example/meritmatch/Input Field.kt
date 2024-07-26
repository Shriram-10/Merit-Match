package com.example.meritmatch

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
@Composable
fun InputField (
    modifier: Modifier,
    label: String = "",
    placeholder: String = "",
    dataViewModel : MainViewModel,
    password: Boolean = false,
    referral: Boolean = false
){
    var message by remember {
        mutableStateOf("")
    }
    var displayToast by remember {
        mutableStateOf(false)
    }
    LaunchedEffect (displayLoading.value) {
        if (displayLoading.value && !dataViewModel.isApiConnected()) {
            message = "Unable to access the API."
            displayToast = true
            displayLoading.value = false
        }
    }

    val color = MaterialTheme.colorScheme

    var valueStr by remember {
        mutableStateOf("")
    }

    var passwordInvisibility by remember {
        mutableStateOf (if (password) true else false)
    }

    OutlinedTextField (
        modifier = modifier,
        value = valueStr,
        onValueChange = {
            valueStr = it
            if (password) {
                user.value.password = valueStr
            } else if (referral) {
                referralCode.value = valueStr
            } else {
                user.value.username = valueStr
            }
        },
        label = {
            Text(text = label)
        },
        singleLine = true,
        placeholder = {
            Text(text = placeholder)
        },
        visualTransformation = if (passwordInvisibility) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = color.primary,
            unfocusedTextColor = color.secondary,
            focusedBorderColor = color.primaryContainer,
            unfocusedBorderColor = color.secondaryContainer,
            focusedLabelColor = color.primary,
            unfocusedLabelColor = color.secondary,
            cursorColor = color.primary,
            unfocusedContainerColor = color.secondaryContainer,
            focusedContainerColor = color.primaryContainer
        ),
        keyboardOptions = if (password) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions(),
        trailingIcon = {
            val image = if (passwordInvisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            val description = if (passwordInvisibility) "Hide password" else "Show password"

            if (password) {
                IconButton(
                    onClick = { passwordInvisibility = !passwordInvisibility }
                ) {
                    Icon(
                        imageVector = image,
                        contentDescription = description,
                        tint = color.primary,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    )

    if (!password && !referral) {
        LaunchedEffect(valueStr) {
            dataViewModel.checkUser(username = valueStr)
        }
    }
}
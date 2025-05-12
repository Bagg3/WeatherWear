package com.mad.weatherwear.shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mad.weatherwear.ui.theme.Accent
import com.mad.weatherwear.ui.theme.BgPrimary
import com.mad.weatherwear.ui.theme.BgTertiary
import com.mad.weatherwear.ui.theme.TextPrimary

@Composable
fun AuthScreenLayout(
    modifier: Modifier = Modifier,
    titleText: String,
    subTitleText: String,
    emailInput: String,
    onEmailInputChange: (String) -> Unit,
    passwordInput: String,
    onPasswordInputChange: (String) -> Unit,
    buttonText: String = "Sign In",
    onButtonClick: () -> Unit,
    navigationText: String = "Don't have an account? Sign Up",
    onNavigationClick: () -> Unit,
    formErrorMessage: String? = null,
    authError: String? = null,
    clearError: () -> Unit
) {
    var displayedErrorMessage by remember { mutableStateOf(formErrorMessage) }

    LaunchedEffect(authError) {
        if (authError != null) {
            displayedErrorMessage = authError
            clearError()
        }
    }

    LaunchedEffect(formErrorMessage) {
        if (formErrorMessage != null) {
            displayedErrorMessage = formErrorMessage
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgTertiary)
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.33f)
                .background(BgPrimary)
        ) {
            Text(
                text = titleText,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                fontSize = 60.sp,
            )
            Spacer(modifier = modifier.height(24.dp))
            Text(
                text = subTitleText,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
            Spacer(modifier = modifier.height(24.dp))
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.66f)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    singleLine = true,
                    value = emailInput,
                    onValueChange = onEmailInputChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = modifier
                        .weight(0.7f)
                        .padding(8.dp),
                    shape = CircleShape,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.LightGray,
                        unfocusedIndicatorColor = Color.White,
                    ),
                    placeholder = { Text("Email") },
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    singleLine = true,
                    value = passwordInput,
                    onValueChange = onPasswordInputChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .weight(0.7f)
                        .padding(8.dp),
                    shape = CircleShape,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    placeholder = { Text("Password") },
                )
            }
            displayedErrorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red, // Bright red color for the error message
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            Button(onClick = onButtonClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Accent,
                    contentColor = Color.Black
                )) { Text(buttonText) }

            TextButton(onClick = onNavigationClick) {
                Text(navigationText, color = Color.Black)
            }
        }
    }
}

@Preview
@Composable
fun PreviewScreenLayout() {
    AuthScreenLayout(
        titleText = "Login",
        subTitleText = "Ready to look\n" + "stylish?",
        emailInput = "",
        onEmailInputChange = {},
        passwordInput = "",
        onPasswordInputChange = {},
        buttonText = "Login",
        onButtonClick = {},
        navigationText = "Don’t have an account?",
        onNavigationClick = {},
        formErrorMessage = null,
        authError = null,
        clearError = {}
    )
}
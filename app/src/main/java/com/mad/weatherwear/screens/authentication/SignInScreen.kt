package com.mad.weatherwear.screens.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SignInScreen(
    authError: String?,
    currentUser: User?,
    clearError: () -> Unit,
    signIn: (Email, Password) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onSignInSuccess: () -> Unit
) {
    var emailInput by remember { mutableStateOf("testmail@uni.au.dk") }
    var passwordInput by remember { mutableStateOf("Test1234") }
    var formErrorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onSignInSuccess()
        }
    }

    LaunchedEffect(authError) {
        if (authError != null) {
            formErrorMessage = authError
            clearError() // Clear error after displaying
        }
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sign In")
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Email:", modifier = Modifier.weight(0.3f))
            TextField(
                value = emailInput,
                onValueChange = { emailInput = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.weight(0.7f)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Password:", modifier = Modifier.weight(0.3f))
            TextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.weight(0.7f)
            )
        }

        formErrorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Button(onClick = {
            formErrorMessage = null // Clear previous form error
            if (!Email.validate(emailInput)) {
                formErrorMessage = "Invalid email format"
            } else if (!Password.validate(passwordInput)) {
                formErrorMessage = "Password must be at least 8 characters, including letters and numbers."
            } else {
                signIn(Email(emailInput), Password(passwordInput))
            }
        }) { Text("Sign In") }

        TextButton(onClick = onNavigateToSignUp) {
            Text("Don't have an account? Sign Up")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    SignInScreen(
        authError = null,
        currentUser = null,
        clearError = {},
        signIn = { _, _ -> },
        onNavigateToSignUp = {},
        onSignInSuccess = {}
    )
}
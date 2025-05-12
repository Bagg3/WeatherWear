package com.mad.weatherwear.screens.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mad.weatherwear.shared.ui.AuthScreenLayout

@Composable
fun SignInScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onSignInSuccess: () -> Unit
) {
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var formErrorMessage by remember { mutableStateOf<String?>(null) }

    val authError by authViewModel.authError.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onSignInSuccess()
        }
    }

    // Validate form before submission
    fun validateForm(): Boolean {
        return when {
            !Email.validate(emailInput) -> {
                formErrorMessage = "Invalid email format"
                false
            }

            !Password.validate(passwordInput) -> {
                formErrorMessage =
                    "Password must be at least 8 characters, including letters and numbers."
                false
            }

            else -> true
        }
    }

    AuthScreenLayout(
        titleText = "Sign In",
        subTitleText = "Welcome back!",
        emailInput = emailInput,
        onEmailInputChange = { emailInput = it },
        passwordInput = passwordInput,
        onPasswordInputChange = { passwordInput = it },
        buttonText = "Sign In",
        onButtonClick = {
            formErrorMessage = null // Clear previous form error
            if (validateForm()) {
                authViewModel.signIn(Email(emailInput), Password(passwordInput))
            }
        },
        navigationText = "Don't have an account? Sign Up",
        onNavigationClick = onNavigateToSignUp,
        formErrorMessage = formErrorMessage,
        authError = authError,
        clearError = { authViewModel.clearError() }
    )
}
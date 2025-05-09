package com.mad.weatherwear.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.weatherwear.authentication.AuthResult
import com.mad.weatherwear.authentication.Email
import com.mad.weatherwear.authentication.Password
import com.mad.weatherwear.authentication.User
import com.mad.weatherwear.service.authentication.AuthenticationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authenticationService = AuthenticationService()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError

    fun signIn(email: Email, password: Password) {
        viewModelScope.launch {
            val result = authenticationService.signIn(email, password)
            handleAuthResult(result)
        }
    }

    fun signUp(email: Email, password: Password) {
        viewModelScope.launch {
            val result = authenticationService.signUp(email, password)
            handleAuthResult(result)
        }
    }

    private fun handleAuthResult(result: AuthResult) {
        if (result.isOk()) {
            _currentUser.value = result.user
            _authError.value = null
        } else {
            _currentUser.value = null
            _authError.value = "Authentication failed. Please try again." // Or use a more specific error from result if available
        }
    }

    fun signOut() {
        authenticationService.signOut()
        _currentUser.value = null
        _authError.value = null
    }

    fun clearError() {
        _authError.value = null
    }
}
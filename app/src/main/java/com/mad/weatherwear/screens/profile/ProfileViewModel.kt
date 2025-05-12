package com.mad.weatherwear.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.weatherwear.shared.preference.FireStorePreferenceService
import com.mad.weatherwear.shared.preference.Preference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val preferenceService = FireStorePreferenceService()

    private val _preference = MutableStateFlow<Preference?>(null)
    val preference: StateFlow<Preference?> = _preference

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadPreference(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userPreference = preferenceService.getPreference(userId)
                _preference.value = userPreference
            } catch (e: Exception) {
                // Create default preference if not found
                _preference.value = Preference(userId, 2) // Medium sensitivity default
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateColdSensitivity(userId: String, sensitivity: Int) {
        if (sensitivity < 1 || sensitivity > 3) return

        _preference.value = _preference.value?.copy(coldSensitivity = sensitivity)

        viewModelScope.launch {
            try {
                preferenceService.updatePreference(userId, sensitivity)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
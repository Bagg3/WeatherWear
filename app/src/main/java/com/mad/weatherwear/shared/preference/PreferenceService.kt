package com.mad.weatherwear.shared.preference

interface PreferenceService {
    suspend fun getPreference(userId: String): Preference
    suspend fun updatePreference(userId: String, coldSensitivity: Int)
}
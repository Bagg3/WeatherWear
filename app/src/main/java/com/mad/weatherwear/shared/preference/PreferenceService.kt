package com.mad.weatherwear.shared.preference

interface PreferenceService {
    suspend fun getPreference(userId: String): Preference
}
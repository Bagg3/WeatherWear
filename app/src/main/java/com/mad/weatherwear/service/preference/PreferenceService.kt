package com.mad.weatherwear.service.preference

import com.mad.weatherwear.model.preference.domain.Preference

interface PreferenceService {
    suspend fun getPreference(userId: String): Preference
}
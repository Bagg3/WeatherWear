package com.mad.weatherwear.model.preference.firestore

import com.google.firebase.firestore.DocumentId

data class PreferenceFS(
    @DocumentId var userId: String?= "",
    val coldSensitivity: Int = 0
)
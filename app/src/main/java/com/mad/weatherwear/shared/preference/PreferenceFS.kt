package com.mad.weatherwear.shared.preference

import com.google.firebase.firestore.DocumentId

data class PreferenceFS(
    @DocumentId var userId: String?= "",
    val coldSensitivity: Int = 0
)
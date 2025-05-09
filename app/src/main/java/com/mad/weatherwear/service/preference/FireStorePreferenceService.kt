package com.mad.weatherwear.service.preference

import com.google.firebase.firestore.FirebaseFirestore
import com.mad.weatherwear.model.preference.domain.Preference
import com.mad.weatherwear.model.preference.firestore.PreferenceFS
import kotlinx.coroutines.tasks.await

class FireStorePreferenceService : PreferenceService {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        const val PREFERENCE_COLLECTION = "Preference"
    }

    override suspend fun getPreference(userId: String): Preference {
        val preference = db.collection(PREFERENCE_COLLECTION)
            .document(userId)
            .get()
            .await()
            .toObject(PreferenceFS::class.java)
            ?: throw Exception("Preference not found for userId: $userId")

        return Preference(
            UserId = preference.userId ?: "",
            coldSensitivity = preference.coldSensitivity
        )
    }
}
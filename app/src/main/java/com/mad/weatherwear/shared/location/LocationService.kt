package com.mad.weatherwear.shared.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationService(
    private val context: androidx.activity.ComponentActivity,
    private val client: FusedLocationProviderClient,
) {

    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun getCurrentLocation(): Location {
        return suspendCoroutine { continuation ->
            try {
                client.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener {
                        continuation.resume(it)
                    }.addOnFailureListener {
                        Log.v(this::class.qualifiedName, "The Location request failed")
                        it.printStackTrace()
                    }
            } catch (e: SecurityException) {
                Log.v(this::class.qualifiedName, "The Location request failed")
                e.printStackTrace()
                throw e
            }
        }
    }

    fun checkPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}
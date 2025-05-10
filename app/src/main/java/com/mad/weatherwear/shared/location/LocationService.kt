package com.mad.weatherwear.shared.location

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LocationService(private val context: Application) {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    suspend fun getCurrentLocation(): Location {
        if (!checkPermission()) {
            throw SecurityException("Location permission not granted")
        }

        return suspendCoroutine { continuation ->
            try {
                val cancellationToken = CancellationTokenSource().token
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationToken
                )
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            continuation.resume(location)
                        } else {
                            continuation.resumeWithException(
                                Exception("Unable to get location")
                            )
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("LocationService", "Location request failed: ${exception.message}")
                        continuation.resumeWithException(exception)
                    }
            } catch (e: SecurityException) {
                Log.e("LocationService", "Security exception: ${e.message}")
                continuation.resumeWithException(e)
            }
        }
    }

    fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
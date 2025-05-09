package com.mad.weatherwear.service.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationService(
    private val context: androidx.activity.ComponentActivity,
    private val client: FusedLocationProviderClient,
) {
    private var locationOn: Boolean = false
    private var launcher: ActivityResultLauncher<String> = context.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        locationOn = it
        Log.v("CALLBACK", "GRANTED=$it")
    }

    suspend fun getCurrentLocation(): Location {

        return suspendCoroutine { continuation ->
            try {

                client.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener {
                        continuation.resume(it)
                    }.addOnFailureListener {
                        Log.v(this::class.qualifiedName, "The Loccation requast failed")
                    }
            } catch (e: SecurityException) {
                Log.v(this::class.qualifiedName, "The Loccation requast failed")
                e.printStackTrace()
            }

        }
    }

    fun checkPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun requestPermission() {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
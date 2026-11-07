package com.aadipanchang.map.control

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.aadipanchang.map.math.LatLong
import com.aadipanchang.map.util.MiscUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FusedLocationProvider @Inject constructor(
    private val client: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) : LocationProvider {

    private var callback: LocationCallback? = null
    private var cancellationTokenSource: CancellationTokenSource? = null

    @SuppressLint("MissingPermission")
    override fun startUpdates(minDistanceMetres: Float, onUpdate: (LatLong, Float?) -> Unit) {
        stopUpdates()

        // 1. Check last known location immediately for instant fix
        try {
            client.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    Log.d(TAG, "Got last known location: lat=${loc.latitude}, lon=${loc.longitude}")
                    onUpdate(
                        LatLong(loc.latitude, loc.longitude),
                        if (loc.hasAccuracy()) loc.accuracy else null
                    )
                }
            }.addOnFailureListener { e ->
                Log.w(TAG, "Failed to get last known location", e)
            }
        } catch (e: SecurityException) {
            Log.w(TAG, "SecurityException getting last known location", e)
        }

        // 2. Actively fetch current fresh location without waiting for displacement
        val tokenSource = CancellationTokenSource()
        cancellationTokenSource = tokenSource
        try {
            client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, tokenSource.token)
                .addOnSuccessListener { loc ->
                    if (loc != null) {
                        Log.d(TAG, "Got current location: lat=${loc.latitude}, lon=${loc.longitude}")
                        onUpdate(
                            LatLong(loc.latitude, loc.longitude),
                            if (loc.hasAccuracy()) loc.accuracy else null
                        )
                    }
                }.addOnFailureListener { e ->
                    Log.w(TAG, "Failed to get current location", e)
                }
        } catch (e: SecurityException) {
            Log.w(TAG, "SecurityException getting current location", e)
        }

        // 3. Register for ongoing updates
        val request = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 10_000L)
            .setMinUpdateIntervalMillis(5_000L)
            .setMinUpdateDistanceMeters(minDistanceMetres)
            .build()
        val cb = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation ?: return
                Log.d(TAG, "Got location from callback: lat=${loc.latitude}, lon=${loc.longitude}")
                onUpdate(
                    LatLong(loc.latitude, loc.longitude),
                    if (loc.hasAccuracy()) loc.accuracy else null
                )
            }
        }
        callback = cb
        try {
            client.requestLocationUpdates(request, cb, Looper.getMainLooper())
        } catch (e: SecurityException) {
            Log.w(TAG, "SecurityException requesting location updates", e)
        }
    }

    override fun stopUpdates() {
        cancellationTokenSource?.cancel()
        cancellationTokenSource = null
        callback?.let {
            try {
                client.removeLocationUpdates(it)
            } catch (e: Exception) {
                Log.w(TAG, "Error removing location updates", e)
            }
        }
        callback = null
    }

    override fun isAvailable(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        return locationManager?.let { LocationManagerCompat.isLocationEnabled(it) } ?: false
    }

    companion object {
        private val TAG = MiscUtil.getTag(FusedLocationProvider::class.java)
    }
}

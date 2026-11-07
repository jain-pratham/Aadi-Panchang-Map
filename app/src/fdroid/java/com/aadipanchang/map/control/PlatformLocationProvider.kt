package com.aadipanchang.map.control

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.location.LocationManagerCompat
import com.aadipanchang.map.math.LatLong
import com.aadipanchang.map.util.MiscUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PlatformLocationProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationProvider {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var activeListeners = mutableListOf<LocationListener>()
    private var updateCallback: ((LatLong, Float?) -> Unit)? = null

    @SuppressLint("MissingPermission")
    override fun startUpdates(minDistanceMetres: Float, onUpdate: (LatLong, Float?) -> Unit) {
        stopUpdates()
        updateCallback = onUpdate

        // 1. Immediately check last known location from available providers
        var bestLastLocation: Location? = null
        for (provider in listOf(LocationManager.PASSIVE_PROVIDER, LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER)) {
            try {
                if (locationManager.isProviderEnabled(provider)) {
                    val last = locationManager.getLastKnownLocation(provider)
                    if (last != null) {
                        if (bestLastLocation == null || last.time > bestLastLocation.time) {
                            bestLastLocation = last
                        }
                    }
                }
            } catch (e: SecurityException) {
                Log.w(TAG, "SecurityException getting last known location for $provider: ${e.message}")
            } catch (e: Exception) {
                Log.w(TAG, "Error getting last known location for $provider: ${e.message}")
            }
        }
        bestLastLocation?.let { loc ->
            Log.d(TAG, "Got platform last known location: lat=${loc.latitude}, lon=${loc.longitude}")
            val accuracy = if (loc.hasAccuracy()) loc.accuracy else null
            onUpdate(LatLong(loc.latitude, loc.longitude), accuracy)
        }

        // 2. Register listeners for providers safely
        for (provider in listOf(LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER)) {
            try {
                if (!locationManager.isProviderEnabled(provider)) continue
                val listener = createListener(onUpdate)
                locationManager.requestLocationUpdates(provider, 5000L, minDistanceMetres, listener)
                activeListeners.add(listener)
            } catch (e: SecurityException) {
                Log.w(TAG, "SecurityException registering updates for $provider: ${e.message}")
            } catch (e: IllegalArgumentException) {
                Log.w(TAG, "Provider $provider not supported on this device")
            } catch (e: Exception) {
                Log.w(TAG, "Error registering updates for $provider: ${e.message}")
            }
        }
    }

    private fun createListener(onUpdate: (LatLong, Float?) -> Unit): LocationListener {
        return object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val accuracy = if (location.hasAccuracy()) location.accuracy else null
                onUpdate(LatLong(location.latitude, location.longitude), accuracy)
            }

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }
    }

    override fun stopUpdates() {
        activeListeners.forEach {
            try {
                locationManager.removeUpdates(it)
            } catch (e: Exception) {
                Log.w(TAG, "Error removing listener: ${e.message}")
            }
        }
        activeListeners.clear()
        updateCallback = null
    }

    override fun isAvailable(): Boolean {
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    companion object {
        private val TAG = MiscUtil.getTag(PlatformLocationProvider::class.java)
    }
}

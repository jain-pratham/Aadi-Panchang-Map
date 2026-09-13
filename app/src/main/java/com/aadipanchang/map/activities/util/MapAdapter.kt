package com.aadipanchang.map.activities.util

import android.os.Bundle
import android.view.View
import com.aadipanchang.map.math.LatLong

/**
 * Interface for map functionality, implemented differently for GMS and fdroid flavors.
 */
interface MapAdapter {
    fun initialize(mapView: View, savedInstanceState: Bundle?)
    fun onResume()
    fun onPause()
    fun onDestroy()
    fun onSaveInstanceState(outState: Bundle)
    fun updateLocation(location: LatLong)
}

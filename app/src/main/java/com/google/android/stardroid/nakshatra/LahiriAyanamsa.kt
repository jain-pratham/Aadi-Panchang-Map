package com.google.android.stardroid.nakshatra

import com.google.android.stardroid.math.julianDay
import java.util.Date

/**
 * Lahiri (Chitrapaksha) ayanamsa — standard for Indian national ephemeris and
 * Vedic sidereal longitude. Linear model from J2000.0.
 */
object LahiriAyanamsa {

    /** Ayanamsa at J2000.0 (degrees): 23°51′07.5″ ≈ 23.852083°. */
    private const val AYANAMSA_J2000_DEGREES = 23.852083f

    /** Precession rate in degrees per Julian century. */
    private const val RATE_DEGREES_PER_CENTURY = 1.396042f

    /**
     * Lahiri ayanamsa in degrees at the given instant (tropical − sidereal).
     */
    fun degrees(date: Date): Float {
        val t = ((julianDay(date) - 2451545.0) / 36525.0).toFloat()
        return AYANAMSA_J2000_DEGREES + RATE_DEGREES_PER_CENTURY * t
    }

    /**
     * Converts tropical ecliptic longitude to sidereal (Lahiri) longitude in [0, 360).
     */
    fun tropicalToSiderealLongitude(tropicalLongitude: Float, date: Date): Float =
        normalizeDegrees(tropicalLongitude - degrees(date))
}

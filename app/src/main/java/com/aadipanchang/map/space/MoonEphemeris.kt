package com.aadipanchang.map.space

import com.aadipanchang.map.math.DEGREES_TO_RADIANS
import com.aadipanchang.map.math.MathUtils.cos
import com.aadipanchang.map.math.MathUtils.sin
import com.aadipanchang.map.math.julianDay
import java.util.Date

/**
 * Geocentric ecliptic position of the Moon from the 2008 Astronomical Almanac
 * approximation (page D22). Shared by [Moon] position/phase logic and Indian
 * astronomy features such as Nakshatra calculation.
 */
object MoonEphemeris {

    /** Ecliptic longitude and latitude in degrees (tropical). */
    data class EclipticPosition(
        val longitude: Float,
        val latitude: Float,
    )

    /**
     * Tropical geocentric ecliptic longitude of the Moon in degrees [0, 360).
     */
    fun getTropicalEclipticLongitude(date: Date): Float =
        computeEclipticPosition(date).longitude

    /**
     * Tropical geocentric ecliptic latitude of the Moon in degrees.
     */
    fun getTropicalEclipticLatitude(date: Date): Float =
        computeEclipticPosition(date).latitude

    /**
     * Computes λ (ecliptic longitude) and β (ecliptic latitude) using the same
     * series as [Moon.getRaDec].
     */
    fun computeEclipticPosition(date: Date): EclipticPosition {
        val t = ((julianDay(date) - 2451545.0) / 36525.0).toFloat()
        val lambda = (218.32f + 481267.881f * t + (6.29f
                * sin((135.0f + 477198.87f * t) * DEGREES_TO_RADIANS)) - 1.27f
                * sin((259.3f - 413335.36f * t) * DEGREES_TO_RADIANS)) + (0.66f
                * sin((235.7f + 890534.22f * t) * DEGREES_TO_RADIANS)) + (0.21f
                * sin((269.9f + 954397.74f * t) * DEGREES_TO_RADIANS)) - (0.19f
                * sin((357.5f + 35999.05f * t) * DEGREES_TO_RADIANS)) - (0.11f
                * sin((186.5f + 966404.03f * t) * DEGREES_TO_RADIANS))
        val beta = (5.13f * sin((93.3f + 483202.02f * t) * DEGREES_TO_RADIANS) + 0.28f
                * sin((228.2f + 960400.89f * t) * DEGREES_TO_RADIANS)) - (0.28f
                * sin((318.3f + 6003.15f * t) * DEGREES_TO_RADIANS)) - (0.17f
                * sin((217.6f - 407332.21f * t) * DEGREES_TO_RADIANS))
        return EclipticPosition(lambda, beta)
    }
}

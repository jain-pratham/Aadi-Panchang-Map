package com.aadipanchang.map.nakshatra

/**
 * Result of [NakshatraCalculator.getCurrentNakshatra].
 */
data class CurrentNakshatra(
    val name: String,
    val index: Int,
    val startLongitude: Float,
    val endLongitude: Float,
    /** Moon sidereal ecliptic longitude (Lahiri) used for the lookup. */
    val moonSiderealLongitude: Float,
    val nakshatra: Nakshatra = NakshatraCatalog.get(index),
)

package com.google.android.stardroid.nakshatra

/**
 * One of the 28 Vedic lunar mansions on the sidereal ecliptic.
 *
 * @param name Display name (Roman transliteration).
 * @param startLongitude Inclusive start of the mansion in sidereal ecliptic longitude (degrees).
 * @param endLongitude Exclusive end in degrees, except [Revati] where this is 360.
 */
data class Nakshatra(
    val name: String,
    val startLongitude: Float,
    val endLongitude: Float,
) {
    val index: Int
        get() = NakshatraCatalog.indexOf(this)

    /** Span along the ecliptic in degrees (12°51′ for standard 28-fold division). */
    val arcDegrees: Float
        get() = endLongitude - startLongitude

    fun containsSiderealLongitude(longitude: Float): Boolean {
        val lon = normalizeDegrees(longitude)
        return if (endLongitude >= 360f) {
            lon >= startLongitude && lon < 360f
        } else {
            lon >= startLongitude && lon < endLongitude
        }
    }
}

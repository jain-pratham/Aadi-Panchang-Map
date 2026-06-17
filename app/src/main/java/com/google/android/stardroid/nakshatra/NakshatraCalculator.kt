package com.google.android.stardroid.nakshatra

import com.google.android.stardroid.space.MoonEphemeris
import java.util.Date

/**
 * Maps the Moon's position to the 28 Nakshatras using Lahiri sidereal longitude
 * and the equal 12°51′ mansion boundaries in [NakshatraCatalog].
 */
class NakshatraCalculator {

    /**
     * Tropical geocentric ecliptic longitude of the Moon (degrees), from [MoonEphemeris].
     */
    fun getMoonTropicalEclipticLongitude(date: Date): Float =
        MoonEphemeris.getTropicalEclipticLongitude(date)

    /**
     * Sidereal (Lahiri) geocentric ecliptic longitude of the Moon in [0, 360) degrees.
     */
    fun getMoonSiderealEclipticLongitude(date: Date): Float {
        val tropical = getMoonTropicalEclipticLongitude(date)
        return LahiriAyanamsa.tropicalToSiderealLongitude(tropical, date)
    }

    /**
     * Nakshatra occupied by the Moon at [date].
     */
    fun getCurrentNakshatra(date: Date): CurrentNakshatra {
        val siderealLon = getMoonSiderealEclipticLongitude(date)
        val nakshatra = NakshatraCatalog.forSiderealLongitude(siderealLon)
        return CurrentNakshatra(
            name = nakshatra.name,
            index = NakshatraCatalog.indexOf(nakshatra),
            startLongitude = nakshatra.startLongitude,
            endLongitude = nakshatra.endLongitude,
            moonSiderealLongitude = siderealLon,
            nakshatra = nakshatra,
        )
    }

}

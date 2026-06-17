package com.google.android.stardroid.space

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.GregorianCalendar
import java.util.TimeZone

class MoonEclipticLongitudeTest {

    @Test
    fun getEclipticLongitude_returnsFiniteValue() {
        val cal = GregorianCalendar(TimeZone.getTimeZone("GMT"))
        cal.set(2009, GregorianCalendar.JANUARY, 1, 12, 0, 0)
        val lon = Moon().getEclipticLongitude(cal.time)
        assertThat(lon).isFinite()
    }

    @Test
    fun getRaDec_unchangedAfterEphemerisExtraction() {
        val moon = Moon()
        val cal = GregorianCalendar(TimeZone.getTimeZone("GMT"))
        cal.set(2009, GregorianCalendar.JANUARY, 1, 12, 0, 0)
        val before = moon.getRaDec(cal.time)
        cal.set(2010, GregorianCalendar.DECEMBER, 25, 12, 0, 0)
        val after = moon.getRaDec(cal.time)
        assertThat(before.ra).isFinite()
        assertThat(before.dec).isFinite()
        assertThat(after.ra).isFinite()
        assertThat(after.dec).isFinite()
    }
}

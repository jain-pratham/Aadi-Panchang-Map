package com.google.android.stardroid.nakshatra

import com.google.android.stardroid.space.Moon
import com.google.android.stardroid.space.MoonEphemeris
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.GregorianCalendar
import java.util.TimeZone

class NakshatraCalculatorTest {

    private val calculator = NakshatraCalculator()

    @Test
    fun moonEclipticLongitude_matchesMoonEphemeris() {
        val cal = GregorianCalendar(TimeZone.getTimeZone("GMT"))
        cal.set(2024, GregorianCalendar.JUNE, 15, 12, 0, 0)
        val date = cal.time
        assertThat(Moon().getEclipticLongitude(date))
            .isWithin(1e-5f)
            .of(MoonEphemeris.getTropicalEclipticLongitude(date))
    }

    @Test
    fun getCurrentNakshatra_returnsExpectedFields() {
        val cal = GregorianCalendar(TimeZone.getTimeZone("GMT"))
        cal.set(2009, GregorianCalendar.JANUARY, 1, 12, 0, 0)
        val result = calculator.getCurrentNakshatra(cal.time)

        assertThat(result.index).isAtLeast(0)
        assertThat(result.index).isLessThan(28)
        assertThat(result.name).isEqualTo(NakshatraCatalog.get(result.index).name)
        assertThat(result.startLongitude).isEqualTo(NakshatraCatalog.get(result.index).startLongitude)
        assertThat(result.endLongitude).isEqualTo(NakshatraCatalog.get(result.index).endLongitude)
        assertThat(result.moonSiderealLongitude)
            .isAtLeast(result.startLongitude)
        if (result.endLongitude < 360f) {
            assertThat(result.moonSiderealLongitude).isLessThan(result.endLongitude)
        } else {
            assertThat(result.moonSiderealLongitude).isLessThan(360f)
        }
    }

    @Test
    fun getCurrentNakshatra_exampleDate_jan2009() {
        val cal = GregorianCalendar(TimeZone.getTimeZone("GMT"))
        cal.set(2009, GregorianCalendar.JANUARY, 1, 12, 0, 0)
        val result = calculator.getCurrentNakshatra(cal.time)
        // 2009-01-01 12:00 UTC — engine report reference example
        assertThat(result.name).isEqualTo("Shatabhisha")
        assertThat(result.index).isEqualTo(23)
        assertThat(result.startLongitude).isWithin(1e-3f).of(306.66666f)
        assertThat(result.endLongitude).isWithin(1e-3f).of(320f)
        assertThat(result.moonSiderealLongitude).isWithin(0.5f).of(311.96f)
    }

    @Test
    fun siderealLongitude_isTropicalMinusAyanamsa() {
        val cal = GregorianCalendar(TimeZone.getTimeZone("GMT"))
        cal.set(2020, GregorianCalendar.MARCH, 21, 0, 0, 0)
        val date = cal.time
        val tropical = calculator.getMoonTropicalEclipticLongitude(date)
        val sidereal = calculator.getMoonSiderealEclipticLongitude(date)
        val expected = normalizeDegrees(tropical - LahiriAyanamsa.degrees(date))
        assertThat(sidereal).isWithin(1e-4f).of(expected)
    }
}

package com.aadipanchang.map.nakshatra

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NakshatraCatalogTest {

    @Test
    fun catalog_has28Nakshatras() {
        assertThat(NakshatraCatalog.count).isEqualTo(28)
        assertThat(NakshatraCatalog.all).hasSize(28)
    }

    @Test
    fun eachNakshatraSpan_is12Degrees51Minutes() {
        val expectedArc = 360f / 28f
        for (nakshatra in NakshatraCatalog.all) {
            assertThat(nakshatra.arcDegrees).isWithin(1e-4f).of(expectedArc)
        }
    }

    @Test
    fun boundaries_coverFullCircle() {
        assertThat(NakshatraCatalog.all.first().startLongitude).isWithin(1e-4f).of(0f)
        assertThat(NakshatraCatalog.all.first().name).isEqualTo("Ashwini")
        assertThat(NakshatraCatalog.all.last().name).isEqualTo("Abhijit")
        assertThat(NakshatraCatalog.all.last().endLongitude).isWithin(1e-4f).of(360f)
        for (i in 0 until NakshatraCatalog.count - 1) {
            assertThat(NakshatraCatalog.all[i].endLongitude)
                .isWithin(1e-4f)
                .of(NakshatraCatalog.all[i + 1].startLongitude)
        }
    }

    @Test
    fun forSiderealLongitude_ashwiniAtZero() {
        val n = NakshatraCatalog.forSiderealLongitude(0f)
        assertThat(n.name).isEqualTo("Ashwini")
        assertThat(NakshatraCatalog.indexOf(n)).isEqualTo(0)
    }

    @Test
    fun forSiderealLongitude_rohiniInSecondQuadrant() {
        // Rohini index 3: 40° to 53.333°
        val n = NakshatraCatalog.forSiderealLongitude(45f)
        assertThat(n.name).isEqualTo("Rohini")
        assertThat(n.index).isEqualTo(3)
    }

    @Test
    fun forSiderealLongitude_wrapsNear360() {
        val n = NakshatraCatalog.forSiderealLongitude(359f)
        assertThat(n.name).isEqualTo("Abhijit")
    }
}

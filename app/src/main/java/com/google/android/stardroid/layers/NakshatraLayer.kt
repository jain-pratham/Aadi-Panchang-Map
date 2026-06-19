// Copyright 2025 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.google.android.stardroid.layers

import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import com.google.android.stardroid.R
import com.google.android.stardroid.math.DEGREES_TO_RADIANS
import com.google.android.stardroid.math.MathUtils.asin
import com.google.android.stardroid.math.MathUtils.atan2
import com.google.android.stardroid.math.MathUtils.cos
import com.google.android.stardroid.math.MathUtils.sin
import com.google.android.stardroid.math.RADIANS_TO_DEGREES
import kotlin.math.tan
import com.google.android.stardroid.math.getGeocentricCoords
import com.google.android.stardroid.nakshatra.LahiriAyanamsa
import com.google.android.stardroid.nakshatra.NakshatraCatalog
import com.google.android.stardroid.nakshatra.normalizeDegrees
import com.google.android.stardroid.renderables.AbstractAstronomicalRenderable
import com.google.android.stardroid.renderables.AstronomicalRenderable
import com.google.android.stardroid.renderables.LinePrimitive
import com.google.android.stardroid.renderables.TextPrimitive
import java.util.*

/**
 * Creates a Layer for displaying the 28 Nakshatra labels on the celestial sphere.
 *
 * @author Stardroid Team
 */
class NakshatraLayer(resources: Resources, preferences: SharedPreferences) : AbstractRenderablesLayer
      (resources, false, preferences) {
    private val showCircles: Boolean = preferences.getBoolean("show_nakshatra_circles", true)
    private val showGrid: Boolean = preferences.getBoolean("show_nakshatra_grid", true)
    private var isHindi: Boolean = preferences.getBoolean("nakshatra_language_hindi", false)

    override val layerDepthOrder = 45
    override val layerNameId = R.string.show_nakshatra_pref
    override val preferenceId = "source_provider.nakshatra"
    
    // Store Nakshatra coordinates for search
    private val nakshatraCoordinates = HashMap<String, Pair<Float, Float>>()
    
    override fun initializeAstroSources(sources: ArrayList<AstronomicalRenderable>) {
        sources.add(NakshatraRenderable(resources, showCircles, showGrid, nakshatraCoordinates, isHindi))
    }
    
    override fun getObjectNamesMatchingPrefix(prefix: String): Set<String> {
        val results = HashSet<String>()
        val lowerPrefix = prefix.lowercase()
        for (name in NakshatraCatalog.NAMES) {
            if (name.lowercase().startsWith(lowerPrefix)) {
                results.add(name)
            }
        }
        return results
    }
    
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        super.onSharedPreferenceChanged(sharedPreferences, key)
        if (key == "nakshatra_language_hindi" && sharedPreferences != null) {
            isHindi = sharedPreferences.getBoolean("nakshatra_language_hindi", false)
            initialize()
        }
    }
    
    override fun searchByObjectName(name: String): List<com.google.android.stardroid.search.SearchResult> {
        val results = ArrayList<com.google.android.stardroid.search.SearchResult>()
        val lowerName = name.lowercase()
        
        for (nakshatraName in NakshatraCatalog.NAMES) {
            if (nakshatraName.lowercase() == lowerName) {
                val coordinates = nakshatraCoordinates[nakshatraName]
                if (coordinates != null) {
                    val (ra, dec) = coordinates
                    val geocentricCoords = getGeocentricCoords(ra, dec)
                    
                    // Create a simple renderable for search
                    val renderable = object : com.google.android.stardroid.renderables.AstronomicalRenderable {
                        override val names = listOf(nakshatraName)
                        override val searchLocation = geocentricCoords
                        override var isVisible = true
                        override fun initialize(): com.google.android.stardroid.renderables.Renderable = this
                        override fun update(): EnumSet<com.google.android.stardroid.renderer.RendererObjectManager.UpdateType> = EnumSet.noneOf(com.google.android.stardroid.renderer.RendererObjectManager.UpdateType::class.java)
                        override val images = emptyList<com.google.android.stardroid.renderables.ImagePrimitive>()
                        override val labels = emptyList<com.google.android.stardroid.renderables.TextPrimitive>()
                        override val lines = emptyList<com.google.android.stardroid.renderables.LinePrimitive>()
                        override val points = emptyList<com.google.android.stardroid.renderables.PointPrimitive>()
                    }
                    
                    results.add(com.google.android.stardroid.search.SearchResult(nakshatraName, renderable))
                }
            }
        }
        return results
    }

    /** Implementation of [AstronomicalRenderable] for Nakshatra labels. */
    private class NakshatraRenderable(resources: Resources, showCircles: Boolean, showGrid: Boolean, private val nakshatraCoordinates: HashMap<String, Pair<Float, Float>>, isHindi: Boolean) : AbstractAstronomicalRenderable() {
        override val labels: MutableList<TextPrimitive> = ArrayList()
        override val lines: MutableList<LinePrimitive> = ArrayList()

        companion object {
            private const val OBLIQUITY = 23.439281f
            private val LABEL_COLOR = Color.argb(180, 255, 200, 100)
            private val RING_COLOR = Color.WHITE
            private const val RING_RADIUS_DEGREES = 2.0f  // Ring radius in degrees (2x-3x larger than previous marker)
            private const val RING_SEGMENTS = 32  // Number of segments to approximate a circle
            private val GRID_COLOR = Color.argb(100, 100, 200, 255)  // Semi-transparent blue
            private const val GRID_LINE_WIDTH = 1.0f
        }

        init {
            val date = Date()
            val ayanamsa = LahiriAyanamsa.degrees(date)

            // Direct RA/DEC overrides for visual alignment (bypassing coordinate conversion)
            // Upper Aries star group (near Hamal): RA 31.8°, DEC 23.46°
            val (bharaniTropicalLon, bharaniTropicalLat) = raDecToEcliptic(31.8f, 23.46f)
            val bharaniSiderealLon = normalizeDegrees(bharaniTropicalLon - ayanamsa)
            
            // Midpoint between Sheratan (Beta Arietis: RA 28.65°, DEC 20.81°) and Mesarthim (Gamma Arietis: RA 28.35°, DEC 19.29°)
            // Direct RA/DEC override for Ashwini (bypassing all coordinate conversions)



            val ashwiniDirectRa = 28.5f
            val ashwiniDirectDec = 20.05f

            val bharaniDirectRa = 34.8f
            val bharaniDirectDec = 23.46f

            val krittikaDirectRa = 48.8500f
            val krittikaDirectDec = 22.1100f

            val rohiniDirectRa = 72.0f
            val rohiniDirectDec = 16.51f

            val mrigashiraDirectRa = 83.850006f
            val mrigashiraDirectDec = 9.93f

            val ardraDirectRa = 84.8000f
            val ardraDirectDec = 4.4100f

            val punarvasuDirectRa = 115.05f
            val punarvasuDirectDec = 29.96f

            val pushyaDirectRa = 127.95f
            val pushyaDirectDec = 18.09f

            val ashleshaDirectRa = 126.3000f
            val ashleshaDirectDec = 7.4500f

            val maghaDirectRa = 145.6000f
            val maghaDirectDec = 10.4700f

            val purvaPhalguniDirectRa = 154.5267f
            val purvaPhalguniDirectDec = 24.0240f

            val uttaraPhalguniDirectRa = 168.8000f
            val uttaraPhalguniDirectDec = 16.5700f

            val hastaDirectRa = 174.1117f
            val hastaDirectDec = -10.0184f

            val chitraDirectRa = 201.3f
            val chitraDirectDec = -11.16f

            val swatiDirectRa = 219.9000f
            val swatiDirectDec = 14.19000f

            val vishakhaDirectRa = 233.6719f
            val vishakhaDirectDec = -14.9971f

            val anuradhaDirectRa = 245.0834f
            val anuradhaDirectDec = -23.1216f

            val jyeshthaDirectRa = 249.3500f
            val jyeshthaDirectDec = -27.4300f

            val moolaDirectRa = 264.4000f
            val moolaDirectDec = -43.1000f

            val purvashadhaDirectRa = 274.8000f
            val purvashadhaDirectDec = -29.3000f

            val uttarashadhaDirectRa = 286.8000f
            val uttarashadhaDirectDec = -5.3000f

            val shravanaDirectRa = 310.4529f
            val shravanaDirectDec = 15.4266f

            val dhanishtaDirectRa = 318.2500f
            val dhanishtaDirectDec = 29.8700f

            val shatabhishaDirectRa = 331.2f
            val shatabhishaDirectDec = -1.79f

            val purvaBhadrapadaDirectRa = 337.7000f
            val purvaBhadrapadaDirectDec = 0.2100f

            val uttaraBhadrapadaDirectRa = -0.2000f
            val uttaraBhadrapadaDirectDec = 6.6800f

            val revatiDirectRa = 18.4325f
            val revatiDirectDec = 7.5755f

            val abhijitDirectDec = 279.0000f
            val abhijitDirectRa = 37.0000f



            // Manual position overrides for visual alignment with star clusters
            // Store both longitude and latitude for Bharani only
            val manualOverridesLon = mapOf(
                "Bharani" to bharaniSiderealLon
            )
            val manualOverridesLat = mapOf(
                "Bharani" to bharaniTropicalLat
            )
            // Direct RA/DEC overrides for 28 Nakshatras (Bharani uses ecliptic coordinates via manualOverridesLon/Lat)
            val manualRaDec = mapOf(
                "Ashwini" to RaDec(ashwiniDirectRa, ashwiniDirectDec),
                "Bharani" to RaDec(bharaniDirectRa, bharaniDirectDec),
                "Krittika" to RaDec(krittikaDirectRa, krittikaDirectDec),
                "Rohini" to RaDec(rohiniDirectRa, rohiniDirectDec),
                "Mrigashira" to RaDec(mrigashiraDirectRa, mrigashiraDirectDec),
                "Ardra" to RaDec(ardraDirectRa, ardraDirectDec),
                "Punarvasu" to RaDec(punarvasuDirectRa, punarvasuDirectDec),
                "Pushya" to RaDec(pushyaDirectRa, pushyaDirectDec),
                "Ashlesha" to RaDec(ashleshaDirectRa, ashleshaDirectDec),
                "Magha" to RaDec(maghaDirectRa, maghaDirectDec),
                "Purva Phalguni" to RaDec(purvaPhalguniDirectRa, purvaPhalguniDirectDec),
                "Uttara Phalguni" to RaDec(uttaraPhalguniDirectRa, uttaraPhalguniDirectDec),
                "Hasta" to RaDec(hastaDirectRa, hastaDirectDec),
                "Chitra" to RaDec(chitraDirectRa, chitraDirectDec),
                "Swati" to RaDec(swatiDirectRa, swatiDirectDec),
                "Vishakha" to RaDec(vishakhaDirectRa, vishakhaDirectDec),
                "Anuradha" to RaDec(anuradhaDirectRa, anuradhaDirectDec),
                "Jyeshtha" to RaDec(jyeshthaDirectRa, jyeshthaDirectDec),
                "Moola" to RaDec(moolaDirectRa, moolaDirectDec),
                "Purvashadha" to RaDec(purvashadhaDirectRa, purvashadhaDirectDec),
                "Uttarashadha" to RaDec(uttarashadhaDirectRa, uttarashadhaDirectDec),
                "Shravana" to RaDec(shravanaDirectRa, shravanaDirectDec),
                "Dhanishta" to RaDec(dhanishtaDirectRa, dhanishtaDirectDec),
                "Shatabhisha" to RaDec(shatabhishaDirectRa, shatabhishaDirectDec),
                "Purva Bhadrapada" to RaDec(purvaBhadrapadaDirectRa, purvaBhadrapadaDirectDec),
                "Uttara Bhadrapada" to RaDec(uttaraBhadrapadaDirectRa, uttaraBhadrapadaDirectDec),
                "Revati" to RaDec(revatiDirectRa, revatiDirectDec),
                "Abhijit" to RaDec(abhijitDirectRa, abhijitDirectDec)
            )

            for ((index, nakshatra) in NakshatraCatalog.all.withIndex()) {
                val displayName = if (isHindi) NakshatraCatalog.HINDI_NAMES[index] else nakshatra.name
                
                // Check if this Nakshatra has a direct RA/DEC override
                val directRaDec = manualRaDec[nakshatra.name]
                
                if (directRaDec != null) {
                    // Use direct RA/DEC without any conversion
                    val raDec = directRaDec
                    
                    // Store coordinates for search
                    nakshatraCoordinates[nakshatra.name] = Pair(raDec.ra, raDec.dec)
                    
                    // Create label
                    labels.add(TextPrimitive(raDec.ra, raDec.dec, displayName, LABEL_COLOR))
                    
                    // Create ring if enabled
                    if (showCircles) {
                        val ringVertices = createRingVertices(raDec.ra, raDec.dec)
                        lines.add(LinePrimitive(RING_COLOR, ringVertices, 1.5f))
                    }
                } else {
                    // Use manual override if available, otherwise calculate center
                    val centerLon = manualOverridesLon[nakshatra.name] 
                        ?: (nakshatra.startLongitude + nakshatra.endLongitude) / 2f
                    
                    // Convert sidereal to tropical ecliptic longitude
                    val tropicalLon = normalizeDegrees(centerLon + ayanamsa)
                    
                    // Use manual latitude override if available, otherwise use 0 (ecliptic)
                    val tropicalLat = manualOverridesLat[nakshatra.name] ?: 0f
                    
                    // Convert ecliptic to RA/DEC using actual latitude
                    val raDec = eclipticToRaDec(tropicalLon, tropicalLat)
                    
                    // Store coordinates for search
                    nakshatraCoordinates[nakshatra.name] = Pair(raDec.ra, raDec.dec)
                    
                    // Create label
                    labels.add(TextPrimitive(raDec.ra, raDec.dec, displayName, LABEL_COLOR))
                    
                    // Create ring if enabled
                    if (showCircles) {
                        val ringVertices = createRingVertices(raDec.ra, raDec.dec)
                        lines.add(LinePrimitive(RING_COLOR, ringVertices, 1.5f))
                    }
                }
            }
            
            // Create grid lines if enabled
            if (showGrid) {
                createGridLines(ayanamsa)
            }
        }

        /**
         * Converts ecliptic longitude and latitude to RA/DEC.
         * Uses the same formula as Moon.kt for ecliptic to equatorial conversion.
         */
        private fun eclipticToRaDec(lambda: Float, beta: Float): RaDec {
            val lambdaRad = lambda * DEGREES_TO_RADIANS
            val betaRad = beta * DEGREES_TO_RADIANS
            val obliquityRad = OBLIQUITY * DEGREES_TO_RADIANS

            val cosObliquity = cos(obliquityRad)
            val sinObliquity = sin(obliquityRad)

            val l = cos(betaRad) * cos(lambdaRad)
            val m = cosObliquity * cos(betaRad) * sin(lambdaRad) - sinObliquity * sin(betaRad)
            val n = sinObliquity * cos(betaRad) * sin(lambdaRad) + cosObliquity * sin(betaRad)

            val ra = normalizeDegrees(atan2(m, l) * RADIANS_TO_DEGREES)
            val dec = asin(n) * RADIANS_TO_DEGREES

            return RaDec(ra, dec)
        }

        /**
         * Converts RA/DEC to ecliptic longitude and latitude (inverse of eclipticToRaDec).
         * Returns a pair of (longitude, latitude).
         */
        private fun raDecToEcliptic(ra: Float, dec: Float): Pair<Float, Float> {
            val raRad = ra * DEGREES_TO_RADIANS
            val decRad = dec * DEGREES_TO_RADIANS
            val obliquityRad = OBLIQUITY * DEGREES_TO_RADIANS

            val cosObliquity = cos(obliquityRad)
            val sinObliquity = sin(obliquityRad)

            // Inverse transformation: from RA/DEC to ecliptic coordinates
            val sinEclipticLon = (sin(raRad) * cos(decRad) * cosObliquity + tan(decRad) * sinObliquity) / cos(decRad)
            val cosEclipticLon = cos(raRad)
            val eclipticLonRad = atan2(sinEclipticLon, cosEclipticLon)
            val eclipticLon = normalizeDegrees(eclipticLonRad * RADIANS_TO_DEGREES)

            // Calculate ecliptic latitude (beta)
            val sinEclipticLat = sin(decRad) * cosObliquity - cos(decRad) * sin(raRad) * sinObliquity
            val eclipticLat = asin(sinEclipticLat) * RADIANS_TO_DEGREES

            return Pair(eclipticLon, eclipticLat)
        }

        /**
         * Creates vertices for a ring (circle) around the given RA/DEC position.
         * The ring is drawn on the celestial sphere at the specified radius.
         */
        private fun createRingVertices(ra: Float, dec: Float): ArrayList<com.google.android.stardroid.math.Vector3> {
            val vertices = ArrayList<com.google.android.stardroid.math.Vector3>()
            val raRad = ra * DEGREES_TO_RADIANS
            val decRad = dec * DEGREES_TO_RADIANS
            val radiusRad = RING_RADIUS_DEGREES * DEGREES_TO_RADIANS

            for (i in 0..RING_SEGMENTS) {
                val angle = (2 * Math.PI * i / RING_SEGMENTS).toFloat()
                
                // Calculate offset in RA and DEC
                val offsetRa = radiusRad * cos(angle) / cos(decRad)
                val offsetDec = radiusRad * sin(angle)
                
                // Calculate new RA/DEC for this vertex
                val vertexRa = normalizeDegrees((raRad + offsetRa) * RADIANS_TO_DEGREES)
                val vertexDec = (decRad + offsetDec) * RADIANS_TO_DEGREES
                
                // Convert to geocentric coordinates
                vertices.add(getGeocentricCoords(vertexRa, vertexDec))
            }
            
            return vertices
        }

        /**
         * Creates grid lines based on Nakshatra boundaries.
         * Draws vertical sector lines at each Nakshatra boundary.
         */
        private fun createGridLines(ayanamsa: Float) {
            // Vertical boundary lines REMOVED - they were cluttering the view
            // Draw vertical lines at each Nakshatra boundary
            /*
            for (nakshatra in NakshatraCatalog.all) {
                // Convert sidereal boundary to tropical
                val startTropical = normalizeDegrees(nakshatra.startLongitude + ayanamsa)
                val endTropical = normalizeDegrees(nakshatra.endLongitude + ayanamsa)
                
                // Create vertical line at start boundary
                val startLineVertices = createVerticalLine(startTropical)
                lines.add(LinePrimitive(GRID_COLOR, startLineVertices, GRID_LINE_WIDTH))
                
                // Create vertical line at end boundary (skip for last Nakshatra to avoid duplicate)
                if (nakshatra != NakshatraCatalog.all.last()) {
                    val endLineVertices = createVerticalLine(endTropical)
                    lines.add(LinePrimitive(GRID_COLOR, endLineVertices, GRID_LINE_WIDTH))
                }
            }
            */
            
            // Draw horizontal connector lines (ecliptic path)
            val eclipticVertices = createEclipticPath(ayanamsa)
            lines.add(LinePrimitive(GRID_COLOR, eclipticVertices, GRID_LINE_WIDTH))
        }

        /**
         * Creates a vertical line at a given ecliptic longitude.
         * The line spans from -30° to +30° declination around the ecliptic.
         */
        private fun createVerticalLine(eclipticLon: Float): ArrayList<com.google.android.stardroid.math.Vector3> {
            val vertices = ArrayList<com.google.android.stardroid.math.Vector3>()
            val latitudes = listOf(-30f, -15f, 0f, 15f, 30f)  // Multiple points for smoother line
            
            for (lat in latitudes) {
                val raDec = eclipticToRaDec(eclipticLon, lat)
                vertices.add(getGeocentricCoords(raDec.ra, raDec.dec))
            }
            
            return vertices
        }

        /**
         * Creates the ecliptic path line.
         * This is the main horizontal line that follows the Nakshatra path.
         */
        private fun createEclipticPath(ayanamsa: Float): ArrayList<com.google.android.stardroid.math.Vector3> {
            val vertices = ArrayList<com.google.android.stardroid.math.Vector3>()
            
            // Draw the ecliptic from 0° to 360° in steps
            for (lon in 0..360 step 10) {
                val tropicalLon = lon.toFloat()
                val raDec = eclipticToRaDec(tropicalLon, 0f)
                vertices.add(getGeocentricCoords(raDec.ra, raDec.dec))
            }
            
            return vertices
        }

        private data class RaDec(val ra: Float, val dec: Float)
    }
}


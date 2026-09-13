// Copyright 2008 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.aadipanchang.map.layers

import android.content.SharedPreferences
import android.content.res.Resources
import android.util.Log
import com.aadipanchang.map.R
import com.aadipanchang.map.base.TimeConstants
import com.aadipanchang.map.control.AstronomerModel
import com.aadipanchang.map.ephemeris.SolarSystemBody
import com.aadipanchang.map.math.getGeocentricCoords
import com.aadipanchang.map.renderer.RendererController
import com.aadipanchang.map.search.SearchResult
import com.aadipanchang.map.space.Universe
import com.aadipanchang.map.util.MiscUtil
import java.util.concurrent.locks.ReentrantLock
import kotlin.math.abs

/**
 * If enabled, keeps the sky gradient up to date.
 *
 * @author John Taylor
 * @author Brent Bryan
 */
class SkyGradientLayer(private val model: AstronomerModel, resources: Resources) :
    Layer {
    private val rendererLock = ReentrantLock()
    @Volatile
    private var renderer: RendererController? = null
    @Volatile
    private var lastUpdateTimeMs = 0L
    @Volatile
    private var isVisible = false

    override fun initialize() {}

    override fun registerWithRenderer(rendererController: RendererController) {
        renderer = rendererController
        rendererController.addUpdateClosure(::redraw)
        redraw()
    }

    override fun setVisible(visible: Boolean) {
        Log.d(TAG, "Setting showSkyGradient $visible")
        isVisible = visible
        if (visible) {
            lastUpdateTimeMs = 0
            redraw()
        } else {
            rendererLock.lock()
            try {
                renderer?.queueDisableSkyGradient()
            } finally {
                rendererLock.unlock()
            }
        }
    }

    /** Redraws the sky shading gradient using the model's current time.  */
    protected fun redraw() {
        if (!isVisible) return
        val modelTime = model.time
        if (abs(modelTime.time - lastUpdateTimeMs) > UPDATE_FREQUENCY_MS) {
            lastUpdateTimeMs = modelTime.time
            val sunPosition = universe.solarSystemObjectFor(SolarSystemBody.Sun).getRaDec(modelTime)
            // Log.d(TAG, "Enabling sky gradient with sun position " + sunPosition);
            rendererLock.lock()
            try {
                renderer?.queueEnableSkyGradient(getGeocentricCoords(sunPosition))
            } finally {
                rendererLock.unlock()
            }
        }
    }

    override val layerDepthOrder = -10
    override val preferenceId = "show_sky_gradient"
    override val layerName = resources.getString(R.string.show_sky_gradient)

    override fun searchByObjectName(name: String): List<SearchResult> {
        return emptyList()
    }

    override fun getObjectNamesMatchingPrefix(prefix: String): Set<String> {
        return emptySet()
    }

    companion object {
        private val TAG = MiscUtil.getTag(SkyGradientLayer::class.java)
        private const val UPDATE_FREQUENCY_MS = 5L * TimeConstants.MILLISECONDS_PER_MINUTE
        val universe = Universe()
    }
}
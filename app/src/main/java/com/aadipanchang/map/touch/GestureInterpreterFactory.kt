package com.aadipanchang.map.touch

import android.content.SharedPreferences
import com.aadipanchang.map.activities.util.FullscreenControlsManager
import com.aadipanchang.map.education.ObjectInfoTapHandler
import com.aadipanchang.map.util.Analytics
import com.aadipanchang.map.util.Toaster
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class GestureInterpreterFactory @Inject constructor(
    val mapMover: MapMover,
    val objectInfoTapHandler: ObjectInfoTapHandler?,
    val preferences: SharedPreferences,
    val toaster: Toaster,
    val analytics: Analytics
) {
    fun createGestureInterpreter(
        fullscreenControlsManager: FullscreenControlsManager,
        screenDimensionsProvider: GestureInterpreter.ScreenDimensionsProvider
    ): GestureInterpreter {
        return GestureInterpreter(
            mapMover,
            objectInfoTapHandler,
            preferences,
            toaster,
            analytics,
            fullscreenControlsManager,
            screenDimensionsProvider)
    }
}

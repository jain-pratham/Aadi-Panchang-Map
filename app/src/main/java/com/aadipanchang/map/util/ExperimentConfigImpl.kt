package com.aadipanchang.map.util

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local implementation of ExperimentConfig replacing Firebase Remote Config.
 * Operates completely offline with zero server dependencies.
 */
@Singleton
class ExperimentConfigImpl @Inject constructor(
    private val preferences: SharedPreferences
) : ExperimentConfig {

    override fun isEnabled(experiment: Experiment): Boolean {
        return when (experiment) {
            Experiment.WARM_WELCOME -> preferences.getBoolean(experiment.remoteConfigKey, true)
        }
    }

    override fun waitForInitialFetch(timeoutMs: Long): Boolean {
        return true
    }
}

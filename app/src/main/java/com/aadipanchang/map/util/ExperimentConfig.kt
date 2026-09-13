package com.aadipanchang.map.util

interface ExperimentConfig {
    fun isEnabled(experiment: Experiment): Boolean
    fun waitForInitialFetch(timeoutMs: Long = 2000): Boolean
}

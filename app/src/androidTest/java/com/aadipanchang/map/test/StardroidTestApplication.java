package com.aadipanchang.map.test;

import com.aadipanchang.map.StardroidApplication;
import dagger.hilt.android.testing.CustomTestApplication;

/**
 * A custom test application that extends StardroidApplication to satisfy dependencies
 * that require the specific application class.
 */
@CustomTestApplication(StardroidApplication.class)
public interface StardroidTestApplication {
}

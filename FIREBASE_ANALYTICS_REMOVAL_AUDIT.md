# FIREBASE ANALYTICS REMOVAL AUDIT

**Project:** Aadi Panchang Map  
**Package:** `com.aadipanchang.map`  
**Target:** Complete Audit for Firebase Analytics Removal  
**Mode:** READ-ONLY AUDIT & REMOVAL SPECIFICATION  
**Date:** October 19, 2026  

---

## 1. Executive Summary

This audit evaluates the codebase to establish a complete removal plan for **Firebase Analytics** from the *Aadi Panchang Map* Android application.

### Key Conclusions:
1. **Astronomy Integrity:** Removing Firebase Analytics has **zero impact** on celestial calculations, sensor tracking, ephemeris, time travel, night mode, star/planet rendering, or location positioning.
2. **Remote Config Independence:** **Firebase Remote Config** (`com.google.firebase:firebase-config`) is completely decoupled from Analytics in app source code. It only manages a single feature flag (`warm_welcome_enabled`) and can continue functioning independently without the `firebase-analytics` library.
3. **Privacy & Policy Elimination:** Removing Firebase Analytics eliminates all 4 advertising and tracking permissions transitively merged into the GMS release build (`AD_ID`, `ACCESS_ADSERVICES_AD_ID`, `ACCESS_ADSERVICES_ATTRIBUTION`, `BIND_GET_INSTALL_REFERRER_SERVICE`), resolving Google Play Data Safety and Advertising ID Declaration compliance requirements.
4. **F-Droid Precedent:** The F-Droid build flavor (`fdroidRelease`) already runs with no-op analytics stubs and no Firebase dependencies; removing Analytics from GMS brings both flavors into architectural and privacy alignment.

---

## 2. Firebase Analytics Dependencies in Gradle

### File: `app/build.gradle`

```groovy
// Line 170-173
gmsImplementation platform('com.google.firebase:firebase-bom:33.7.0')
gmsImplementation 'com.google.firebase:firebase-analytics'           // <--- TARGET FOR REMOVAL
gmsImplementation 'com.google.firebase:firebase-config'              // <--- RETAIN FOR REMOTE CONFIG
gmsImplementation 'com.google.android.gms:play-services-location:21.3.0' // <--- RETAIN FOR FUSED LOCATION
```

- **Target Dependency:** `gmsImplementation 'com.google.firebase:firebase-analytics'` (Line 171)
- **Safe to Remove:** **YES**.
- **Side Effects:** Removes the Google Analytics for Firebase SDK and all its transitive dependencies (`play-services-measurement`, `play-services-ads-identifier`, `transport-runtime`, Privacy Sandbox libraries).
- **Retained Dependencies:**
  - `platform('com.google.firebase:firebase-bom:33.7.0')` is still required to manage the version of `firebase-config`.
  - `com.google.firebase:firebase-config` remains active.
  - `com.google.android.gms:play-services-location:21.3.0` remains active.

### Build Flavor ResValues:
- [app/build.gradle:121](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/build.gradle#L121): `resValue "string", "analytics_enabled", "true"` in `gms` block.
- [app/build.gradle:127](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/build.gradle#L127): `resValue "string", "analytics_enabled", "false"` in `fdroid` block.
- **Recommended Action:** Remove or set `analytics_enabled` to `"false"` in the `gms` flavor.

---

## 3. Firebase Analytics Initialization Points

### Point 1: GMS Wrapper Constructor
- **File:** [app/src/gms/java/com/aadipanchang/map/util/Analytics.java:37-41](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/gms/java/com/aadipanchang/map/util/Analytics.java#L37-L41)
- **Code:**
  ```java
  @Inject
  Analytics(StardroidApplication application) {
    firebaseAnalytics = FirebaseAnalytics.getInstance(application);
    Task<String> appId = firebaseAnalytics.getAppInstanceId();
    appId.addOnCompleteListener(task -> Log.d(TAG, "Firebase ID " + task.getResult()));
  }
  ```
- **What It Does:** Obtains the singleton `FirebaseAnalytics` instance from the Android application context and queries the Firebase App Instance ID.
- **Safe to Remove:** **YES**. Replacing this class with a no-op implementation (identical to the F-Droid version) eliminates all SDK interaction.

### Point 2: Application Startup Setup
- **File:** [app/src/main/java/com/aadipanchang/map/StardroidApplication.kt:103-137](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/StardroidApplication.kt#L103-L137)
- **Code:**
  ```kotlin
  private fun setUpAnalytics(versionName: String?) {
    analytics.setEnabled(preferences.getBoolean(AnalyticsInterface.PREF_KEY, true))
    PreferencesButton.setAnalytics(analytics)
    ...
    analytics.setUserProperty(AnalyticsInterface.NEW_USER, newUser.toString())
    ...
    preferences.registerOnSharedPreferenceChangeListener(preferenceChangeAnalyticsTracker)
  }
  ```
- **What It Does:** Reads `enable_analytics` preference, enables/disables the tracker, sets cohort user properties, and registers `PreferenceChangeAnalyticsTracker`.
- **Safe to Remove:** **YES**. Can be removed or stubbed.

### Point 3: Dagger Hilt Dependency Injection
- **File:** [app/src/main/java/com/aadipanchang/map/ApplicationModule.kt:74-75](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/ApplicationModule.kt#L74-L75)
- **Code:**
  ```kotlin
  @Provides
  @Singleton
  fun provideAnalytics(analytics: Analytics): AnalyticsInterface = analytics
  ```
- **What It Does:** Binds variant-specific `Analytics` implementation to `AnalyticsInterface`.
- **Safe to Remove:** **YES**. Can bind a common `NoOpAnalytics` class in `main` or retain variant-agnostic no-op.

---

## 4. Complete Inventory of Analytics Logging Calls

All event logging and user property calls route through `AnalyticsInterface.trackEvent()` and `AnalyticsInterface.setUserProperty()`.

### A. Event Logging Calls (`analytics.trackEvent(...)` — 25 Total)

| # | File Path & Line | Event Name Constant | Parameters Sent | Safe to Remove? |
| :-: | :--- | :--- | :--- | :-: |
| 1 | [PreferencesButton.java:147](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/views/PreferencesButton.java#L147) | `PREFERENCE_BUTTON_TOGGLE_EVENT` | `preference_toggle_value` (key + state) | **YES** |
| 2 | [GestureInterpreter.kt:112](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/touch/GestureInterpreter.kt#L112) | `HORIZON_LEVEL_TOGGLED_EVENT` | `enabled` (boolean) | **YES** |
| 3 | [SensorAccuracyMonitor.kt:86](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/util/SensorAccuracyMonitor.kt#L86) | `CALIBRATION_TOAST_SHOWN_EVENT` | `null` | **YES** |
| 4 | [SensorAccuracyMonitor.kt:89](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/util/SensorAccuracyMonitor.kt#L89) | `CALIBRATION_AUTO_TRIGGERED_EVENT` | `null` | **YES** |
| 5 | [PreferenceChangeAnalyticsTracker.kt:45](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/util/PreferenceChangeAnalyticsTracker.kt#L45) | `PREFERENCE_CHANGE_EVENT` | `value` (key:value or REDACTED) | **YES** |
| 6 | [StardroidApplication.kt:286](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/StardroidApplication.kt#L286) | `START_EVENT` | `local_hour`, `day_of_week`, sensor path, settings snapshot | **YES** |
| 7 | [ObjectInfoTapHandler.kt:94](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/education/ObjectInfoTapHandler.kt#L94) | `OBJECT_INFO_VIEWED_EVENT` | `object_id`, `object_type` | **YES** |
| 8 | [WarmWelcomeActivity.kt:61](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/WarmWelcomeActivity.kt#L61) | `WARM_WELCOME_STARTED_EVENT` | `is_manual_invocation` | **YES** |
| 9 | [WarmWelcomeActivity.kt:67](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/WarmWelcomeActivity.kt#L67) | `WARM_WELCOME_SLIDE_VIEWED_EVENT` | `slide_number` (1) | **YES** |
| 10 | [WarmWelcomeActivity.kt:92](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/WarmWelcomeActivity.kt#L92) | `WARM_WELCOME_SLIDE_VIEWED_EVENT` | `slide_number` (position + 1) | **YES** |
| 11 | [WarmWelcomeActivity.kt:116](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/WarmWelcomeActivity.kt#L116) | `WARM_WELCOME_SKIPPED_EVENT` | `slide_number` | **YES** |
| 12 | [WarmWelcomeActivity.kt:123](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/WarmWelcomeActivity.kt#L123) | `WARM_WELCOME_COMPLETED_EVENT` | `null` | **YES** |
| 13 | [GeoapifyMapsAdapter.kt:166](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/util/GeoapifyMapsAdapter.kt#L166) | `MAP_LOAD_EVENT` | `success`, `provider`, `error_code` | **YES** |
| 14 | [DynamicStarMapActivity.java:525](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/DynamicStarMapActivity.java#L525) | `NO_SENSORS_WARNING_EVENT` | `null` | **YES** |
| 15 | [DynamicStarMapActivity.java:690](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/DynamicStarMapActivity.java#L690) | `MENU_ITEM_EVENT` | `menu_item` (string ID) | **YES** |
| 16 | [DynamicStarMapActivity.java:734](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/DynamicStarMapActivity.java#L734) | `SESSION_LENGTH_EVENT` | `session_length`, `session_bucket` | **YES** |
| 17 | [DynamicStarMapActivity.java:846](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/DynamicStarMapActivity.java#L846) | `TIME_TRAVEL_USED_EVENT` | `travel_event` ("Preset:...") | **YES** |
| 18 | [DynamicStarMapActivity.java:888](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/DynamicStarMapActivity.java#L888) | `TIME_TRAVEL_USED_EVENT` | `travel_event` ("Manual:...") | **YES** |
| 19 | [DynamicStarMapActivity.java:981](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/DynamicStarMapActivity.java#L981) | `LAYER_TOGGLED_EVENT` | `layer_name`, `layer_enabled` | **YES** |
| 20 | [DynamicStarMapActivity.java:1033](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/DynamicStarMapActivity.java#L1033) | `SEARCH_EVENT` | `search_term`, `search_success` | **YES** |
| 21 | [DynamicStarMapActivity.java:1051](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/DynamicStarMapActivity.java#L1051) | `SEARCH_FAILED_EVENT` | `search_term` | **YES** |
| 22 | [DynamicStarMapActivity.java:1113](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/DynamicStarMapActivity.java#L1113) | `MANUAL_MODE_TOGGLED_EVENT` | `enabled` (boolean) | **YES** |
| 23 | [DynamicStarMapActivity.java:1333](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/DynamicStarMapActivity.java#L1333) | `OBJECT_LOCKED_EVENT` | `object_name`, `mode` | **YES** |
| 24 | [EulaDialogFragment.java:149](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/dialogs/EulaDialogFragment.java#L149) | `TOS_ACCEPTED_EVENT` | Empty bundle | **YES** |
| 25 | [EulaDialogFragment.java:158](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/dialogs/EulaDialogFragment.java#L158) | `TOS_REJECTED_EVENT` | Empty bundle | **YES** |

### B. User Property Calls (`analytics.setUserProperty(...)` — 9 Total)

| # | File Path & Line | Property Key Constant | Value Set | Safe to Remove? |
| :-: | :--- | :--- | :--- | :-: |
| 1 | [StardroidApplication.kt:123](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/StardroidApplication.kt#L123) | `NEW_USER` (`new_user_prop`) | `"true"` / `"false"` | **YES** |
| 2 | [StardroidApplication.kt:125](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/StardroidApplication.kt#L125) | `FIRST_INSTALL_VERSION` | Version name | **YES** |
| 3 | [StardroidApplication.kt:127](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/StardroidApplication.kt#L127) | `USER_LOCALE` | `Locale.getDefault().toLanguageTag()` | **YES** |
| 4 | [StardroidApplication.kt:187](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/StardroidApplication.kt#L187) | `DEVICE_SENSORS` | `"none"` | **YES** |
| 5 | [StardroidApplication.kt:208](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/StardroidApplication.kt#L208) | `DEVICE_SENSORS` | Pipe-delimited list of sensors | **YES** |
| 6 | [StardroidApplication.kt:211](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/StardroidApplication.kt#L211) | `HAS_GYRO` | Boolean string | **YES** |
| 7 | [StardroidApplication.kt:215](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/StardroidApplication.kt#L215) | `HAS_ROTATION_VECTOR` | Boolean string | **YES** |
| 8 | [StardroidApplication.kt:304](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/StardroidApplication.kt#L304) | `SENSOR_LIAR` | `"true"` | **YES** |
| 9 | [WarmWelcomeActivity.kt:159](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/WarmWelcomeActivity.kt#L159) | `COMPLETED_WARM_WELCOME` | Boolean string | **YES** |

---

## 5. Classes and Helpers Used Exclusively by Analytics

| File Path | Role | Exclusively Analytics? | Recommended Action |
| :--- | :--- | :-: | :--- |
| [app/src/gms/java/.../util/Analytics.java](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/gms/java/com/aadipanchang/map/util/Analytics.java) | GMS wrapper calling `FirebaseAnalytics` SDK | **YES** | Replace with no-op implementation or remove. |
| [app/src/fdroid/java/.../util/Analytics.java](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/fdroid/java/com/aadipanchang/map/util/Analytics.java) | F-Droid no-op stub | **YES** | Retain or unify with GMS no-op. |
| [app/src/fdroid/java/.../FirebaseAnalytics.java](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/fdroid/java/com/google/firebase/analytics/FirebaseAnalytics.java) | Dummy class to stub SDK imports | **YES** | Delete (no longer needed if SDK references are removed). |
| [app/src/main/java/.../util/PreferenceChangeAnalyticsTracker.kt](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/util/PreferenceChangeAnalyticsTracker.kt) | Logs SharedPreferences changes to Analytics | **YES** | Delete class and unregister listener. |
| [app/src/main/java/.../util/AnalyticsInterface.java](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/util/AnalyticsInterface.java) | Public interface defining tracking methods & constants | **YES** | Retain as no-op interface or remove callers. |

---

## 6. Preferences, UI Elements, and Opt-Out Implementation

### 1. Preference Definition
- **File:** [app/src/main/res/xml/preference_screen.xml:100-105](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/res/xml/preference_screen.xml#L100-L105)
  ```xml
  <CheckBoxPreference
      android:defaultValue="@string/analytics_enabled"
      android:title="@string/enable_analytics"
      android:summary="@string/enable_analytics_desc"
      android:key="enable_analytics"/>
  ```
- **What It Does:** Renders the "Send usage statistics" toggle in App Settings.
- **Recommended Action:** Remove this `<CheckBoxPreference>` entry from `preference_screen.xml`.

### 2. Settings Activity Handling
- **File:** [app/src/main/java/com/aadipanchang/map/activities/EditSettingsActivity.java:48, 144-147](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/EditSettingsActivity.java#L144-L147)
  ```java
  private void updatePreferences() {
    Log.d(TAG, "Updating preferences");
    analytics.setEnabled(preferenceFragment.findPreference(Analytics.PREF_KEY).isEnabled());
  }
  ```
- **What It Does:** Tries to toggle Analytics on/off when closing settings (currently contains the `.isEnabled()` defect).
- **Recommended Action:** Remove `analytics` injection and `updatePreferences()` call.

### 3. Strings & Resources
- `@string/enable_analytics` and `@string/enable_analytics_desc` across 34 locale folders under `app/src/main/res/values-*/strings.xml`.
- **Recommended Action:** Safe to leave or clean up unused string resources.

---

## 7. Impact on Astronomy Functionality

| Astronomy / App Feature | Uses Analytics Output? | Breaks if Analytics Removed? |
| :--- | :-: | :-: |
| Celestial coordinate calculations (`AstronomerModel`, `Ephemeris`, `RaDec`) | **NO** | **NO** |
| Sensor fusion & orientation (`SensorOrientationController`, accelerometer, compass, gyro) | **NO** | **NO** |
| Star, planet, constellation, meteor shower, grid & horizon rendering | **NO** | **NO** |
| Time travel mode (`TimeTravelDialog`, date stepping) | **NO** | **NO** |
| Search functionality (`LayerManager.searchByObjectName`, `SearchTermsProvider`) | **NO** | **NO** |
| Object info cards & Hubble gallery images (`ObjectInfoRegistry`, `GalleryAdapter`) | **NO** | **NO** |
| Geolocation handling (`LocationController`, `FusedLocationProvider`, `PlatformLocationProvider`) | **NO** | **NO** |
| Night vision mode (`NightModeHelper`, screen dimming) | **NO** | **NO** |

**Conclusion:** Removing Analytics has **ZERO effect** on the core astronomy experience. All analytics tracking calls are strictly one-way void telemetry calls.

---

## 8. Firebase Remote Config Independence & Parameter Audit

### Independence Verification:
- **Code Coupling:** `FirebaseRemoteConfig` does **NOT** depend on `AnalyticsInterface` or `FirebaseAnalytics` anywhere in the app code.
- **SDK Level:** Firebase Remote Config (`firebase-config:22.x`) handles parameter fetching independently using `FirebaseInstallations`. It treats `AnalyticsConnector` as an optional dependency; when Analytics is absent, Remote Config simply fetches parameters without logging A/B testing exposure events.

### Remote Config Parameters in Code:

| Parameter Key | Defined In | Default Value | Functional Effect |
| :--- | :--- | :--- | :--- |
| `warm_welcome_enabled` | [remote_config_defaults.xml:4](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/gms/res/xml/remote_config_defaults.xml#L4), [Experiment.kt:4](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/util/Experiment.kt#L4) | `false` | Controls whether a new user is shown the 3-slide onboarding tour (`WarmWelcomeActivity`) before entering the sky map ([StartupRouter.kt:25-28](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/main/java/com/aadipanchang/map/activities/StartupRouter.kt#L25-L28)). |

- **Other Parameters:** None. No astronomy parameters, sensor calibration constants, or celestial data are controlled via Remote Config.
- **Conclusion:** Remote Config can be safely retained while completely dropping Analytics.

---

## 9. Manifest Permissions & Transitive Dependencies

### Permissions Eliminated by Removing `firebase-analytics`:
1. `com.google.android.gms.permission.AD_ID` (Play Services Advertising Identifier)
2. `android.permission.ACCESS_ADSERVICES_AD_ID` (Privacy Sandbox Ads ID)
3. `android.permission.ACCESS_ADSERVICES_ATTRIBUTION` (Privacy Sandbox Attribution)
4. `com.google.android.finsky.permission.BIND_GET_INSTALL_REFERRER_SERVICE` (Play Install Referrer)

### Permissions Retained (Essential for App):
1. `android.permission.ACCESS_COARSE_LOCATION` (Used by `FusedLocationProvider` / `PlatformLocationProvider`)
2. `android.permission.INTERNET` (Used by Geoapify Static Maps and Firebase Remote Config)
3. `android.permission.ACCESS_NETWORK_STATE` (Used by Coil image loader and Remote Config)
4. `android.permission.WAKE_LOCK` (Keeps screen on during observation)
5. `android.permission.VIBRATE` (Haptic feedback for compass calibration and toggles)
6. `com.aadipanchang.map.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` (Android 14+ IPC security)

---

## 10. Shared Dependencies After Analytics Removal

1. **`com.google.android.gms:play-services-location:21.3.0`:**
   - Depends only on standard Play Services base (`play-services-base`, `play-services-basement`, `play-services-tasks`).
   - Has zero shared requirements with Firebase Analytics.
2. **`com.google.firebase:firebase-config`:**
   - Managed by `platform('com.google.firebase:firebase-bom:33.7.0')`.
   - Utilizes `firebase-common`, `firebase-installations`, and `firebase-abt`.
   - Functions completely without `firebase-analytics`.
   - **Important Note:** [app/src/gms/res/values/firebase.xml](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/gms/res/values/firebase.xml) contains the upstream project ID `sky-map-1286`. If Remote Config is kept, this file must still exist so `FirebaseRemoteConfig.getInstance()` can initialize.

---

## 11. Test Dependencies on Analytics

- **`app/src/test` (Unit Tests):**
  - Only [ObjectInfoTapHandlerTest.kt:48](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/app/src/test/java/com/aadipanchang/map/education/ObjectInfoTapHandlerTest.kt#L48) references `AnalyticsInterface`:
    `@Mock private lateinit var mockAnalytics: AnalyticsInterface`
  - The mock is passed to `ObjectInfoTapHandler` constructor to satisfy Dagger injection.
  - None of the test cases verify `mockAnalytics.trackEvent()` or assert on analytics behavior.
- **`app/src/androidTest` (Instrumented Tests):**
  - **Zero** tests reference `Analytics`, `FirebaseAnalytics`, or `AnalyticsInterface`.

---

## 12. Recommended Removal Plan

The cleanest, lowest-risk strategy is **Option A (No-Op Architecture)**:

### Phase 1: Dependency & Manifest Decoupling
1. In `app/build.gradle`:
   - Remove line 171: `gmsImplementation 'com.google.firebase:firebase-analytics'`.
   - Set `analytics_enabled` to `"false"` in the `gms` flavor (line 121).
   - Keep `firebase-bom:33.7.0` and `firebase-config`.

### Phase 2: Convert GMS Analytics to No-Op
2. In `app/src/gms/java/com/aadipanchang/map/util/Analytics.java`:
   - Remove `import com.google.firebase.analytics.FirebaseAnalytics;`.
   - Replace implementation with no-op methods (matching `app/src/fdroid/java/.../Analytics.java`):
     ```java
     public class Analytics implements AnalyticsInterface {
       @Inject
       Analytics(StardroidApplication application) {}

       @Override public void setEnabled(boolean enabled) {}
       @Override public void trackEvent(String event, Bundle params) {}
       @Override public void setUserProperty(String name, String value) {}
     }
     ```
3. In `app/src/fdroid/java/com/google/firebase/analytics/FirebaseAnalytics.java`:
   - Delete this dummy stub file.

### Phase 3: Remove Analytics UI Toggle & SharedPreferences Listener
4. In `app/src/main/res/xml/preference_screen.xml`:
   - Remove `<CheckBoxPreference android:key="enable_analytics" ... />`.
5. In `app/src/main/java/com/aadipanchang/map/activities/EditSettingsActivity.java`:
   - Remove `updatePreferences()` and `analytics.setEnabled(...)` call.
6. In `app/src/main/java/com/aadipanchang/map/util/PreferenceChangeAnalyticsTracker.kt`:
   - Remove class or disable registration in `StardroidApplication.kt:136`.

### Phase 4: Policy & Store Declarations
7. In `PRIVACY_POLICY.md`:
   - Update Section 4 to state that the application contains **zero analytics SDKs and collects zero telemetry across all build flavors**.
   - Confirm that no advertising IDs or search histories are collected or declared.
8. In Google Play Console:
   - Complete Advertising ID Declaration stating that the app **does not** use advertising ID.
   - In Data Safety form, mark Search History, App Interactions, and Diagnostics as **Not Collected**.

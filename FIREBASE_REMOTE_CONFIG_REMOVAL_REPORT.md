# FIREBASE REMOTE CONFIG REMOVAL REPORT

**Project:** Aadi Panchang Map  
**Package:** `com.aadipanchang.map`  
**Platform:** Android  
**Date:** October 19, 2026  
**Task:** TASK 15 — Complete Firebase Remote Config Removal  

---

## 1. EXECUTIVE SUMMARY

Firebase Remote Config and all remaining Firebase SDK infrastructure have been completely and cleanly removed from the Aadi Panchang Map application.

With this task, **the application is now 100% free of all Firebase and Google Play Measurement dependencies** across both the Google Play Services (GMS) and F-Droid build variants.

Key accomplishments:
- **Dependency Elimination:** Removed `com.google.firebase:firebase-bom:33.7.0` and `com.google.firebase:firebase-config` from `app/build.gradle`.
- **Zero Firebase Libraries Remaining:** Neither variant contains any Firebase SDK, library, BoM, or measurement connector.
- **Unified Local Experiment Architecture:** Created a local, offline implementation of `ExperimentConfigImpl.kt` in `main/java`, completely replacing the flavor-specific implementations and eliminating `ExperimentModule.kt`.
- **Resource Cleanup:** Deleted `app/src/gms/res/xml/remote_config_defaults.xml` and `app/src/gms/res/values/firebase.xml`.
- **Upstream Project Disconnected:** The upstream Firebase project reference (`sky-map-1286`) has been completely removed from the project resources.
- **Onboarding Experience Preserved:** Retained the complete 3-slide Warm Welcome onboarding flow, with local default `warm_welcome_enabled = true`. First-time users see onboarding on clean install; returning users bypass it; manual invocation via Menu → Tutorial remains fully operational.
- **Merged Manifests Verified:** All Firebase registrars (`FirebaseRemoteConfigKtxRegistrar`, `RemoteConfigRegistrar`, etc.), providers (`FirebaseInitProvider`), and services (`ComponentDiscoveryService`) are completely absent from merged manifests.
- **Privacy Policy Updated:** Updated `PRIVACY_POLICY.md` to remove all Remote Config disclosures and references to Google Firebase connections.
- **Builds & Unit Tests Validated:** Both `assembleGmsDebug` and `assembleFdroidDebug` succeeded with exit code 0. Unit tests completed with 193 passing tests and 0 analytics/remote config regressions.

---

## 2. FILES CHANGED

| Status | File Path | Description of Change |
|---|---|---|
| **Modified** | `app/build.gradle` | Removed `firebase-bom:33.7.0` and `firebase-config`. Retained only `play-services-location:21.3.0` under GMS flavor dependencies. |
| **Created** | `app/src/main/java/com/aadipanchang/map/util/ExperimentConfigImpl.kt` | Created unified, local offline implementation of `ExperimentConfig` with default `warm_welcome_enabled = true`. |
| **Deleted** | `app/src/gms/java/com/aadipanchang/map/util/ExperimentConfigImpl.kt` | Deleted Firebase Remote Config fetch and initialization class. |
| **Deleted** | `app/src/fdroid/java/com/aadipanchang/map/util/ExperimentConfigImpl.kt` | Deleted F-Droid flavor-specific stub. |
| **Deleted** | `app/src/gms/java/com/aadipanchang/map/util/ExperimentModule.kt` | Deleted Hilt module providing `FirebaseRemoteConfig`. |
| **Deleted** | `app/src/gms/res/xml/remote_config_defaults.xml` | Deleted obsolete Remote Config defaults XML. |
| **Deleted** | `app/src/gms/res/values/firebase.xml` | Deleted upstream Firebase project configuration (`sky-map-1286`). |
| **Modified** | `PRIVACY_POLICY.md` | Removed all Remote Config disclosures, network explanations, and Google Firebase references. |

---

## 3. DEPENDENCIES REMOVED

In `app/build.gradle`:
- **Removed:**
  ```groovy
  gmsImplementation platform('com.google.firebase:firebase-bom:33.7.0')
  gmsImplementation 'com.google.firebase:firebase-config'
  ```
- **Retained Flavor Dependency:**
  ```groovy
  gmsImplementation 'com.google.android.gms:play-services-location:21.3.0'
  ```

### Dependency Tree Verification
- **GMS Release Runtime Classpath:**
  Command: `.\gradlew app:dependencies --configuration gmsReleaseRuntimeClasspath | Select-String -Pattern "firebase"`
  Result: **0 matches (Completely absent)**.
- **F-Droid Release Runtime Classpath:**
  Command: `.\gradlew app:dependencies --configuration fdroidReleaseRuntimeClasspath | Select-String -Pattern "firebase"`
  Result: **0 matches (Completely absent)**.

---

## 4. FIREBASE REFERENCES REMAINING

A repository-wide search was conducted for `FirebaseRemoteConfig`, `firebase-config`, `fetchAndActivate`, and `firebase.xml`:
- **In Source Code (`app/src`):** **ZERO Firebase references remaining.**
- **In Build Scripts:** **ZERO Firebase references remaining.**
- **In Manifests:** **ZERO Firebase components, registrars, or providers.**
- **Historical Reports:** Only historical references exist in previously committed Markdown audit and implementation reports.

### Upstream Project Ownership Resolution
- `app/src/gms/res/values/firebase.xml` (which previously declared project `sky-map-1286`, app ID `1:1013100360622:android:8dd01fc8df295f5188ce3f`, and storage buckets) has been deleted.
- The app no longer connects to or references `sky-map-1286`.
- The upstream project ownership authorization issue flagged in Task 14 is now completely resolved.

---

## 5. ONBOARDING BEHAVIOR (BEFORE AND AFTER)

### Before (Task 14 State):
- On startup, `ExperimentConfigImpl` in GMS initiated an asynchronous `fetchAndActivate()` request to Google Firebase servers.
- Local XML defaults set `warm_welcome_enabled = false`.
- If the remote server fetch timed out, failed, or was unauthorized (as was likely with the upstream `sky-map-1286` project), `warm_welcome_enabled` fell back to `false`.
- In F-Droid, `ExperimentConfigImpl` returned a hardcoded `false`.
- Consequently, onboarding was skipped for users unless explicitly enabled via server response.

### After (Task 15 Implementation):
- `ExperimentConfigImpl` now operates 100% locally on-device:
  ```kotlin
  @Singleton
  class ExperimentConfigImpl @Inject constructor(
      private val preferences: SharedPreferences
  ) : ExperimentConfig {
      override fun isEnabled(experiment: Experiment): Boolean {
          return when (experiment) {
              Experiment.WARM_WELCOME -> preferences.getBoolean(experiment.remoteConfigKey, true)
          }
      }
      override fun waitForInitialFetch(timeoutMs: Long): Boolean = true
  }
  ```
- **Local Default:** `warm_welcome_enabled` defaults to `true`.
- **First-Time Users:**
  1. On first launch (`read_warm_welcome_version <= 0`), `StartupRouter.needsWarmWelcome()` returns `true`.
  2. `SplashScreenActivity` launches `WarmWelcomeActivity`.
  3. The user sees all three introduction slides (Cosmos Exploration, Sky Navigation, and Sensors & Calibration).
  4. Upon tapping "Launch!" or "Skip", `markWarmWelcomeSeen()` records `app.version` into `ApplicationConstants.READ_WARM_WELCOME_PREF_VERSION` and sets `ApplicationConstants.NO_WARN_ABOUT_MISSING_SENSORS = true`.
- **Returning Users:**
  1. On subsequent launches, `read_warm_welcome_version` is positive (`> 0`).
  2. `StartupRouter.needsWarmWelcome()` returns `false`.
  3. `SplashScreenActivity` immediately transitions into `DynamicStarMapActivity` (or `WhatsNewDialogFragment` if the app version has updated).
  4. Returning users can re-open the tutorial at any time by selecting **Menu → Tutorial** in `DynamicStarMapActivity` (`tutorialItem.setVisible(true)` remains active).
- **Offline Reliability:** Zero network calls, zero latency delays on splash screen (`waitForInitialFetch` completes immediately), and zero dependence on Google servers.

---

## 6. PRIVACY POLICY CHANGES

[PRIVACY_POLICY.md](file:///c:/Projects/Freelancer/Sky%20Map_Original/stardroid-v1/PRIVACY_POLICY.md) was updated as follows:
1. **Section 2 (Information We Access):** Removed reference to Firebase Remote Config from network permissions. Clarified that `INTERNET` and `ACCESS_NETWORK_STATE` permissions are used solely for optional static map tile rendering (Geoapify Maps API).
2. **Section 8 (Third-Party Services):** Completely removed the `Firebase Remote Config (GMS Build Only)` section. Updated service disclosures to explicitly state that the application contains no remote configuration SDKs (such as Firebase Remote Config).
3. **Section 9 (Data Storage and Retention):** Removed the `Remote Config Local Cache` subsection.
4. **Section 12 (Security):** Removed Firebase Remote Config from network communication security statements; retained Geoapify Maps API disclosures.
5. **No Offline Over-Claiming:** Maintained accurate disclosures that optional Geoapify static map tiles transmit center coordinates over HTTPS when configured.

---

## 7. GMS AND F-DROID BUILD RESULTS

| Build Task | Result | Duration | Notes |
|---|---|---|---|
| `.\gradlew assembleGmsDebug` | **SUCCESS** (Exit Code 0) | 50s | Built `app/build/outputs/apk/gms/debug/app-gms-debug.apk`. |
| `.\gradlew assembleFdroidDebug` | **SUCCESS** (Exit Code 0) | 42s | Built `app/build/outputs/apk/fdroid/debug/app-fdroid-debug.apk`. |
| `.\gradlew processGmsReleaseManifest` | **SUCCESS** (Exit Code 0) | 9s | Verified zero Firebase components in merged manifest. |
| `.\gradlew processFdroidReleaseManifest` | **SUCCESS** (Exit Code 0) | 9s | Verified zero Firebase components in merged manifest. |

---

## 8. UNIT TEST RESULTS

| Test Task | Total Tests | Passed | Failed | Details |
|---|---|---|---|---|
| `.\gradlew testGmsDebugUnitTest` | 194 | 193 | 1 | Pre-existing failure: `NakshatraCalculatorTest > getCurrentNakshatra_exampleDate_jan2009 FAILED` (astronomy calculation assertion). 0 Firebase-related failures. |
| `.\gradlew testFdroidDebugUnitTest` | 194 | 193 | 1 | Exactly identical results. |

---

## 9. CODEBASE INTEGRITY & APPLICATION FUNCTIONALITY

All core astronomy and navigation functionality remains intact:
- **Search:** Fully operational via `SearchTermsProvider` and local SQLite index.
- **Sky Rendering:** Untouched (`AstronomerModel`, `RendererModel`, `LayerManager`, `SkyGradientLayer`).
- **Sensor Calibration:** Untouched (`CompassCalibrationActivity`, `SensorAccuracyMonitor`, figure-8 wave prompt).
- **Time Travel:** Untouched (`DynamicStarMapActivity`, manual date/time picker, time speed slider).
- **Location Handling:** Untouched (`LocationManagementActivity`, `FusedLocationProvider`, `PlatformLocationProvider`, optional Geoapify preview adapter).
- **Onboarding Carousel:** 100% functional with local state management.

---

## 10. REMAINING ISSUES

1. **Google Play Console Data Safety Updates:**
   - Because Firebase Remote Config has been removed along with Firebase Analytics, the application makes **zero network calls to Google Firebase servers**.
   - The only remaining network functionality in the entire application is the optional Geoapify static map tile preview in Location Settings (active only if a build-time API key is set).
2. **Release Keystore:**
   - As documented in Task 14, release packaging requires a local `storeFile` in `signingConfigs.releaseapk`. Compilation and R8 minification are fully verified.
3. **Astronomy Calculation Test:**
   - `NakshatraCalculatorTest` failure is a pre-existing assertion discrepancy and should be investigated as a separate astronomy/ephemeris task.

# Final Pre-Release Audit Report for Google Play Console

**Project:** Aadi Panchang Map (`com.aadipanchang.map`)  
**Audit Date:** October 21, 2026  
**Auditor:** Antigravity AI Engineering & Audit Agent  
**Build Target:** Google Play Services Release (`gmsRelease`)  
**Target Android Version:** Android 16 (API 36) | **Min SDK:** Android 8.0 (API 26)  
**Version Code:** 1670 | **Version Name:** 1.15.0:Saturn  

---

## 1. Executive Summary

A comprehensive pre-release technical, legal, privacy, metadata, and security audit of the **Aadi Panchang Map** Android codebase was performed prior to Google Play Console submission.

### Final Readiness Classification
```
========================================================================
             STATUS: READY AFTER MANUAL CONSOLE TASKS
========================================================================
```

### Key Audit Findings Summary
1. **Production Bundle & Signing:** The production Android App Bundle (`app-gms-release.aab`, 19.26 MB) was built and signed using the designated `aadipanchang-upload` release keystore. R8 code shrinking and resource optimization are active. Debuggable flags are disabled.
2. **Target API Compliance:** Target SDK is configured to **API 36** (Android 16), which satisfies and exceeds Google Play's 2025/2026 requirement (Target API 35+).
3. **Telemetry & Privacy:** Zero analytics SDKs exist in the binary. Google Firebase Analytics and Firebase Remote Config have been completely removed. Approximate location (`ACCESS_COARSE_LOCATION`) is processed strictly on-device for astronomy math, with optional static map tiles rendered via Geoapify. The privacy policy is publicly accessible at HTTP 200 OK and accurately discloses IP transmission and location handling.
4. **Unit Test Audit:** 193 of 194 unit tests passed. The historical `NakshatraCalculatorTest.getCurrentNakshatra_exampleDate_jan2009` test failure is reproducible and stems from an expected vs. actual catalog naming assertion for a 2009 historical date; per strict user instructions, Nakshatra calculations and boundaries are preserved as correct for the application's intended panchang system.
5. **Actionable Manual Console Items:**
   - **Feature Graphic:** `fastlane/metadata/android/en-US/images/featureGraphic.jpeg` is currently 1795 × 876 px and must be resized to exactly **1024 × 500 px** before uploading to Google Play Console.
   - **English Screenshots:** Images `01` and `04` in `en-US/phoneScreenshots` contain an alpha channel (RGBA), and images `05` and `06` are duplicates. (In contrast, the Hindi `hi-IN` phone screenshots folder has 5 validated 24-bit RGB non-duplicate screenshots).
   - **Geoapify API Key Restriction:** Key restrictions should be confirmed in the Geoapify web dashboard.
   - **Personal Account Closed Testing:** If the Play Console account was created after Nov 13, 2023, the mandatory 14-day closed test with at least 12 testers must be conducted before applying for production access.

---

## 2. Master Findings Table

| Finding ID | Severity | Category | Component / File | Evidence | Impact / Why It Matters | Recommended Action | Resolution Type |
|---|---|---|---|---|---|---|---|
| **AUD-01** | **BLOCKER** | Store Assets | `en-US/images/featureGraphic.jpeg` | Image dimensions: `1795 × 876 px` | Google Play Console rejects any feature graphic that is not exactly `1024 × 500 px`. | Resize the feature graphic to `1024 × 500 px` (24-bit PNG or JPEG) prior to Play Console upload. | Manual / External Asset |
| **AUD-02** | **HIGH** | Store Assets | `en-US/images/phoneScreenshots/` | Images `01` & `04` are `RGBA` (32-bit with alpha). Images `05` & `06` are identical duplicates (`2,280,584 bytes`). | Play Console requires 24-bit RGB PNG or JPEG without alpha channel. Duplicate images degrade store listing quality. | Convert images to 24-bit RGB (no alpha) and replace duplicate `06` before uploading English screenshots. | Asset Correction |
| **AUD-03** | **HIGH** | Developer Policy | Google Play Console Testing Policy | Accounts created after Nov 13, 2023 must run closed tests. | Personal accounts cannot publish directly to production without completing a 14-day closed test with ≥12 opted-in testers. | Verify account type. If personal, release AAB to the Closed Testing track first. | Manual Console Task |
| **AUD-04** | **HIGH** | Security | `no-checkin.properties` (Geoapify Key) | `geoapify_maps_api_key` embedded in release APK/AAB | An unrestricted API key extracted from client code could be abused by third parties to exhaust API quota. | In Geoapify Developer Dashboard, restrict key by Android package `com.aadipanchang.map` and SHA-256 fingerprint. | External Dashboard |
| **AUD-05** | **MEDIUM** | Testing | `NakshatraCalculatorTest.kt:50` | `getCurrentNakshatra_exampleDate_jan2009` fails (Expected `Shatabhisha`, was `Purva Bhadrapada`). | Test failure indicates discrepancy against historical test assertion. User explicitly instructed not to modify calculations or boundaries. | AAB packaging (`bundleGmsRelease`) builds successfully. Document discrepancy; no release blocker for panchang usage. | Documented / Verified |
| **AUD-06** | **LOW** | Build Config | `app/build.gradle` | `compileSdk 35` with `targetSdk 36` | `targetSdk` is higher than `compileSdk`. While AGP 8.7.3 compiles successfully, Android conventions recommend `compileSdk >= targetSdk`. | Maintain current setup for Android 16 readiness, or set `compileSdk 36` in future maintenance cycle. | Informational / Maintenance |
| **AUD-07** | **INFORMATIONAL** | Legal / Licensing | `app/src/main/res/values/credits.xml` | Split license: GPLv3 (derivatives) + Apache 2.0 (legacy Google components) | Must ensure corresponding source code is available to end users per GPLv3 obligations. | Public GitHub repository `https://github.com/jain-pratham/Aadi-Panchang-Map` satisfies source access requirement. | Compliant |
| **AUD-08** | **INFORMATIONAL** | Privacy & Policy | `PRIVACY_POLICY.md` | HTTP 200 accessible at `https://github.com/jain-pratham/Aadi-Panchang-Map/blob/master/PRIVACY_POLICY.md` | Privacy policy accurately reflects zero telemetry, on-device astronomy, approximate location, and Geoapify IP disclosure. | Submit verified URL in Play Console App Content section. | Ready |

---

## 3. Detailed Audit by Domain

### 3.1 Android Build & Configuration Audit
- **Application ID:** `com.aadipanchang.map` (Verified)
- **Namespace:** `com.aadipanchang.map` (Verified)
- **Version Code:** `1670`
- **Version Name:** `1.15.0:Saturn`
- **SDK Levels:**
  - `minSdkVersion`: 26 (Android 8.0 Oreo, covers >95% of active devices)
  - `targetSdkVersion`: 36 (Android 16, fully compliant with Play Store requirements)
  - `compileSdkVersion`: 35 (Android 15)
- **Build Toolchain:** AGP `8.7.3`, Gradle `8.13`, Kotlin `2.0.20`, Java Toolchain `JDK 17`.
- **R8 / ProGuard:** `minifyEnabled true` in release build type with `proguard-android-optimize.txt` and `proguard.cfg`.
- **Flavors:**
  - `gms`: Google Play Services Location (`com.google.android.gms:play-services-location:21.3.0`), zero telemetry (`analytics_enabled=false`).
  - `fdroid`: Offline open-source variant, zero telemetry.

### 3.2 Manifest Components & Permissions Audit
- **Permissions Declared in Production Manifest:**
  1. `android.permission.ACCESS_COARSE_LOCATION` (Approximate location only; `ACCESS_FINE_LOCATION` is **not** requested).
  2. `android.permission.INTERNET` (Required for optional Geoapify static map tile downloads).
  3. `android.permission.ACCESS_NETWORK_STATE` (Check connectivity before loading map tiles).
  4. `android.permission.WAKE_LOCK` (Keep screen on during night sky observation).
  5. `android.permission.VIBRATE` (Haptic feedback for UI interactions).
- **Exported Activity Security Audit:**
  - `SplashScreenActivity`: `android:exported="true"` with `MAIN` / `LAUNCHER` intent-filter (Android 12+ compliant).
  - `DynamicStarMapActivity`: `android:exported="true"` with `SEARCH` intent-filter.
  - All other activities (`WarmWelcomeActivity`, `EditSettingsActivity`, `ImageGalleryActivity`, `DiagnosticActivity`, `CompassCalibrationActivity`, `LocationManagementActivity`) are explicitly not exported or default unexported.
  - `SearchTermsProvider`: `android:exported="false"`.

### 3.3 Production AAB & Signing Verification
- **AAB File Path:** `app/build/outputs/bundle/gmsRelease/app-gms-release.aab`
- **File Size:** `20,190,994 bytes` (19.26 MB)
- **SHA-256 Checksum:** `0e3dab60fb9c02e4ab9fb344bd73c0cf9260b5552a2b2d1990a21c7af8ce431e`
- **Release Signing Certificate:**
  - **Alias:** `aadipanchang-upload`
  - **Owner:** `CN=Aadi Panchang Map, OU=Engineering, O=Aadi Panchang Map, L=Delhi, ST=Delhi, C=IN`
  - **Issuer:** `CN=Aadi Panchang Map, OU=Engineering, O=Aadi Panchang Map, L=Delhi, ST=Delhi, C=IN`
  - **Validity:** Sun Sep 13 16:03:21 IST 2026 until Thu Jan 29 16:03:21 IST 2054
  - **Certificate SHA-1:** `EF:3F:40:44:E1:AC:DB:D2:37:87:B4:AC:74:A3:D0:34:C6:A7:0E:FD`
  - **Certificate SHA-256:** `9B:61:52:73:75:7E:22:A0:F9:5A:BF:5D:AD:BD:97:53:D8:2F:4E:E0:25:30:06:5B:06:4B:10:7A:E1:21:B1:99`
  - **Verification:** Signed in accordance with Google Play App Signing upload key specifications.

### 3.4 Unit Test & Astro Math Verification
- **Execution Command:** `.\gradlew.bat testGmsDebugUnitTest --continue`
- **Results:** 194 tests executed, 193 passed, 1 failed.
- **Specific Failure:** `NakshatraCalculatorTest.getCurrentNakshatra_exampleDate_jan2009`:
  - Expected: `Shatabhisha` (index 23, `306.67° - 320.00°`)
  - Calculated: `Purva Bhadrapada` (index 24)
  - Cause: Discrepancy between astronomical moon ephemeris sidereal calculation and historical hardcoded test assertion.
  - Release Risk: Low. The user explicitly instructed that current Nakshatra calculations and boundaries are correct for this application and must not be altered. Core boundary logic and catalog mappings passed (`getCurrentNakshatra_returnsExpectedFields`).

### 3.5 Privacy, Data Safety & Security Audit
- **Analytics & Tracking Removal:**
  - `firebase-analytics` library: **Absent**.
  - `firebase-config` library: **Absent**.
  - `com.google.android.gms.permission.AD_ID`: **Absent**.
  - Third-party advertising / crash SDKs: **Zero**.
- **Location Data Handling:**
  - `ACCESS_COARSE_LOCATION` only.
  - Location is processed on-device in `AstronomerModel`.
  - Transmitted off-device **only** if the user opens Location Management and a Geoapify API key is present.
- **Data Safety Form Mapping:**
  - Data Type: *Location > Approximate location*.
  - Collected / Shared: *Shared* (sent to Geoapify for static map tile rendering).
  - Ephemeral: *Yes*.
  - Required / Optional: *Optional*.
  - Purpose: *App functionality*.
  - Encryption: *Yes* (HTTPS).
- **Privacy Policy URL:**
  - `https://github.com/jain-pratham/Aadi-Panchang-Map/blob/master/PRIVACY_POLICY.md` verified live via HTTP GET (Status 200 OK).

### 3.6 Legal & Open-Source Attribution Audit
- **Project Origins:** Independent derivative based on open-source Sky Map (Stardroid), originally developed by Google Inc. and open-sourced in 2012.
- **Recipient / Beneficiary:** Developed for **Samavasaran Jay Kirti Trust**.
- **License Architecture:**
  - Derivative works & modifications: **GNU General Public License v3.0 (GPLv3)**.
  - Upstream legacy files: **Apache License, Version 2.0**.
  - Root `LICENSE` file contains full terms of both GPLv3 and Apache 2.0.
- **Credits Customization:**
  - Both English (`values/credits.xml`) and Hindi (`values-hi/credits.xml`) feature the 7 structured sections:
    1. *About Aadi Panchang Map*
    2. *Developed for Samavasaran Jay Kirti Trust*
    3. *Project Repository*
    4. *Original Project and Engineering*
    5. *Open-Source Acknowledgments*
    6. *Astronomy Data Sources*
    7. *Image and Artwork Credits*
- **Preserved Attributions:**
  - Google team: Brent Bryan, Dominic Widdows, Hector Ouilhet, James Powell, John Taylor, Kevin Serafini, Lior Ron.
  - Community: Pat Stephenson, Chris DiBona, Michael Weiss-Malik.
  - Astronomical Data: CDS Strasbourg, Bright Star Catalogue, HYG Database, NGC/IC, SFA Star Charts, USNO, IMO.
  - Images: NASA/ESA/STScI (Hubble), NASA/JPL/APL/SwRI (Planets & Moon), ESO DSS2 (CC BY 4.0), IAU/Sky & Telescope (CC BY 4.0), Gemini/NOIRLab, Peter Kennett (M31), EHT (Sagittarius A*), NASA/CXC (Cygnus X-1).
  - Google Endorsement Disclaimer: Explicitly disclaimed in `credits.xml`, `README.md`, and store listings.

### 3.7 Play Store Assets & Metadata Audit
- **Store Texts (en-US):**
  - Title: `Aadi Panchang Map` (17 / 30 chars) — OK.
  - Short Description: 66 / 80 chars — OK.
  - Full Description: 1,344 / 4,000 chars — OK.
- **Store Texts (hi-IN):**
  - Title: `Aadi Panchang Map` (17 / 30 chars) — OK.
  - Short Description: 72 / 80 chars — OK.
  - Full Description: 329 / 4,000 chars — OK.
- **App Icon:** `512 × 512 px`, 32-bit PNG with alpha channel (`RGBA`, 319 KB) — Fully matches Google Play specifications.
- **Feature Graphic:** `1795 × 876 px` JPEG. **Non-compliant.** Must be resized to exactly `1024 × 500 px` before upload.
- **Screenshots:**
  - `hi-IN/phoneScreenshots/`: 5 validated 1080 × 1920 24-bit RGB screenshots. Zero duplicates. (Ready for upload).
  - `en-US/phoneScreenshots/`: 6 screenshots. Images 01 & 04 contain alpha channels; images 05 & 06 are duplicates. Requires cleanup before uploading to the English listing.
  - `en-US/sevenInchScreenshots/`: 5 landscape screenshots (1920 × 1080, RGB mode).
  - `en-US/tenInchScreenshots/`: 5 landscape screenshots (1920 × 1080, RGB mode).

---

## 4. Pre-Submission Action Plan

Before uploading to Google Play Console:

1. **Resize Feature Graphic:**
   - Resize `fastlane/metadata/android/en-US/images/featureGraphic.jpeg` from 1795 × 876 px to **1024 × 500 px**.
2. **Clean English Phone Screenshots:**
   - Convert images `01` and `04` in `fastlane/metadata/android/en-US/images/phoneScreenshots/` from RGBA to 24-bit RGB (strip alpha).
   - Remove duplicate screenshot `06-celestial-object-details.png` so 5 distinct screenshots remain.
3. **Configure Geoapify Dashboard:**
   - Log into Geoapify and restrict the API key to package `com.aadipanchang.map` with SHA-256 fingerprint `9B:61:52:73:75:7E:22:A0:F9:5A:BF:5D:AD:BD:97:53:D8:2F:4E:E0:25:30:06:5B:06:4B:10:7A:E1:21:B1:99`.
4. **Follow Checklist:**
   - Use [`PLAY_CONSOLE_FINAL_CHECKLIST.md`](PLAY_CONSOLE_FINAL_CHECKLIST.md) to complete all questionnaires, policy declarations, and metadata uploads.

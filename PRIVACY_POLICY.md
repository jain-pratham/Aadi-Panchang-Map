# Aadi Panchang Map Privacy Policy

**Effective Date:** To be set before publication (Must be finalized prior to public release)

---

## 1. Introduction

Welcome to **Aadi Panchang Map**. 

Aadi Panchang Map is an independent open-source derivative project based on the open-source Sky Map (Stardroid) codebase. This Privacy Policy explains how Aadi Panchang Map accesses, collects, uses, and discloses information when you use the application.

We respect your privacy and design our application to operate with minimal data collection necessary for functionality.

---

## 2. Information We Access

Depending on your device permissions and the specific build variant of the application (Google Play Services / GMS build vs. F-Droid build), Aadi Panchang Map may access the following on-device information:

- **Approximate Geographic Location** (`ACCESS_COARSE_LOCATION`): Used to determine your viewing location for sky rendering.
- **Network State and Internet Access** (`INTERNET`, `ACCESS_NETWORK_STATE`): Used for optional static map tile rendering (Geoapify Maps API) and analytics reporting (GMS build variant only).
- **Device Sensors**: Accesses accelerometer, magnetic field, and rotation vector sensors on-device to dynamically align the star map as you move your device.
- **Device Wake Lock and Vibration**: Used to keep the screen active while viewing the star map and provide tactile feedback for UI interactions.

---

## 3. Location Information

Aadi Panchang Map requires location context to compute and display accurate astronomical positions (such as the local horizon, planet positions, and celestial azimuth/altitude) for your specific point on Earth.

- **On-Device Processing:** Location data accessed by the application is processed locally on your device to update the internal astronomical model (`AstronomerModel`).
- **Approximate Location Only:** The application requests approximate location permission (`ACCESS_COARSE_LOCATION`). It does not request precise GPS location permission (`ACCESS_FINE_LOCATION`).
- **Manual Location Option:** You can choose to deny location permission or manually configure/override your location by entering city names or geographic coordinates within the app settings.
- **No Location Tracking in Analytics:** Geographic latitude and longitude coordinates are **not** intentionally transmitted through the application's explicit analytics events.
- **Optional Static Map Preview:** If a Geoapify API key is configured and you view the Location Management map screen, latitude/longitude coordinates are sent directly to Geoapify static map servers (`maps.geoapify.com`) to display a location preview tile.

---

## 4. Analytics

Aadi Panchang Map handles analytics differently depending on the build flavor of the application:

### A. Google Play Services (GMS) Build Variant
In the GMS version of the application, **analytics collection is enabled by default** and can be disabled by the user at any time. The GMS variant utilizes **Google Firebase Analytics** to collect aggregated usage statistics and technical telemetry to help us understand feature usage, improve stability, and evaluate translation coverage.

Data processed in the GMS variant includes:

1. **Explicit Application Events:**
   - **Feature & Menu Interactions:** Toggling map layers (stars, constellations, planets, grid, horizon, meteor showers), opening menus (settings, credits, help, calibration, time travel, gallery), and onboarding slide views.
   - **Search Queries:** In-app search query terms (e.g., celestial object names entered into the search bar) to measure search success and identify missing catalog items.
   - **App Performance & Sensor Metrics:** Session duration buckets, local startup hour, night mode toggle status, and available device hardware sensors (e.g., presence of gyroscope or rotation vector sensors).
   - **Settings & Preference Changes:** Changes to app preference flags (e.g., auto/manual mode, sound effects).

2. **Automatic Technical Telemetry (Processed by Firebase SDK):**
   - When analytics is enabled, the Google Firebase Analytics SDK automatically processes technical device information including:
     - Firebase App Instance ID (a randomly generated identifier unique to the app installation).
     - Device hardware model and manufacturer.
     - Operating system version and build.
     - Application version and locale/language setting.
     - Screen resolution and device orientation.
     - Coarse city/country location derived from client IP address by Google server infrastructure.

### B. F-Droid Build Variant
The F-Droid version of Aadi Panchang Map is **100% open-source and offline**. It contains **no Firebase Analytics SDK** and utilizes no-op stubs. In the F-Droid build, **zero analytics events or usage metrics are collected or transmitted**.

---

## 5. Analytics Opt-Out

In the GMS build variant, analytics collection is **optional**.

- **Default State:** Analytics is enabled by default in GMS builds.
- **Opt-Out Control:** You can disable analytics data collection at any time by navigating to **App Settings** and turning off the **Usage Statistics / Analytics** option (`enable_analytics`).
- **Effect of Opt-Out:** Turning off analytics invokes `FirebaseAnalytics.setAnalyticsCollectionEnabled(false)`, halting both custom application event logging and automatic telemetry collection by the Firebase SDK.

---

## 6. Information We Do Not Intentionally Collect

Aadi Panchang Map is designed specifically for observing the night sky. The application **does not** intentionally request, process, or collect:

- Personal profile information (such as your real name, email address, phone number, or physical address).
- Contact lists or address book data.
- Financial, payment, or credit card information.
- Photos, media files, or audio recordings.
- User accounts, passwords, or social media profiles.
- Advertising Identifiers (AAID/GAID) — the app does not declare advertising ID permissions.

---

## 7. How Information Is Used

Information accessed or collected by Aadi Panchang Map is used solely for the following purposes:

1. **App Functionality:** To calculate local astronomical object positions and render the night sky map relative to your location.
2. **User Preferences:** To save your display settings, manual location choices, and layer preferences locally on your device.
3. **App Improvements (GMS Build Only):** To analyze aggregated feature usage, diagnose missing sensor hardware issues, and optimize application performance.

---

## 8. Third-Party Services

The application interacts with third-party service providers only as described below:

- **Google Firebase Analytics (GMS Build Only):** Used for aggregated usage analytics in the Play Store version. Data processed by Firebase Analytics is governed by Google's Privacy Policy and standard service terms.
  - *Reference:* [Google Privacy & Terms](https://policies.google.com/privacy) (URL to be verified before publication)
- **Geoapify Maps API (Optional Runtime Service):** Used to load static location map preview tiles in Location Settings. This service is available in both GMS and F-Droid builds only if a Geoapify API key is configured at build time. When active, requests sent to `https://maps.geoapify.com` include center coordinates. If no API key is set (default `"unset"`), no network requests are sent.
- **External Links & Browser Intents:** If you click on external links (such as celestial catalog reference pages, NASA/ESO image source credits, or troubleshooting links), those links open in your device's web browser and are governed by the privacy policies of the respective external websites.

*Note: The application does not contain advertising SDKs (such as AdMob), third-party ad networks, social login SDKs, or third-party crash reporters (such as Crashlytics).*

---

## 9. Data Storage and Retention

- **Local Device Storage:** All user preferences, layer toggles, and manual location coordinates are stored locally on your device using standard Android `SharedPreferences`. This data remains on your device until you uninstall the app or clear the app's data in Android System Settings.
- **Analytics Data Retention:** Analytics metrics collected via Google Firebase in the GMS build are retained in accordance with standard Google Firebase Analytics data retention schedules and settings. The project does not independently control Google's underlying infrastructure retention schedule.

---

## 10. Data Deletion

- **Local Data Deletion:** You can delete all locally stored app settings and saved manual locations at any time by using the **Clear Data / Clear Storage** function in your device's Android System Settings, or by uninstalling the application.
- **Firebase Analytics Data:** Disabling the analytics setting within the app prevents future analytics collection. Because analytics metrics are aggregated and do not contain personal user accounts or email addresses, individual account deletion mechanisms are not applicable.

---

## 11. Children's Privacy

Aadi Panchang Map is a general-audience educational application for viewing celestial bodies. The application is not directed at children under the age of 13, and it does not knowingly collect personal information from children.

---

## 12. Security

We take reasonable technical precautions within the design of Aadi Panchang Map to safeguard your information. Location processing for star mapping occurs on-device, and analytics transmission (in the GMS variant) uses encrypted HTTPS connections. However, please be aware that no method of electronic transmission or storage can be guaranteed to be 100% secure.

---

## 13. Changes to This Privacy Policy

We may update this Privacy Policy from time to time to reflect updates to application functionality, legal requirements, or privacy practices. Any updates will be posted in this document with an updated effective date.

---

## 14. Contact Us

If you have any questions, concerns, or inquiries regarding this Privacy Policy or the privacy practices of Aadi Panchang Map, please contact us by email at:

**Official Support Contact:** `jainpratham4050@gmail.com`

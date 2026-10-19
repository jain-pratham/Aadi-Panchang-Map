# Aadi Panchang Map Privacy Policy

**Effective Date:** October 19, 2026

---

## 1. Introduction

Welcome to **Aadi Panchang Map**. 

Aadi Panchang Map is an independent open-source derivative project based on the open-source Sky Map (Stardroid) codebase. This Privacy Policy explains how Aadi Panchang Map accesses, collects, uses, and discloses information when you use the application.

We respect your privacy and design our application to operate with minimal data collection necessary for functionality.

---

## 2. Information We Access

Depending on your device permissions and the specific build variant of the application (Google Play Services / GMS build vs. F-Droid build), Aadi Panchang Map may access the following on-device information:

- **Approximate Geographic Location** (`ACCESS_COARSE_LOCATION`): Used to determine your viewing location for sky rendering.
- **Network State and Internet Access** (`INTERNET`, `ACCESS_NETWORK_STATE`): Used for optional static map tile rendering (Geoapify Maps API).
- **Device Sensors**: Accesses accelerometer, magnetic field, and rotation vector sensors on-device to dynamically align the star map as you move your device.
- **Device Wake Lock and Vibration**: Used to keep the screen active while viewing the star map and provide tactile feedback for UI interactions.

---

## 3. Location Information

Aadi Panchang Map requires location context to compute and display accurate astronomical positions (such as the local horizon, planet positions, and celestial azimuth/altitude) for your specific point on Earth.

- **On-Device Processing:** Location data accessed by the application is processed locally on your device to update the internal astronomical model (`AstronomerModel`).
- **Approximate Location Only:** The application requests approximate location permission (`ACCESS_COARSE_LOCATION`). It does not request precise GPS location permission (`ACCESS_FINE_LOCATION`).
- **Manual Location Option:** You can choose to deny location permission or manually configure/override your location by entering city names or geographic coordinates within the app settings.
- **No Location Tracking:** No analytics or telemetry SDKs exist to record, transmit, or monitor your location.
- **Optional Static Map Preview:** If a Geoapify API key is configured and you view the Location Management map screen, latitude/longitude coordinates are sent directly to Geoapify static map servers (`maps.geoapify.com`) to display a location preview tile. If no API key is configured (the default build setting), no map tile requests are made.

---

## 4. Analytics

Aadi Panchang Map contains **no analytics SDKs and performs no telemetry tracking** in either the Google Play Services (GMS) or F-Droid build variants.

- **Firebase Analytics Removed:** The Google Firebase Analytics SDK and all telemetry logging have been completely removed.
- **Zero Usage Statistics:** The application does not collect, record, or transmit usage statistics, feature interaction metrics, search queries, session durations, or device sensor metrics.
- **No Analytics App Instance ID:** The application does not generate or retrieve an analytics app instance ID or advertising identifier.
- **Both Build Variants:** Both the Google Play Services (GMS) variant and the F-Droid variant operate with zero analytics tracking.

---

## 5. Analytics Settings

Because all analytics collection and telemetry code have been completely removed from both build variants, no analytics toggles, opt-in/opt-out preferences, or usage tracking controls are present or needed in the application.

---

## 6. Information We Do Not Intentionally Collect

Aadi Panchang Map is designed specifically for observing the night sky. The application **does not** intentionally request, process, or collect:

- Personal profile information (such as your real name, email address, phone number, or physical address).
- Contact lists or address book data.
- Financial, payment, or credit card information.
- Photos, media files, or audio recordings.
- User accounts, passwords, or social media profiles.
- Advertising Identifiers (AAID/GAID) — the app does not declare or use advertising ID permissions (`com.google.android.gms.permission.AD_ID`).
- Search queries, layer toggles, menu interactions, or user preferences to any analytics server.

---

## 7. How Information Is Used

Information accessed by Aadi Panchang Map is used solely for the following purposes:

1. **App Functionality:** To calculate local astronomical object positions and render the night sky map relative to your location.
2. **User Preferences:** To save your display settings, manual location choices, and layer preferences locally on your device.

---

## 8. Third-Party Services

The application interacts with third-party service providers only as described below:

- **Geoapify Maps API (Optional Runtime Service):** Used to load static location map preview tiles in Location Settings. This service is available in both GMS and F-Droid builds only if a Geoapify API key is configured at build time. When active, requests sent to `https://maps.geoapify.com` include center coordinates. If no API key is set (default `"unset"`), no network requests are sent.
- **External Links & Browser Intents:** If you click on external links (such as celestial catalog reference pages, NASA/ESO image source credits, or troubleshooting links), those links open in your device's web browser and are governed by the privacy policies of the respective external websites.

*Note: The application does not contain advertising SDKs (such as AdMob), analytics SDKs, remote configuration SDKs (such as Firebase Remote Config), third-party ad networks, social login SDKs, or third-party crash reporters (such as Crashlytics).*

---

## 9. Data Storage and Retention

- **Local Device Storage:** All user preferences, layer toggles, and manual location coordinates are stored locally on your device using standard Android `SharedPreferences`. This data remains on your device until you uninstall the app or clear the app's data in Android System Settings.

---

## 10. Data Deletion

- **Local Data Deletion:** You can delete all locally stored app settings, cached parameters, and saved manual locations at any time by using the **Clear Data / Clear Storage** function in your device's Android System Settings, or by uninstalling the application.
- **No Cloud User Accounts:** Because the application does not collect analytics metrics, user accounts, or cloud profiles, there is no remote user data stored on our servers to delete.

---

## 11. Children's Privacy

Aadi Panchang Map is a general-audience educational application for viewing celestial bodies. The application is not directed at children under the age of 13, and it does not knowingly collect personal information from children.

---

## 12. Security

We take reasonable technical precautions within the design of Aadi Panchang Map to safeguard your information. Location processing for star mapping occurs on-device, and third-party network communications (Geoapify Maps API) use encrypted HTTPS connections. However, please be aware that no method of electronic transmission or storage can be guaranteed to be 100% secure.

---

## 13. Changes to This Privacy Policy

We may update this Privacy Policy from time to time to reflect updates to application functionality, legal requirements, or privacy practices. Any updates will be posted in this document with an updated effective date.

---

## 14. Contact Us

If you have any questions, concerns, or inquiries regarding this Privacy Policy or the privacy practices of Aadi Panchang Map, please contact us by email at:

**Official Support Contact:** `jainpratham4050@gmail.com`

# Aadi Panchang Map — Google Play Console Final Preparation Checklist

This document is the operational checklist for publishing **Aadi Panchang Map** (`com.aadipanchang.map`) to Google Play Console. It outlines every required field, asset, declaration, and procedure.

---

## 1. Quick Reference & Core Identifiers

| Parameter | Current Verified Value | Source in Project | Play Console Destination | Status |
|---|---|---|---|---|
| **App Name** | `Aadi Panchang Map` | `strings.xml` / `title.txt` | Dashboard > Main Store Listing | **READY** |
| **Package Name / Application ID** | `com.aadipanchang.map` | `app/build.gradle` (`defaultConfig`) | Set on first AAB upload | **READY** |
| **Version Code** | `1670` | `app/build.gradle` | Release Bundle metadata | **READY** |
| **Version Name** | `1.15.0:Saturn` | `app/build.gradle` | Release Notes / User-facing | **READY** |
| **Minimum SDK** | `API 26` (Android 8.0 Oreo) | `app/build.gradle` (`minSdk 26`) | Enforced by Manifest / AAB | **READY** |
| **Target SDK** | `API 36` (Android 16) | `app/build.gradle` (`targetSdk 36`) | Enforced by Manifest / AAB | **READY** |
| **Compile SDK** | `API 35` (Android 15) | `app/build.gradle` (`compileSdk 35`) | Build toolchain | **READY** |
| **Default Language** | English (`en-US`) | `fastlane/metadata/android/` | App Setup > Default Language | **READY** |
| **Secondary Language** | Hindi (`hi-IN`) | `fastlane/metadata/android/hi-IN` | Store Listing Translations | **READY** |
| **Production AAB Path** | `app/build/outputs/bundle/gmsRelease/app-gms-release.aab` | Build Output (`bundleGmsRelease`) | App Bundle upload zone | **READY** |
| **AAB SHA-256 Checksum** | `0e3dab60fb9c02e4ab9fb344bd73c0cf9260b5552a2b2d1990a21c7af8ce431e` | Generated Artifact | Release integrity check | **READY** |
| **Upload Key Fingerprint (SHA-256)** | `9B:61:52:73:75:7E:22:A0:F9:5A:BF:5D:AD:BD:97:53:D8:2F:4E:E0:25:30:06:5B:06:4B:10:7A:E1:21:B1:99` | Keystore Certificate | Play App Signing match | **READY** |
| **Privacy Policy URL** | `https://github.com/jain-pratham/Aadi-Panchang-Map/blob/master/PRIVACY_POLICY.md` | `PRIVACY_POLICY.md` (HTTP 200 verified) | App Content > Privacy Policy | **READY** |

---

## 2. Complete Play Console Section Checklist

| # | Play Console Section | Required Information / Asset | Current Verified Value | Source in Project | What the User Must Do Manually | Status |
|---|---|---|---|---|---|---|
| **A.1** | App Information | App Name | `Aadi Panchang Map` | `en-US/title.txt` | Enter or confirm name | **READY** |
| **A.2** | App Information | Default Language | English (United States) — `en-US` | fastlane metadata | Select `English (United States)` | **READY** |
| **A.3** | App Information | App or Game | App | App design | Select `App` | **READY** |
| **A.4** | App Information | Free or Paid | Free | `README.md` / `credits.xml` | Select `Free` (cannot be changed after publish) | **READY** |
| **A.5** | App Information | Category | `Education` or `Books & Reference` | Domain functionality | Select `Education` | **READY** |
| **A.6** | App Information | Developer Email | `jainpratham4050@gmail.com` | `PRIVACY_POLICY.md` | Enter official developer email | **READY** |
| **A.7** | App Information | Developer Phone | Unknown (Personal) | N/A | Enter developer phone number (required if verified organization or standard console verification) | **NEEDS ACTION** |
| **A.8** | App Information | Developer Website | `https://github.com/jain-pratham/Aadi-Panchang-Map` | Project Repository | Enter repository URL | **READY** |
| **B.1** | Store Listing | Short Description (en-US) | `Aadi Panchang Map turns your phone into a window on the night sky.` (66/80 chars) | `en-US/short_description.txt` | Copy & paste into field | **READY** |
| **B.2** | Store Listing | Full Description (en-US) | 1,344 characters (Features, Nakshatras, Open Source) | `en-US/full_description.txt` | Copy & paste into field | **READY** |
| **B.3** | Store Listing | Short Description (hi-IN) | `Aadi Panchang Map आपके फ़ोन को रात के आकाश की एक खिड़की में बदल देता है।` (72/80 chars) | `hi-IN/short_description.txt` | Add Hindi language and paste | **READY** |
| **B.4** | Store Listing | Full Description (hi-IN) | 329 characters (Localized overview & attribution) | `hi-IN/full_description.txt` | Paste into Hindi translation | **READY** |
| **B.5** | Store Listing | App Icon | 512 × 512 px PNG with alpha (319 KB) | `en-US/images/icon.png` | Upload file | **READY** |
| **B.6** | Store Listing | Feature Graphic | 1795 × 876 px JPEG (Needs 1024 × 500 px) | `en-US/images/featureGraphic.jpeg` | Resize or crop image to exactly `1024 × 500 px` before upload | **NEEDS ACTION** |
| **B.7** | Store Listing | Phone Screenshots (en-US) | 6 screenshots (1080 × 1920) | `en-US/images/phoneScreenshots/` | Remove alpha from images 01 & 04; remove duplicate 06 | **NEEDS ACTION** |
| **B.8** | Store Listing | Phone Screenshots (hi-IN) | 5 screenshots (1080 × 1920, RGB mode, no alpha) | `hi-IN/images/phoneScreenshots/` | Upload all 5 files directly to Hindi listing | **READY** |
| **B.9** | Store Listing | 7-inch Tablet Screenshots | 5 screenshots (1920 × 1080, RGB mode) | `en-US/images/sevenInchScreenshots/` | Upload to 7-inch tablet section | **READY** |
| **B.10** | Store Listing | 10-inch Tablet Screenshots | 5 screenshots (1920 × 1080, RGB mode) | `en-US/images/tenInchScreenshots/` | Upload to 10-inch tablet section | **READY** |
| **B.11** | Store Listing | Chromebook Screenshots | 5 screenshots (1920 × 1080, RGB mode) | `en-US/images/chromebookScreenshots/` | Upload to Chromebook section (optional) | **READY** |
| **C.1** | App Content | Privacy Policy | `https://github.com/jain-pratham/Aadi-Panchang-Map/blob/master/PRIVACY_POLICY.md` | Root repository file | Enter URL in Console | **READY** |
| **C.2** | App Content | Ads Declaration | No Ads (`resValue analytics_enabled=false`, zero ad SDKs) | Verified from code | Select "No, my app does not contain ads" | **READY** |
| **C.3** | App Content | App Access | All functionality is available without credentials or subscription | Zero auth walls | Select "All functionality is available without special access" | **READY** |
| **C.4** | App Content | Content Rating | IARC Questionnaire | Astronomy / Educational app | Complete IARC questionnaire (Expect Everyone / PEGI 3 / USK 0) | **NEEDS ACTION** |
| **C.5** | App Content | Target Audience & Content | General audience (Age 13+) | `PRIVACY_POLICY.md` Sec. 11 | Select age groups: `13-15`, `16-17`, `18 and over`. Declare not designed for children | **READY** |
| **C.6** | App Content | News Apps | Not a news app | App nature | Select "No" | **READY** |
| **C.7** | App Content | COVID-19 Tracing / Status | Not related | App nature | Select "No" | **READY** |
| **C.8** | App Content | Data Safety Form | Approximate Location shared ephemerally with Geoapify; Zero telemetry | Source code audit | Complete Data Safety wizard per Section 3 below | **NEEDS ACTION** |
| **C.9** | App Content | Government Apps | Not a government app | Organization nature | Select "No" | **READY** |
| **C.10** | App Content | Financial Features | No financial features | App nature | Select "My app doesn't provide any financial features" | **READY** |
| **C.11** | App Content | Health Apps | Not a health app | App nature | Select "My app isn't a health app" | **READY** |
| **D.1** | Release & Signing | Play App Signing | Enabled (Google manages signing key; developer uploads with upload key) | Keystore configuration | Enroll in Play App Signing; upload `app-gms-release.aab` | **READY** |
| **D.2** | Release & Signing | Release Track | Closed Testing Track (or Internal) | Play Console policy | For personal accounts created after Nov 13, 2023: run 14-day test with 12 testers | **NEEDS ACTION** |
| **D.3** | Release & Signing | Release Notes (en-US) | `Welcome to Aadi Panchang Map! Explore the night sky, planets, stars, constellations, and Vedic Nakshatras.` | `en-US/changelogs/default.txt` | Paste into Release Notes field | **READY** |
| **D.4** | Release & Signing | Release Notes (hi-IN) | `Aadi Panchang Map में आपका स्वागत है! रात के आकाश, ग्रहों, तारों, नक्षत्रों और खगोलीय पिंडों का अन्वेषण करें।` | `hi-IN/changelogs/default.txt` | Paste into Hindi Release Notes | **READY** |
| **E.1** | External Services | Geoapify API Key Restriction | Configured in `no-checkin.properties` | `app/no-checkin.properties` | Verify HTTP Referrer / Android package restriction in Geoapify Dashboard | **NEEDS ACTION** |

---

## 3. Google Play Data Safety Form Answers (Verified from Code)

Complete the **Data Safety** questionnaire with the following verified selections:

### A. Data Collection and Security
1. **Does your app collect or share any of the required user data types?**
   - **Answer:** **Yes** (because approximate location is sent to Geoapify for static map preview).
2. **Is all of the user data collected by your app encrypted in transit?**
   - **Answer:** **Yes** (HTTPS is enforced for all Geoapify requests).
3. **Do you provide a way for users to request that their data be deleted?**
   - **Answer:** **No** (data sent to Geoapify is ephemeral and not stored; no user accounts exist).

### B. Data Types Declaration: Location
Under the **Location** category:
- Check **Approximate location** (`ACCESS_COARSE_LOCATION`).
- Leave *Precise location* unchecked (`ACCESS_FINE_LOCATION` is not used).

For **Approximate location**, answer the specific questions:
- **Is this data collected, shared, or both?**
  - Select: **Shared** (transmitted to third party `maps.geoapify.com` for map preview).
- **Is this data processed ephemerally?**
  - Select: **Yes** (used only in real time to return the map tile; not stored remotely).
- **Is this data required for your app, or can users choose whether it's collected?**
  - Select: **Data collection is optional** (user can deny location or type manual coordinates).
- **Why is this user data collected/shared?**
  - Check: **App functionality** (displays geographic location context).

### C. All Other Categories
- **Personal info:** Not collected / Not shared.
- **Financial info:** Not collected / Not shared.
- **Health and fitness:** Not collected / Not shared.
- **Messages / Photos / Audio / Files:** Not collected / Not shared.
- **App activity:** Not collected / Not shared (Firebase Analytics has been completely removed).
- **App info and performance (Diagnostics/Crash logs):** Not collected / Not shared.
- **Device or other IDs:** Not collected / Not shared (No Advertising ID / No AAID).

---

## 4. Manual Information Required from Developer

Before publishing, you must personally provide or verify these values in Google Play Console:
1. **Developer Contact Information:**
   - Legal Name / Developer Name.
   - Contact email: `jainpratham4050@gmail.com`.
   - Contact phone number (with country code, e.g. `+91 ...`).
   - Physical address (required by Google for paid apps or merchant accounts, or European consumer protection disclosures).
2. **Account Type & Testing Requirements:**
   - Check whether your Google Play Developer Account is a **Personal** or **Organization** account.
   - If created after **November 13, 2023** as a personal account: You **must** invite at least **12 testers** to participate in a **Closed Test** for at least **14 consecutive days** before requesting production access.
3. **Geoapify API Key Restriction:**
   - Log into your [Geoapify MyProjects Dashboard](https://myprojects.geoapify.com/).
   - Ensure the API key used in `app/no-checkin.properties` is restricted to Android application package `com.aadipanchang.map` with upload certificate SHA-256 fingerprint:
     `9B:61:52:73:75:7E:22:A0:F9:5A:BF:5D:AD:BD:97:53:D8:2F:4E:E0:25:30:06:5B:06:4B:10:7A:E1:21:B1:99`

---

## 5. Step-by-Step Google Play Console Submission Procedure

Follow these sequential steps in Google Play Console:

### Step 1: Create the Application
1. Go to [Google Play Console](https://play.google.com/console).
2. Click **Create app**.
3. Enter App name: `Aadi Panchang Map`.
4. Default language: `English (United States) - en-US`.
5. App or game: `App`.
6. Free or paid: `Free`.
7. Accept the Declarations and click **Create app**.

### Step 2: Set Up App Content & Policies
1. In the left menu, scroll to **Policy and programs** > **App content**.
2. **Privacy policy:** Enter `https://github.com/jain-pratham/Aadi-Panchang-Map/blob/master/PRIVACY_POLICY.md` and save.
3. **Ads:** Select "No, my app does not contain ads" and save.
4. **App access:** Select "All functionality is available without special access" and save.
5. **Content ratings:** Complete the questionnaire (Educational, no violence/gambling/chat). Save and receive IARC certificate.
6. **Target audience:** Select 13-15, 16-17, 18+. Confirm not designed for children.
7. **News apps:** Select "No".
8. **COVID-19:** Select "My app is not a publicly available COVID-19 app".
9. **Data safety:** Complete the form using the exact answers from Section 3 above.
10. **Government apps:** Select "No".
11. **Financial features:** Select "My app doesn't provide any financial features".

### Step 3: Main Store Listing Setup
1. In the left menu, select **Store presence** > **Main store listing**.
2. Paste Short description from `fastlane/metadata/android/en-US/short_description.txt`.
3. Paste Full description from `fastlane/metadata/android/en-US/full_description.txt`.
4. Upload App icon: `fastlane/metadata/android/en-US/images/icon.png` (512 × 512).
5. Upload Feature graphic: Resize `fastlane/metadata/android/en-US/images/featureGraphic.jpeg` to **1024 × 500 px** and upload.
6. Upload Phone screenshots:
   - For Hindi (`hi-IN`), upload the 5 validated screenshots from `fastlane/metadata/android/hi-IN/images/phoneScreenshots/`.
   - For English, upload the cleaned 1080 × 1920 RGB screenshots.
7. Upload Tablet screenshots from `sevenInchScreenshots` and `tenInchScreenshots`.
8. Under **Manage translations**, add **Hindi (India) - hi-IN** and paste the text from `fastlane/metadata/android/hi-IN/`.

### Step 4: Create and Roll Out Release
1. In the left menu, select **Testing** > **Closed testing** (or **Production** if account is eligible).
2. Click **Create new release**.
3. Under App bundles, upload:
   `c:\Projects\Freelancer\Sky Map_Original\stardroid-v1\app\build\outputs\bundle\gmsRelease\app-gms-release.aab`
4. Release name will automatically populate as `1670 (1.15.0:Saturn)`.
5. Under Release notes, paste the text from `fastlane/metadata/android/en-US/changelogs/default.txt`.
6. Click **Next** to run Google Play's automated pre-launch checks.
7. Review validation results and click **Save**.
8. If on a Closed Testing track, recruit 12 testers, distribute the opt-in link, and maintain active testing for 14 days before submitting for production review.

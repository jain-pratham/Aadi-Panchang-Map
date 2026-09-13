# Aadi Panchang Map

An Android application for viewing stars, constellations, planets, and celestial bodies.

## About

**Aadi Panchang Map** is an independent open-source derivative project based on the original open-source Sky Map (Stardroid) codebase. The original Sky Map project was developed by Google Inc. and open sourced in 2012. Aadi Panchang Map is independently branded and developed as a derivative project. It is not affiliated with, endorsed by, or authorized by Google Inc. or the original Sky Map maintainers.

- **Application ID:** `com.aadipanchang.map`
- **Minimum Android Version:** Android 8.0 (API Level 26)
- **Target Android Version:** Android 15 (API Level 36)

## Features

- Dynamic sensor-driven sky map tracking (accelerometer, compass, gyroscope).
- Interactive manual mode with pan, pinch-to-zoom, and rotation gestures.
- Toggleable celestial layers: Stars, Constellations, Planets, Deep Sky Objects, Grid, Horizon, and Meteor Showers.
- In-app search for stars, planets, constellations, and deep-sky objects with directional targeting.
- Time travel feature to view historical or future night skies (1900–2100).
- Night vision mode for dark-adapted night sky observation.
- Detailed info cards and image gallery for celestial bodies.

## Building

Aadi Panchang Map can be built using the standard Gradle wrapper. JDK 17 is required for compilation.

### Build Variants

The project includes two product flavors:
- **GMS Flavor (`gms`)**: Includes Google Play Services and Google Firebase Analytics (with user opt-out support).
- **F-Droid Flavor (`fdroid`)**: 100% open-source and offline build variant without Firebase Analytics SDKs or network tracking calls.

### Build Commands

To build the debug APKs for both flavors:

**Linux / macOS:**
```bash
./gradlew assembleGmsDebug
./gradlew assembleFdroidDebug
```

**Windows (PowerShell / Command Prompt):**
```cmd
gradlew.bat assembleGmsDebug
gradlew.bat assembleFdroidDebug
```

## Testing

Run unit tests using the standard Gradle test task:

**Linux / macOS:**
```bash
./gradlew test
```

**Windows:**
```cmd
gradlew.bat test
```

## License

Aadi Panchang Map is distributed under a split open-source licensing structure:

- **GNU General Public License v3.0 (GPLv3)** applies to current and derivative project modifications.
- **Apache License 2.0** applies to legacy components originally developed by Google Inc. carrying original Apache 2.0 license headers.

The full legal terms and conditions are documented in the root [`LICENSE`](LICENSE) file.

## Third-Party Attributions

Aadi Panchang Map incorporates data and media assets from third-party astronomical catalogs, research institutions, and space agencies, including:
- Centre de Données astronomiques de Strasbourg (CDS)
- Bright Star Catalogue, HYG Database, NGC/IC Catalogues
- US Naval Observatory (USNO) & International Meteor Organization (IMO)
- Imagery courtesy of NASA, ESA, STScI, ESO (CC BY 4.0), IAU/Sky & Telescope (CC BY 4.0), and NOIRLab.

Detailed credits and attribution listings are available within the app menu and [`app/src/main/res/values/credits.xml`](app/src/main/res/values/credits.xml).

## Privacy

The project privacy policy is documented in [`PRIVACY_POLICY.md`](PRIVACY_POLICY.md). Aadi Panchang Map does not collect personal profile information. Location data accessed by the application is processed on-device for astronomy rendering and is not transmitted in analytics events.

## Source Code

Source code is provided directly within this repository.

*(PUBLIC REPOSITORY URL TO BE ADDED BEFORE PUBLICATION)*

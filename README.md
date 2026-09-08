# Tabataki

**Deutsch** | [English](#english)

Aktueller Release: **v1.0.14**

Tabataki ist eine Android-App für Intervall- und Tabata-Training. Das Projekt wurde als Lern- und Praxisprojekt entwickelt, um Erfahrungen mit Kotlin, Jetpack Compose, Android-Entwicklung, lokaler Datenspeicherung und automatisierten GitHub-Workflows zu sammeln.

Die App befindet sich weiterhin in Entwicklung. Signierte APK-Dateien werden über GitHub Releases für Tests und die direkte Installation auf Android-Geräten bereitgestellt.

## Funktionen

- konfigurierbarer Intervall-Timer
- Arbeits-, Pausen- und Rundeneinstellungen
- Trainingstage und Routinen
- eigener Übungskatalog
- eigene Übungen und Kategorien
- lokale Speicherung ohne Benutzerkonto
- Import und Export von Trainingsdaten als JSON
- akustische Signale bei Phasenwechseln
- mehrsprachige Benutzeroberfläche
- dunkles, für das Training optimiertes Design
- eigenes Tabataki-App-Icon

## Technik

- Kotlin
- Jetpack Compose
- Material 3
- Room
- DataStore
- Gradle
- GitHub Actions für Tests, Android Lint, Builds und Releases

## Installation

Die aktuelle signierte APK kann über den Bereich **Releases** dieses Repositories heruntergeladen werden.

Aktueller Release: **Tabataki v1.0.14**

> Tabataki befindet sich noch in Entwicklung. Releases dienen derzeit vor allem zum Testen der App auf echten Android-Geräten.

Je nach Android- und Play-Protect-Einstellungen kann die Installation von APK-Dateien außerhalb des Google Play Stores eingeschränkt oder zusätzlich bestätigt werden müssen.

Bei einem Update von einer bereits installierten, mit demselben Release-Schlüssel signierten Tabataki-Version kann die neue APK in der Regel direkt über die vorhandene Installation installiert werden.

## Lokal entwickeln

1. Repository klonen.
2. Repository in Android Studio öffnen.
3. Gradle-Abhängigkeiten synchronisieren.
4. App auf einem Emulator oder Android-Gerät starten.

Das Android-Projekt liegt direkt in der Repository-Wurzel.

Lokale Prüfungen können zum Beispiel mit folgendem Befehl ausgeführt werden:

```powershell
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest
```

## Datenschutz

Tabataki benötigt für die lokale Nutzung kein Benutzerkonto und fordert derzeit keine zusätzlichen Android-Berechtigungen wie Standort, Kamera, Mikrofon oder Kontakte an.

Trainingsdaten und Einstellungen werden lokal auf dem Gerät gespeichert. Tabataki verwendet derzeit keinen eigenen Server und überträgt diese Daten nicht an einen eigenen Online-Dienst.

Android-Cloud-Backups und die Übernahme der App-Daten über die von Android bereitgestellten Backup- und Geräteübertragungsmechanismen sind bewusst deaktiviert bzw. ausgeschlossen.

## Qualitätssicherung

GitHub Actions prüft den aktuellen Entwicklungsstand automatisiert. Dazu gehören unter anderem:

- Unit-Tests
- Android Lint
- Debug-APK-Build
- Kompilierung des Instrumentation-Test-APKs
- signierter Release-Build bei neuen Versionen

## Status

**In Entwicklung.**

Die App ist als installierbare Android-APK verfügbar, sollte aktuell aber weiterhin als Test- und Entwicklungsversion betrachtet werden.

## Feedback

**Für Feedback wäre ich sehr dankbar.**

Fehler, Verbesserungsvorschläge und Ideen können über die [GitHub Issues](https://github.com/scncvk19/tabataki-android/issues) eingereicht werden.

Da Tabataki ein Lernprojekt ist, hilft konstruktives Feedback dabei, die App technisch und funktional weiterzuentwickeln.

## Nutzungsrechte

Der Quellcode ist zur Ansicht und Bewertung veröffentlicht. Es wird keine Erlaubnis zur Übernahme, Veränderung oder Weiterverteilung erteilt. Einzelheiten stehen in [COPYRIGHT.md](COPYRIGHT.md).

---

<a id="english"></a>

# English

[Deutsch](#tabataki) | **English**

Current release: **v1.0.14**

Tabataki is an Android app for interval and Tabata training. The project was created as a learning and hands-on development project to gain practical experience with Kotlin, Jetpack Compose, Android development, local data storage, and automated GitHub workflows.

The app is still under development. Signed APK files are provided through GitHub Releases for testing and direct installation on Android devices.

## Features

- configurable interval timer
- work, rest, and round settings
- workout days and routines
- built-in exercise catalog
- custom exercises and categories
- local storage without a user account
- JSON import and export of training data
- audio signals when training phases change
- multilingual user interface
- dark training-focused design
- custom Tabataki app icon

## Technology

- Kotlin
- Jetpack Compose
- Material 3
- Room
- DataStore
- Gradle
- GitHub Actions for tests, Android Lint, builds, and releases

## Installation

The latest signed APK can be downloaded from the **Releases** section of this repository.

Current release: **Tabataki v1.0.14**

> Tabataki is still under development. Releases are currently intended mainly for testing the app on real Android devices.

Depending on Android and Play Protect settings, installing APK files outside the Google Play Store may be restricted or require additional confirmation.

When updating an already installed Tabataki version signed with the same release key, the new APK can normally be installed directly over the existing installation.

## Local development

1. Clone the repository.
2. Open the repository in Android Studio.
3. Sync the Gradle dependencies.
4. Run the app on an emulator or Android device.

The Android project is located directly in the repository root.

Local checks can be run, for example, with:

```powershell
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest
```

## Privacy

Tabataki does not require a user account for local use and currently does not request additional Android permissions such as location, camera, microphone, or contacts.

Training data and settings are stored locally on the device. Tabataki currently does not use its own server and does not transmit this data to its own online service.

Android cloud backups and app-data transfer through Android's backup and device-transfer mechanisms are intentionally disabled or excluded.

## Quality assurance

GitHub Actions automatically checks the current development state. These checks include:

- unit tests
- Android Lint
- debug APK build
- instrumentation-test APK compilation
- signed release build for new versions

## Status

**In development.**

The app is available as an installable Android APK, but it should currently still be considered a testing and development version.

## Feedback

**Feedback is very welcome.**

Bugs, improvement suggestions, and ideas can be submitted through [GitHub Issues](https://github.com/scncvk19/tabataki-android/issues).

Because Tabataki is a learning project, constructive feedback helps improve the app both technically and functionally.

## Usage rights

The source code is published for viewing and evaluation only. No permission is granted to copy, modify, or redistribute it. See [COPYRIGHT.md](COPYRIGHT.md) for details.

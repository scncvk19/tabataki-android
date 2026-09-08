# Tabataki

🇩🇪 [Deutsch](#deutsch)  
🇬🇧 [English](#english)

---

<a id="deutsch"></a>

# Deutsch

Tabataki ist eine Android-App für Intervall- und Tabata-Training. Das Projekt wurde als Lern- und Praxisprojekt entwickelt und verbindet Kotlin, Jetpack Compose, lokale Datenspeicherung sowie automatisierte Tests und Releases über GitHub Actions.

Die App befindet sich weiterhin in Entwicklung, ist aber bereits als signierte APK für Android verfügbar.

## Download

**Aktuelle Version:** `v1.0.15`  
**APK:** `Tabataki-1.0.15.apk`

- [Aktuellen Release öffnen](https://github.com/scncvk19/tabataki-android/releases/latest)
- [Tabataki-1.0.15.apk direkt herunterladen](https://github.com/scncvk19/tabataki-android/releases/download/v1.0.15/Tabataki-1.0.15.apk)

**SHA-256:**

```text
f4e17894427e1be314f029ca92ed1a76e5f9b4f3a74914c2fb10932ddf12e388
```

> Tabataki wird derzeit außerhalb des Google Play Stores über GitHub Releases verteilt. Je nach Android- und Play-Protect-Einstellungen kann die Installation einer APK aus externen Quellen eine zusätzliche Bestätigung erfordern.

Wenn bereits eine ältere Tabataki-Version installiert ist, die mit demselben Release-Schlüssel signiert wurde, kann die neue APK normalerweise direkt als Update installiert werden. Vor einer Deinstallation empfiehlt sich ein manueller JSON-Export der eigenen Trainingsdaten.

## Funktionen

- konfigurierbarer Intervall-Timer
- Arbeits-, Pausen- und Rundeneinstellungen
- Trainingstage und Routinen
- integrierter Übungskatalog
- eigene Übungen und Kategorien
- lokale Speicherung ohne Benutzerkonto
- Import und Export von Trainingsdaten als JSON
- akustische Signale bei Phasenwechseln
- mehrsprachige Benutzeroberfläche
- dunkles, trainingsorientiertes Design
- eigenes Tabataki-App-Icon

## Technik

- Kotlin
- Jetpack Compose
- Material 3
- Room
- DataStore
- Gradle
- GitHub Actions

## Lokale Entwicklung

1. Repository klonen.
2. Repository in Android Studio öffnen.
3. Gradle-Abhängigkeiten synchronisieren.
4. App auf einem Emulator oder Android-Gerät starten.

Das Android-Projekt liegt direkt in der Repository-Wurzel.

Für die lokalen Qualitätsprüfungen kann unter Windows PowerShell beispielsweise folgender Befehl verwendet werden:

```powershell
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest
```

## Qualitätssicherung

GitHub Actions prüft Änderungen automatisiert. Dazu gehören derzeit:

- Unit-Tests
- Android Lint
- Debug-APK-Build
- Kompilierung des Instrumentation-Test-APKs
- signierter Release-Build bei neuen Versionen

Neue Releases werden automatisiert erstellt und mit dem hinterlegten Release-Schlüssel signiert. Der private Signierschlüssel ist nicht Bestandteil des Repository-Quellcodes.

## Datenschutz

Tabataki benötigt für die lokale Nutzung kein Benutzerkonto und fordert derzeit keine zusätzlichen Android-Berechtigungen wie Standort, Kamera, Mikrofon oder Kontakte an.

Trainingsdaten und Einstellungen werden lokal auf dem Gerät gespeichert. Tabataki verwendet derzeit keinen eigenen Backend-Server für diese Daten und überträgt sie nicht an einen eigenen Online-Dienst.

Android-Cloud-Backups sowie die Übernahme von App-Daten über die von Android bereitgestellten Backup- und Geräteübertragungsmechanismen sind bewusst deaktiviert bzw. ausgeschlossen. Für eine bewusste Sicherung steht der manuelle JSON-Export zur Verfügung.

## Projektstatus

**In Entwicklung.**

Die App ist als installierbare Android-APK verfügbar. Releases sollten derzeit weiterhin als Entwicklungs- und Testversionen betrachtet werden.

## Feedback

Fehler, Verbesserungsvorschläge und Ideen sind willkommen und können über [GitHub Issues](https://github.com/scncvk19/tabataki-android/issues) gemeldet werden.

Konstruktives Feedback hilft dabei, Tabataki technisch und funktional weiterzuentwickeln.

## Nutzungsrechte

Dieses Repository ist **nicht als Open-Source-Projekt lizenziert**. Der Quellcode wird zur Ansicht und Bewertung bereitgestellt. Ohne vorherige schriftliche Erlaubnis des Rechteinhabers wird keine Erlaubnis zur Übernahme, Veränderung, Weiterverteilung oder Nutzung in anderen Projekten erteilt.

Weitere Informationen stehen in [COPYRIGHT.md](COPYRIGHT.md). Abhängigkeiten von Drittanbietern unterliegen ihren jeweiligen Lizenzen.

---

<a id="english"></a>

# English

Tabataki is an Android app for interval and Tabata training. It was created as a learning and hands-on development project combining Kotlin, Jetpack Compose, local data storage, and automated testing and releases with GitHub Actions.

The app is still under development, but a signed Android APK is already available.

## Download

**Current version:** `v1.0.15`  
**APK:** `Tabataki-1.0.15.apk`

- [Open the latest release](https://github.com/scncvk19/tabataki-android/releases/latest)
- [Download Tabataki-1.0.15.apk directly](https://github.com/scncvk19/tabataki-android/releases/download/v1.0.15/Tabataki-1.0.15.apk)

**SHA-256:**

```text
f4e17894427e1be314f029ca92ed1a76e5f9b4f3a74914c2fb10932ddf12e388
```

> Tabataki is currently distributed outside the Google Play Store through GitHub Releases. Depending on Android and Play Protect settings, installing an APK from an external source may require additional confirmation.

If an older Tabataki version signed with the same release key is already installed, the new APK can normally be installed directly as an update. Before uninstalling the app, exporting personal training data as JSON is recommended.

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
- dark, training-focused design
- custom Tabataki app icon

## Technology

- Kotlin
- Jetpack Compose
- Material 3
- Room
- DataStore
- Gradle
- GitHub Actions

## Local development

1. Clone the repository.
2. Open the repository in Android Studio.
3. Sync the Gradle dependencies.
4. Run the app on an emulator or Android device.

The Android project is located directly in the repository root.

On Windows PowerShell, the local quality checks can be run with:

```powershell
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest
```

## Quality assurance

GitHub Actions automatically checks changes. The current workflow includes:

- unit tests
- Android Lint
- debug APK build
- instrumentation-test APK compilation
- signed release build for new versions

New releases are created automatically and signed with the configured release key. The private signing key is not stored in the repository source code.

## Privacy

Tabataki does not require a user account for local use and currently does not request additional Android permissions such as location, camera, microphone, or contacts.

Training data and settings are stored locally on the device. Tabataki currently does not use its own backend server for this data and does not transmit it to its own online service.

Android cloud backups and app-data transfer through Android's backup and device-transfer mechanisms are intentionally disabled or excluded. A manual JSON export is available for deliberate backups.

## Project status

**In development.**

The app is available as an installable Android APK. Releases should currently still be considered development and testing versions.

## Feedback

Bugs, improvement suggestions, and ideas are welcome and can be submitted through [GitHub Issues](https://github.com/scncvk19/tabataki-android/issues).

Constructive feedback helps improve Tabataki both technically and functionally.

## Usage rights

This repository is **not licensed as an open-source project**. The source code is made available for viewing and evaluation. No permission is granted to copy, modify, redistribute, or use it in another project without the copyright holder's prior written permission.

See [COPYRIGHT.md](COPYRIGHT.md) for details. Third-party dependencies remain subject to their respective licenses.

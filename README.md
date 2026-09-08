# Tabataki Android

Tabataki ist eine schlanke Android-App für Tabata- und Intervalltraining. Sie kombiniert einen manuellen Timer mit Trainingstagen, Routinen und einer Übungsbibliothek und ist als persönliches Lern- und Community-Projekt gedacht.

## Funktionen

- Tabata-/Intervalltimer mit Arbeitszeit, Pause und Runden
- Trainingstage und Routinen
- Übungsbibliothek mit eigenen Übungen und Kategorien
- Backup und Wiederherstellung der eigenen Routinen als JSON
- Mehrsprachige Oberfläche
- Akustisches Feedback während des Timers
- Lokale Nutzung ohne Benutzerkonto

## Datenschutz

Für die lokale Nutzung ist kein Benutzerkonto erforderlich. Tabataki fordert derzeit keine zusätzlichen Android-Berechtigungen wie Standort, Kamera, Mikrofon oder Kontakte an.

Trainingsdaten und Einstellungen werden von der App lokal auf dem Gerät gespeichert. Tabataki verwendet derzeit keinen eigenen Server und übermittelt diese Daten nicht an einen eigenen Online-Dienst. Android-Cloud-Backups sowie Android-Backup- und Geräteübertragungsmechanismen für App-Daten sind bewusst deaktiviert bzw. ausgeschlossen.

## Entwicklung

Voraussetzungen:

- JDK 17
- Android SDK passend zu `compileSdk`

Projekt lokal prüfen:

```powershell
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest
```

Die Debug-APK liegt anschließend unter:

```text
app\build\outputs\apk\debug\app-debug.apk
```

GitHub Actions führt dieselben Prüfungen bei Pull Requests gegen `main` automatisch aus.

## Installation

Veröffentlichte APKs werden über GitHub Releases bereitgestellt. Android kann bei einer manuellen APK-Installation je nach Geräte- und Sicherheitseinstellungen eine Bestätigung für Installationen aus unbekannten Quellen verlangen.

## Feedback

Fehler, Ideen und Verbesserungsvorschläge können über die GitHub Issues des Projekts gemeldet werden.

## Lizenz / Nutzung

Der Quellcode ist öffentlich einsehbar, sofern das Repository öffentlich geschaltet wird. Daraus folgt nicht automatisch eine Open-Source-Lizenz. Maßgeblich sind die Hinweise in `COPYRIGHT.md` und `THIRD_PARTY_NOTICES.md`.

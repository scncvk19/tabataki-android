# Tabataki

Tabataki ist eine Android-App für Intervall- und Tabata-Training. Ich habe sie als KI-unterstütztes Lernprojekt entwickelt, um praktische Erfahrung mit Kotlin, Jetpack Compose und lokaler Datenspeicherung zu sammeln.

Das Repository befindet sich aktuell in Entwicklung. Der Quellcode ist noch nicht als vollständig stabile Release-Version gedacht.

## Funktionen

- konfigurierbarer Intervall-Timer
- Trainingstage und Routinen
- eigener Übungskatalog
- lokale Speicherung ohne Benutzerkonto
- Backup/Import von Trainingsplänen
- akustische Signale bei Phasenwechseln
- mehrsprachige Benutzeroberfläche
- dunkles, für das Training optimiertes Design

## Technik

- Kotlin
- Jetpack Compose
- Material 3
- Room
- DataStore
- Gradle
- GitHub Actions

## Installation auf Android

Tabataki ist nicht auf den Google Play Store angewiesen. Fertige Versionen können direkt über GitHub als APK verteilt werden.

### Stabile Releases

Sobald ein signierter Release veröffentlicht wurde:

1. Auf der GitHub-Seite dieses Repositories **Releases** öffnen.
2. Die Datei `Tabataki-<version>.apk` herunterladen.
3. Android erlauben, Apps aus dieser Quelle zu installieren, falls das Gerät danach fragt.
4. APK öffnen und Tabataki installieren.

Wichtig: Android-Updates funktionieren nur zuverlässig, wenn alle Release-APKs mit demselben privaten Signing-Key signiert werden.

### Test-Builds

GitHub Actions erzeugt bei Builds auf `main` zusätzlich eine Debug-APK als Workflow-Artefakt. Diese ist für Tests gedacht, nicht als langfristige Release-Version.

## Projektstruktur

Das Android-Projekt liegt direkt in der Repository-Wurzel und kann unmittelbar in Android Studio geöffnet werden.

Die Kotlin-Struktur ist in getrennte Bereiche für Lokalisierung, Datenhaltung, Timerzustand und Benutzeroberfläche aufgeteilt.

## Lokal entwickeln

1. Repository klonen.
2. Den Repository-Ordner in Android Studio öffnen.
3. Gradle-Abhängigkeiten synchronisieren.
4. Die App auf einem Emulator oder Android-Gerät starten.

Unter Windows kann alternativ `build.ps1` verwendet werden, um Unit-Tests und einen Debug-Build auszuführen.

## GitHub Release Signing

Der private Signing-Key wird bewusst nicht im Repository gespeichert. Für automatische signierte GitHub Releases erwartet der Workflow folgende Repository-Secrets:

- `TABATAKI_KEYSTORE_BASE64`
- `TABATAKI_KEYSTORE_PASSWORD`
- `TABATAKI_KEY_ALIAS`
- `TABATAKI_KEY_PASSWORD`

Ein Tag wie `v1.0.10` startet anschließend automatisch den Release-Build und hängt die signierte APK an einen GitHub Release an.

## Datenschutz

Die Trainingsdaten werden lokal auf dem Gerät gespeichert. Es gibt kein Benutzerkonto und die App benötigt für ihre Kernfunktionen keine Internetverbindung.

## Transparenz

Das Projekt wurde von mir mit Unterstützung generativer KI entwickelt. Architektur, Funktionsauswahl, Tests und weitere Überarbeitung erfolgen im Rahmen meines persönlichen Lernprozesses.

## Status

In Entwicklung. Noch keine vollständig geprüfte stabile Release-Version.

## Nutzungsrechte

Der Quellcode ist zur Ansicht und Bewertung veröffentlicht. Es wird keine Erlaubnis zur Übernahme, Veränderung oder Weiterverteilung erteilt. Einzelheiten stehen in [COPYRIGHT.md](COPYRIGHT.md).

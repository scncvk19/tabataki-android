# Tabataki

Tabataki ist eine Android-App für Intervall- und Tabata-Training. Ich habe sie als Lernprojekt entwickelt, um praktische Erfahrung mit Kotlin, Jetpack Compose, Android-Entwicklung und lokaler Datenspeicherung zu sammeln.

Das Projekt befindet sich weiterhin in Entwicklung. Über GitHub Releases werden signierte APK-Dateien für Tests und die direkte Installation auf Android-Geräten bereitgestellt.

## Funktionen

- konfigurierbarer Intervall-Timer
- Trainingstage und Routinen
- eigener Übungskatalog
- lokale Speicherung ohne Benutzerkonto
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
- GitHub Actions für Build und Release

## Installation

Die aktuelle APK kann über den Bereich **Releases** dieses Repositories heruntergeladen werden.

> Hinweis: Tabataki befindet sich noch in Entwicklung. Releases dienen derzeit vor allem zum Testen der App auf echten Android-Geräten.

Je nach Android- und Play-Protect-Einstellungen kann die Installation von APK-Dateien außerhalb des Google Play Stores eingeschränkt oder zusätzlich bestätigt werden müssen.

## Lokal entwickeln

1. Repository klonen.
2. Das Repository in Android Studio öffnen.
3. Gradle-Abhängigkeiten synchronisieren.
4. Die App auf einem Emulator oder Android-Gerät starten.

Das Android-Projekt liegt direkt in der Repository-Wurzel.

## Datenschutz

Tabataki benötigt für die lokale Nutzung kein Benutzerkonto und fordert derzeit keine zusätzlichen Android-Berechtigungen wie Standort, Kamera, Mikrofon oder Kontakte an.

Trainingsdaten und Einstellungen werden von der App lokal auf dem Gerät gespeichert. Tabataki verwendet derzeit keinen eigenen Server und überträgt diese Daten nicht an einen eigenen Online-Dienst.

Android-Cloud-Backups und die Übernahme der App-Daten über die von Android bereitgestellten Backup-/Geräteübertragungsmechanismen sind für Tabataki bewusst deaktiviert bzw. ausgeschlossen.

## Status

**In Entwicklung.**

Die App ist bereits als installierbare Android-APK verfügbar, sollte aktuell aber noch als Test- bzw. Entwicklungsversion betrachtet werden.

## Feedback

**Für Feedback wäre ich sehr dankbar.**

Fehler, Verbesserungsvorschläge und Ideen können gerne über die [GitHub Issues](https://github.com/scncvk19/tabataki-android/issues) eingereicht werden.

Gerade weil Tabataki ein Lernprojekt ist, hilft mir konstruktives Feedback dabei, die App technisch und funktional weiterzuentwickeln.

## Nutzungsrechte

Der Quellcode ist zur Ansicht und Bewertung veröffentlicht. Es wird keine Erlaubnis zur Übernahme, Veränderung oder Weiterverteilung erteilt. Einzelheiten stehen in [COPYRIGHT.md](COPYRIGHT.md).

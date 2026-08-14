# Tabataki

Tabataki ist eine Android-App für Intervall- und Tabata-Training. Ich habe sie als KI-unterstütztes Lernprojekt entwickelt, um praktische Erfahrung mit Kotlin, Jetpack Compose und lokaler Datenspeicherung zu sammeln.

Das Repository befindet sich aktuell in Vorbereitung für eine öffentliche Veröffentlichung. Der Quellcode ist noch nicht als stabile Release-Version gedacht.

## Funktionen

- konfigurierbarer Intervall-Timer
- Trainingstage und Routinen
- eigener Übungskatalog
- lokale Speicherung ohne Benutzerkonto
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

## Projektstruktur

Das Android-Projekt liegt direkt in der Repository-Wurzel und kann unmittelbar in Android Studio geöffnet werden.

Die Kotlin-Struktur ist in getrennte Bereiche für Lokalisierung, Datenhaltung, Timerzustand und Benutzeroberfläche aufgeteilt.

## Lokal entwickeln

1. Repository klonen.
2. Den Ordner `13 - Tabataki/App/Tabataki` in Android Studio öffnen.
3. Gradle-Abhängigkeiten synchronisieren.
4. Die App auf einem Emulator oder Android-Gerät starten.

## Datenschutz

Die Trainingsdaten werden lokal auf dem Gerät gespeichert. Vor einer Veröffentlichung werden Berechtigungen, Backup-Verhalten und alle externen Verbindungen noch einmal geprüft.

## Transparenz

Das Projekt wurde von mir mit Unterstützung generativer KI entwickelt. Architektur, Funktionsauswahl, Tests und weitere Überarbeitung erfolgen im Rahmen meines persönlichen Lernprozesses.

## Status

In Entwicklung. Noch keine stabile oder geprüfte Release-Version.

## Nutzungsrechte

Der Quellcode ist zur Ansicht und Bewertung veröffentlicht. Es wird keine Erlaubnis zur Übernahme, Veränderung oder Weiterverteilung erteilt. Einzelheiten stehen in [COPYRIGHT.md](COPYRIGHT.md).

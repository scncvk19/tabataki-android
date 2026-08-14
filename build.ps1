$ErrorActionPreference = "Stop"

if (-not $env:JAVA_HOME) {
    $androidStudioJbr = Join-Path $env:ProgramFiles "Android\Android Studio\jbr"
    if (Test-Path $androidStudioJbr) {
        $env:JAVA_HOME = $androidStudioJbr
    } else {
        Write-Error "JAVA_HOME ist nicht gesetzt. Bitte Java 17 installieren oder JAVA_HOME konfigurieren."
    }
}

Push-Location $PSScriptRoot
try {
    .\gradlew.bat testDebugUnitTest assembleDebug
} finally {
    Pop-Location
}

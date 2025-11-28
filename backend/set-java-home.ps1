# Script to set JAVA_HOME correctly for this project
# Run this before building: .\set-java-home.ps1

$javaHome = "C:\Program Files\Java\jdk-25"
if (Test-Path $javaHome) {
    $env:JAVA_HOME = $javaHome
    Write-Host "JAVA_HOME set to: $env:JAVA_HOME" -ForegroundColor Green
    Write-Host "Java version:" -ForegroundColor Yellow
    & "$javaHome\bin\java" -version
} else {
    Write-Host "JDK not found at: $javaHome" -ForegroundColor Red
    Write-Host "Please install JDK 17 or update the path in this script" -ForegroundColor Yellow
}


Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  Starting SmartGram Java Backend (JDK 21)         " -ForegroundColor Green
Write-Host "===================================================" -ForegroundColor Cyan

Set-Location $PSScriptRoot

if (-not (Test-Path "backend-java\bin\com\smartgram\SmartGramServer.class")) {
    Write-Host "Compiling SmartGramServer.java..." -ForegroundColor Yellow
    New-Item -ItemType Directory -Force "backend-java\bin" | Out-Null
    javac -d backend-java\bin backend-java\SmartGramServer.java
}

Write-Host "Running SmartGram Java Server on http://localhost:5002..." -ForegroundColor Green
java -cp backend-java\bin com.smartgram.SmartGramServer 5002

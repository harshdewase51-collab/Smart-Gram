@echo off
echo ===================================================
echo   Starting SmartGram Java Backend (JDK 21)
echo ===================================================

cd /d "%~dp0"

REM Compile if not already compiled
if not exist "backend-java\bin\com\smartgram\SmartGramServer.class" (
    echo Compiling SmartGramServer.java...
    if not exist "backend-java\bin" mkdir "backend-java\bin"
    javac -d backend-java\bin backend-java\SmartGramServer.java
)

echo Running SmartGram Java Server on port 5002...
java -cp backend-java\bin com.smartgram.SmartGramServer 5002
pause

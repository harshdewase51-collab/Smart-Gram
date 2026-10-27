@echo off
echo ===================================================
echo   Starting SmartGram Java Backend (JDK 21)
echo ===================================================

cd /d "%~dp0"

if not exist "backend\bin\com\smartgram\SmartGramServer.class" (
    echo Compiling SmartGramServer.java...
    if not exist "backend\bin" mkdir "backend\bin"
    javac -d backend\bin backend\SmartGramServer.java
)

echo Running SmartGram Java Server on port 5002...
java -cp backend\bin com.smartgram.SmartGramServer 5002
pause

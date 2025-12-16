@echo off
echo ========================================
echo   FC Calc - Install and Run
echo ========================================
echo.

cd /d "%~dp0"

echo Checking connected devices...
adb devices
echo.

echo Installing Debug APK...
echo.

call gradlew.bat installDebug -Dorg.gradle.java.home="C:/Program Files/Microsoft/jdk-21.0.7.6-hotspot"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo   INSTALL SUCCESSFUL
    echo ========================================
    echo.
    echo Launching DocScan Lite...
    adb shell am start -n com.fccalc/.MainActivity
    echo.
    echo App launched successfully!
) else (
    echo.
    echo ========================================
    echo   INSTALL FAILED
    echo ========================================
)

echo.
pause

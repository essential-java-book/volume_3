@echo off
setlocal

rem Maven Wrapper (Capitulo 13): equivalente en
rem Windows de "mvnw". Descarga y cachea la
rem distribucion de Maven de
rem .mvn\wrapper\maven-wrapper.properties la
rem primera vez que se ejecuta.

set "DIR=%~dp0"
set "PROPS=%DIR%.mvn\wrapper\maven-wrapper.properties"

if not exist "%PROPS%" (
  echo No se encuentra %PROPS%
  exit /b 1
)

for /f "usebackq tokens=1,* delims==" %%A in ("%PROPS%") do if "%%A"=="distributionUrl" set "DIST_URL=%%B"

for %%F in ("%DIST_URL%") do set "ZIP_NAME=%%~nxF"
set "DIST_NAME=%ZIP_NAME:-bin.zip=%"

set "CACHE_DIR=%USERPROFILE%\.m2\wrapper\dists\%DIST_NAME%"
set "MAVEN_HOME=%CACHE_DIR%\%DIST_NAME%"

if exist "%MAVEN_HOME%\bin\mvn.cmd" goto :run

if not exist "%CACHE_DIR%" mkdir "%CACHE_DIR%"
echo Descargando %DIST_URL% ...
powershell -NoProfile -Command "Invoke-WebRequest -Uri '%DIST_URL%' -OutFile '%CACHE_DIR%\%ZIP_NAME%'"
if errorlevel 1 exit /b 1
powershell -NoProfile -Command "Expand-Archive -Path '%CACHE_DIR%\%ZIP_NAME%' -DestinationPath '%CACHE_DIR%' -Force"
if errorlevel 1 exit /b 1
del "%CACHE_DIR%\%ZIP_NAME%"

:run
call "%MAVEN_HOME%\bin\mvn.cmd" %*

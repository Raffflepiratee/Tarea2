@echo off
echo ========================================
echo   Recompilando y Redespliegando WAR
echo ========================================

echo.
echo [1/5] Limpiando WAR anterior...
del "C:\Users\rbogl\OneDrive\Escritorio\apache-tomcat-10.1.47\webapps\biblioteca-web.war" 2>nul
rmdir /S /Q "C:\Users\rbogl\OneDrive\Escritorio\apache-tomcat-10.1.47\webapps\biblioteca-web" 2>nul

echo.
echo [2/5] Recompilando proyecto...
cd "%~dp0"
call mvn clean package -q

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Fallo la compilacion
    pause
    exit /b 1
)

echo.
echo [3/5] Copiando WAR a Tomcat...
copy /Y "target\biblioteca-web.war" "C:\Users\rbogl\OneDrive\Escritorio\apache-tomcat-10.1.47\webapps\" >nul

echo.
echo [4/5] Verificando archivo...
if exist "C:\Users\rbogl\OneDrive\Escritorio\apache-tomcat-10.1.47\webapps\biblioteca-web.war" (
    echo    OK - WAR copiado correctamente
) else (
    echo    ERROR - No se pudo copiar el WAR
    pause
    exit /b 1
)

echo.
echo [5/5] Listo!
echo.
echo ========================================
echo   AHORA REINICIA TOMCAT:
echo   1. Presiona Ctrl+C en la terminal de Tomcat
echo   2. Ejecuta: catalina.bat run
echo ========================================
echo.
pause


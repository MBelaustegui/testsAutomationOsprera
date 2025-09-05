@echo off
echo ========================================
echo    EJECUTAR SUITE SGP COMPLETA
echo ========================================
echo.

REM Verificar ambiente actual
for /f "tokens=2 delims==" %%a in ('findstr "ambiente=" src\test\resources\testdata\default.properties') do set current_env=%%a
echo 🌍 Ambiente actual: %current_env%
echo.

echo 🚀 Ejecutando suite completa...
echo.

REM Compilar
echo 📦 Compilando proyecto...
call mvn compile -q
if %errorlevel% neq 0 (
    echo ❌ Error en compilación
    pause
    exit /b 1
)

REM Ejecutar tests
echo 🧪 Ejecutando tests...
call mvn test -Dtest=SGPSuite
if %errorlevel% neq 0 (
    echo ❌ Error en ejecución de tests
    pause
    exit /b 1
)

REM Generar reporte
echo 📊 Generando reporte Allure...
call mvn allure:report -q
if %errorlevel% neq 0 (
    echo ❌ Error generando reporte
    pause
    exit /b 1
)

echo.
echo ✅ ¡Suite ejecutada exitosamente!
echo.
echo 📊 Para ver el reporte:
echo    mvn allure:serve
echo.
echo 🧹 Para limpiar reportes:
echo    limpiar-reportes.bat
echo.
echo 🔄 Para cambiar ambiente:
echo    cambiar-ambiente.bat
echo.
pause

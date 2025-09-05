@echo off
echo ========================================
echo    SGP - SISTEMA DE TESTING AUTOMATIZADO
echo ========================================
echo.

:menu
echo Selecciona el tipo de test a ejecutar:
echo.
echo 1. Smoke Test (Tests críticos rápidos)
echo 2. Performance Test (Tests de rendimiento)
echo 3. Validación de Campos (Test principal)
echo 4. Suite Completa (Pedidos + Auditoría Provincial + Central)
echo 5. Solo Tests Críticos
echo 6. Solo Tests de Regresión
echo 7. Generar Reporte Allure
echo 8. Servir Reporte Allure
echo 9. Salir
echo.
set /p choice="Ingresa tu opción (1-9): "

if "%choice%"=="1" goto smoke
if "%choice%"=="2" goto performance
if "%choice%"=="3" goto validacion
if "%choice%"=="4" goto suite
if "%choice%"=="5" goto critical
if "%choice%"=="6" goto regression
if "%choice%"=="7" goto report
if "%choice%"=="8" goto serve
if "%choice%"=="9" goto exit
goto menu

:smoke
echo.
echo 🚀 Ejecutando Smoke Tests...
mvn test -Dtest=SmokeTest
goto end

:performance
echo.
echo ⚡ Ejecutando Performance Tests...
mvn test -Dtest=PerformanceTest
goto end

:validacion
echo.
echo ✅ Ejecutando Test de Validación de Campos...
mvn test -Dtest=ValidacionCamposRequeridosTest
goto end

:suite
echo.
echo 🎯 Ejecutando Suite Completa (Pedidos + Auditoría Provincial + Central)...
mvn test -Dtest=SGPSuiteTest
goto end

:all
echo.
echo 🌟 Ejecutando TODOS los Tests...
mvn test
goto end

:critical
echo.
echo 🔴 Ejecutando Tests Críticos (Smoke + Performance)...
mvn test -Dtest="SmokeTest,PerformanceTest"
goto end

:regression
echo.
echo 🔄 Ejecutando Tests de Regresión...
mvn test -Dtest="ValidacionCamposRequeridosTest,SmokeTest"
goto end

:report
echo.
echo 📊 Generando Reporte Allure...
mvn allure:report
echo ✅ Reporte generado en: target/site/allure-maven-plugin/
goto end

:serve
echo.
echo 🌐 Serviendo Reporte Allure...
mvn allure:serve
goto end

:end
echo.
echo ========================================
echo Test completado. Presiona cualquier tecla para continuar...
pause >nul
goto menu

:exit
echo.
echo 👋 ¡Hasta luego!
pause
exit

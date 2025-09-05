@echo off
echo ========================================
echo    CAMBIAR AMBIENTE DE TESTING
echo ========================================
echo.
echo 1. QA (Desarrollo)
echo 2. PRODUCCION
echo 3. DESARROLLO
echo.
set /p opcion="Selecciona el ambiente (1-3): "

if "%opcion%"=="1" (
    echo Cambiando a ambiente QA...
    powershell -Command "(Get-Content 'src\test\resources\testdata\default.properties') -replace 'ambiente=.*', 'ambiente=qa' | Set-Content 'src\test\resources\testdata\default.properties'"
    echo ✅ Ambiente cambiado a QA
    echo 🔗 URL: https://sioqa.osprera.org.ar
    echo 👤 Usuario: prueba_admdeleg
) else if "%opcion%"=="2" (
    echo Cambiando a ambiente PRODUCCION...
    powershell -Command "(Get-Content 'src\test\resources\testdata\default.properties') -replace 'ambiente=.*', 'ambiente=produccion' | Set-Content 'src\test\resources\testdata\default.properties'"
    echo ✅ Ambiente cambiado a PRODUCCION
    echo 🔗 URL: https://sio.osprera.org.ar
    echo 👤 Usuario Pedidos: BelausteguiMA
    echo 👤 Usuario Auditoría Provincial: BelausteguiMA
    echo 👤 Usuario Auditoría Central: BelausteguiMA
    echo 🔑 Contraseña: Inicio03
    echo 🏷️ Tipo Usuario: OSPRERA
) else if "%opcion%"=="3" (
    echo Cambiando a ambiente DESARROLLO...
    powershell -Command "(Get-Content 'src\test\resources\testdata\default.properties') -replace 'ambiente=.*', 'ambiente=desarrollo' | Set-Content 'src\test\resources\testdata\default.properties'"
    echo ✅ Ambiente cambiado a DESARROLLO
    echo 🔗 URL: https://sio-dev.osprera.org.ar
    echo 👤 Usuario: dev_user
) else (
    echo ❌ Opción inválida
)

echo.
echo Presiona cualquier tecla para continuar...
pause > nul

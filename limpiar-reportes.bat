@echo off
echo ========================================
echo    LIMPIAR REPORTES DE TESTING
echo ========================================
echo.
echo 🧹 Limpiando todos los reportes...
echo.

echo Eliminando allure-results...
if exist "target\allure-results" (
    rmdir /s /q "target\allure-results"
    echo ✅ allure-results eliminado
) else (
    echo ⚠️ allure-results no existe
)

echo Eliminando allure-report...
if exist "target\allure-report" (
    rmdir /s /q "target\allure-report"
    echo ✅ allure-report eliminado
) else (
    echo ⚠️ allure-report no existe
)

echo Eliminando surefire-reports...
if exist "target\surefire-reports" (
    rmdir /s /q "target\surefire-reports"
    echo ✅ surefire-reports eliminado
) else (
    echo ⚠️ surefire-reports no existe
)

echo Eliminando screenshots...
if exist "evidencia_*.png" (
    del /q "evidencia_*.png"
    echo ✅ Screenshots eliminados
) else (
    echo ⚠️ No hay screenshots para eliminar
)

echo Eliminando archivos temporales...
if exist "numero_pedido_temp.txt" (
    del /q "numero_pedido_temp.txt"
    echo ✅ Archivo temporal eliminado
) else (
    echo ⚠️ No hay archivos temporales
)

echo.
echo 🎉 ¡Limpieza completada!
echo.
echo Ahora puedes ejecutar:
echo   mvn test -Dtest=SGPSuite
echo   mvn allure:report
echo   mvn allure:serve
echo.
pause

# 🚀 SGP - Sistema de Testing Automatizado con Allure Avanzado

## 📋 **Índice**
- [Funcionalidades Implementadas](#-funcionalidades-implementadas)
- [Estructura de Tests](#-estructura-de-tests)
- [Comandos de Ejecución](#-comandos-de-ejecución)
- [Reportes y Métricas](#-reportes-y-métricas)
- [Configuración de Ambientes](#-configuración-de-ambientes)
- [Mejores Prácticas](#-mejores-prácticas)

## ✨ **Funcionalidades Implementadas**

### 🎯 **1. Organización Jerárquica de Tests**
- **Epics**: Agrupación principal por sistema
- **Features**: Funcionalidades específicas
- **Stories**: Historias de usuario
- **Tags**: Clasificación por tipo y prioridad

### 📊 **2. Métricas de Performance**
- Tiempo de carga de páginas
- Tiempo de respuesta de formularios
- Métricas de setup y teardown
- Comparación entre ambientes

### 🔍 **3. Screenshots Inteligentes**
- Captura automática en cada step
- Screenshots de errores
- Evidencias organizadas por test
- Calidad y formato optimizados

### 🏷️ **4. Sistema de Tags Avanzado**
- `@smoke`: Tests críticos rápidos
- `@regression`: Tests de regresión
- `@performance`: Tests de rendimiento
- `@validacion`: Tests de validación
- `@pedidos`: Tests del módulo pedidos

## 🧪 **Estructura de Tests**

### **ValidacionCamposRequeridosTest.java**
```java
@Epic("SGP - Sistema de Gestión de Pedidos")
@Feature("Validación de Campos Requeridos")
@Story("Pedidos de Medicamentos")
@Story("Validación de Formularios")
@Tag("smoke")                    // Test crítico
@Tag("regression")               // Test de regresión
@Tag("validacion")               // Test de validación
@Tag("pedidos")                  // Test del módulo pedidos
```

### **PerformanceTest.java**
```java
@Epic("SGP - Sistema de Gestión de Pedidos")
@Feature("Performance Testing")
@Story("Métricas de Rendimiento")
@Tag("performance")
@Tag("metrics")
@Tag("smoke")
```

### **SmokeTest.java**
```java
@Epic("SGP - Sistema de Gestión de Pedidos")
@Feature("Smoke Testing")
@Story("Verificación Rápida del Sistema")
@Tag("smoke")
@Tag("critical")
@Tag("quick")
```

## 🚀 **Comandos de Ejecución**

### **Ejecución Individual:**
```bash
# Test de validación principal
mvn test -Dtest=ValidacionCamposRequeridosTest

# Test de performance
mvn test -Dtest=PerformanceTest

# Test de smoke
mvn test -Dtest=SmokeTest
```

### **Ejecución por Tags:**
```bash
# Solo tests críticos
mvn test -Dcucumber.filter.tags="@smoke"

# Tests de regresión
mvn test -Dcucumber.filter.tags="@regression"

# Tests de performance
mvn test -Dcucumber.filter.tags="@performance"
```

### **Ejecución por Módulo:**
```bash
# Solo tests de pedidos
mvn test -Dcucumber.filter.tags="@pedidos"

# Tests de validación
mvn test -Dcucumber.filter.tags="@validacion"
```

### **Script Automatizado:**
```bash
# Usar el archivo run-tests.bat
./run-tests.bat
```

## 📊 **Reportes y Métricas**

### **Generar Reporte:**
```bash
mvn allure:report
```

### **Servir Reporte Localmente:**
```bash
mvn allure:serve
```

### **Métricas Disponibles:**
- ⏱️ **Tiempo de Setup**: Configuración inicial
- 🚀 **Tiempo de Login**: Autenticación
- 📱 **Tiempo de Carga**: Páginas y elementos
- 🔄 **Tiempo de Respuesta**: Formularios y acciones
- 📊 **Performance Summary**: Resumen completo

### **Screenshots Organizados:**
- 📸 **Login Exitoso**: Después de autenticación
- 📸 **Mensajes Validación**: Campos requeridos
- 📸 **Error Screenshots**: Capturas automáticas de errores
- 📸 **Performance Screenshots**: Estados de páginas

## 🌍 **Configuración de Ambientes**

### **Archivo: `default.properties`**
```properties
# Ambiente actual
ambiente=qa

# QA Environment
qa.url=https://sioqa.osprera.org.ar/#/login
qa.usuario.pedidos=prueba_admdeleg
qa.usuario.auditoria_provincial=prueba_auddeleg
qa.usuario.auditoria_central=prueba_audcentral
qa.password=test1234
qa.tipo_usuario=Externo

# Producción
produccion.url=https://sio.osprera.org.ar/#/login
produccion.usuario=BelausteguiMA
produccion.password=Inicio03
produccion.tipo_usuario=OSPRERA
```

### **Cambiar Ambiente:**
1. Editar `src/test/resources/testdata/default.properties`
2. Cambiar la línea `ambiente=qa` por `ambiente=produccion`
3. Ejecutar tests normalmente

## 🎨 **Configuración de Allure**

### **Archivo: `allure.properties`**
```properties
# Directorios
allure.results.directory=target/allure-results
allure.report.directory=target/allure-report

# Screenshots
allure.screenshot.quality=80
allure.screenshot.format=png

# Métricas
allure.metrics.enabled=true
allure.metrics.include.pattern=.*Performance.*
allure.metrics.include.pattern=.*Metrics.*

# Filtros
allure.filter.tags=smoke,regression,performance,validacion,pedidos
allure.filter.severity=CRITICAL,NORMAL,MINOR
```

## 📈 **Mejores Prácticas**

### **1. Organización de Tests:**
- ✅ Usar tags consistentes
- ✅ Agrupar por funcionalidad
- ✅ Mantener jerarquía clara

### **2. Screenshots:**
- ✅ Capturar en momentos clave
- ✅ Incluir en steps importantes
- ✅ Organizar por contexto

### **3. Métricas:**
- ✅ Medir tiempos críticos
- ✅ Comparar entre ambientes
- ✅ Documentar thresholds

### **4. Reportes:**
- ✅ Generar después de cada ejecución
- ✅ Revisar métricas regularmente
- ✅ Compartir con el equipo

## 🔧 **Troubleshooting**

### **Problema: Tests no se ejecutan**
```bash
# Compilar primero
mvn compile -q

# Verificar nombres de clases
mvn test -Dtest=ValidacionCamposRequeridosTest
```

### **Problema: Screenshots no se ven**
```bash
# Verificar configuración de Allure
mvn allure:report
mvn allure:serve
```

### **Problema: Ambiente incorrecto**
```bash
# Verificar default.properties
# Cambiar ambiente=qa por ambiente=produccion
```

## 📱 **Vistas de Reporte**

### **Dashboard Principal:**
- 📊 Resumen de ejecuciones
- 📈 Gráficos de tendencias
- 🏷️ Filtros por tags

### **Vista de Tests:**
- 📋 Lista de todos los tests
- 🔍 Filtros avanzados
- 📊 Métricas por test

### **Vista de Steps:**
- 📝 Detalle de cada paso
- 📸 Screenshots organizados
- ⏱️ Tiempos por step

### **Vista de Métricas:**
- 📊 Performance metrics
- 🔄 Comparaciones
- 📈 Tendencias

## 🎯 **Próximos Pasos Recomendados**

1. **Ejecutar Smoke Tests** para verificar funcionalidad básica
2. **Revisar métricas de performance** en el reporte
3. **Configurar CI/CD** para ejecución automática
4. **Implementar más tests** usando la estructura establecida
5. **Personalizar reportes** según necesidades del equipo

---

## 🚀 **¡Listo para Usar!**

Tu sistema de testing ahora incluye:
- ✅ **Organización jerárquica** de tests
- ✅ **Métricas de performance** detalladas
- ✅ **Screenshots inteligentes** organizados
- ✅ **Sistema de tags** avanzado
- ✅ **Configuración de ambientes** flexible
- ✅ **Reportes Allure** optimizados

¡Ejecuta `./run-tests.bat` para empezar! 🎉✨

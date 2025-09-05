# 🚀 Selenium + Allure - Sistema de Reportes Avanzado

## 📋 Descripción
Este proyecto ha sido **completamente migrado** de Cucumber a **JUnit 5 + Allure** para proporcionar reportes de pruebas más visuales, detallados y profesionales.

## ✨ Características de Allure

### 🎨 **Reportes Visuales Súper Atractivos**
- **Dashboard interactivo** con gráficos y estadísticas
- **Timeline de ejecución** con duración de cada paso
- **Screenshots automáticos** en caso de fallos
- **Logs detallados** de cada operación
- **Métricas de rendimiento** y tiempos de respuesta

### 🔍 **Información Detallada**
- **Epics, Features y Stories** organizados jerárquicamente
- **Severidad de pruebas** (Critical, High, Medium, Low)
- **Tags personalizados** para categorización
- **Pasos detallados** con anotaciones @Step
- **Attachments** para logs, screenshots y datos

## 🚀 **Tests Migrados y Disponibles**

### **✅ Tests de Validación**
- **`ValidacionCamposRequeridosTest`** - Valida mensajes de campos requeridos en pedidos de medicamentos

### **✅ Tests de Auditoría**
- **`AuditoriaProvincialTest`** - Flujo completo de auditoría provincial
- **`AuditoriaCentralTest`** - Flujo completo de auditoría central

### **✅ Tests de Pedidos**
- **`PedidoLentesTest`** - Flujo completo de pedidos de lentes
- **`PedidoProtesisTest`** - Flujo completo de pedidos de prótesis
- **`PedidoMedicamentosTest`** - Flujo completo de pedidos de medicamentos

### **✅ Test Suite Principal**
- **`SGPSuiteTest`** - Suite principal que ejecuta todos los tests

## 🛠️ Instalación y Configuración

### **1. Instalar Allure Command Line Tool**
```bash
# Windows (con chocolatey)
choco install allure

# macOS (con homebrew)
brew install allure

# Linux
sudo apt-add-repository ppa:qameta/allure
sudo apt update
sudo apt install allure
```

### **2. Verificar Instalación**
```bash
allure --version
```

## 🚀 Ejecución de Tests

### **Ejecutar Todos los Tests**
```bash
# Ejecutar todos los tests
mvn clean test

# Generar reporte HTML
mvn allure:report

# Abrir reporte en navegador
mvn allure:serve
```

### **Ejecutar Tests Específicos**
```bash
# Por tags
mvn test -Dtest=ValidacionCamposRequeridosTest
mvn test -Dtest=AuditoriaProvincialTest
mvn test -Dtest=AuditoriaCentralTest
mvn test -Dtest=PedidoLentesTest
mvn test -Dtest=PedidoProtesisTest
mvn test -Dtest=PedidoMedicamentosTest

# Por método específico
mvn test -Dtest=ValidacionCamposRequeridosTest#validarMensajesCamposRequeridos
```

### **Ejecutar por Categorías**
```bash
# Solo tests de validación
mvn test -Dtest=ValidacionCamposRequeridosTest

# Solo tests de auditoría
mvn test -Dtest=AuditoriaProvincialTest,AuditoriaCentralTest

# Solo tests de pedidos
mvn test -Dtest=PedidoLentesTest,PedidoProtesisTest,PedidoMedicamentosTest
```

## 📊 Generación de Reportes

### **Comandos Maven para Allure**
```bash
# Limpiar y ejecutar tests
mvn clean test

# Generar reporte estático
mvn allure:report

# Servir reporte en servidor local
mvn allure:serve

# Generar reporte y abrir en navegador
mvn allure:report allure:open
```

### **Comandos Directos de Allure**
```bash
# Generar reporte desde resultados existentes
allure generate target/allure-results -o target/allure-report

# Abrir reporte en navegador
allure open target/allure-report

# Servir reporte en servidor local
allure serve target/allure-results
```

## 🎯 Estructura del Test con Allure

### **Anotaciones Principales**
```java
@Epic("SGP - Sistema de Gestión de Pedidos")           // Categoría principal
@Feature("Validación de Campos Requeridos")            // Funcionalidad específica
@Story("Pedidos de Medicamentos")                      // Caso de uso específico
@Description("Descripción detallada del test")         // Descripción del test
@Severity(SeverityLevel.CRITICAL)                      // Nivel de severidad
@Tag("validacion")                                     // Tags para categorización
```

### **Anotaciones de Pasos**
```java
@Step("Descripción del paso")                          // Paso individual
Allure.step("Mensaje informativo")                     // Paso dinámico
Allure.addAttachment("Nombre", "tipo", "contenido")    // Adjuntar archivos
```

## 📁 Estructura de Directorios

```
src/test/java/com/osprera/test/
├── ValidacionCamposRequeridosTest.java    # Test de validación
├── AuditoriaProvincialTest.java           # Test de auditoría provincial
├── AuditoriaCentralTest.java              # Test de auditoría central
├── PedidoLentesTest.java                  # Test de pedidos de lentes
├── PedidoProtesisTest.java                # Test de pedidos de prótesis
├── PedidoMedicamentosTest.java            # Test de pedidos de medicamentos
└── SGPSuiteTest.java                      # Suite principal

target/
├── allure-results/          # Resultados de ejecución
├── allure-report/           # Reporte HTML generado
└── surefire-reports/        # Reportes de Maven
```

## 🔧 Configuración Avanzada

### **Personalizar Reportes**
```properties
# allure.properties
allure.report.title=SGP - Validación de Campos Requeridos
allure.report.description=Reporte de pruebas para validación de campos requeridos
allure.language=es
allure.attachments.max.size=52428800
```

### **Configuración de Screenshots**
```java
// Capturar screenshot en caso de error
try {
    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    Allure.addAttachment("Error Screenshot", "image/png", new String(screenshot));
} catch (Exception e) {
    Allure.addAttachment("Error", "text/plain", "No se pudo capturar screenshot");
}
```

## 🌟 Ventajas sobre Cucumber

| Aspecto | Cucumber | Allure + JUnit 5 |
|---------|----------|------------------|
| **Reportes** | Básicos HTML | Visuales y profesionales |
| **Screenshots** | Manual | Automáticos |
| **Métricas** | Limitadas | Detalladas y gráficas |
| **Organización** | Gherkin | Anotaciones Java |
| **Mantenimiento** | Archivos .feature | Código Java directo |
| **Integración** | Solo con Cucumber | Con cualquier framework |
| **Personalización** | Limitada | Totalmente personalizable |

## 🚨 Solución de Problemas

### **Error: "allure command not found"**
```bash
# Agregar Allure al PATH
export PATH=$PATH:/usr/local/bin/allure

# O usar ruta completa
/usr/local/bin/allure serve target/allure-results
```

### **Error: "No tests were found"**
```bash
# Verificar que las clases tengan anotaciones @Test
# Verificar que estén en el directorio correcto
# Verificar dependencias en pom.xml
```

### **Reporte no se genera**
```bash
# Limpiar y recompilar
mvn clean compile test-compile

# Verificar permisos de directorio target
# Verificar que los tests se ejecutaron correctamente
```

## 📞 Soporte

Para más información sobre Allure:
- **Documentación oficial**: https://docs.qameta.io/allure/
- **GitHub**: https://github.com/allure-framework/allure2
- **Comunidad**: https://github.com/allure-framework/allure2/discussions

---

## 🎉 ¡Disfruta de tus nuevos reportes súper visuales con Allure!

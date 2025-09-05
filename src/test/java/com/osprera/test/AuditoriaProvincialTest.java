package com.osprera.test;

import com.osprera.test.pages.AuditoriaProvincialPage;
import com.osprera.test.pages.PedidoPage;
import com.osprera.test.utils.ContextoGlobal;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;
import com.osprera.test.hooks.TestFailureListener;

@Epic("SGP - Sistema de Gestión de Pedidos")
@Feature("Auditoría Provincial")
@Story("Autorización de Pedidos")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestFailureListener.class)
public class AuditoriaProvincialTest {
    
    private static WebDriver driver;
    private static AuditoriaProvincialPage auditoriaPage;
    private static PedidoPage pedidoPage;
    
    @BeforeAll
    static void setUp() {
        // Configurar WebDriver
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        auditoriaPage = new AuditoriaProvincialPage(driver);
        pedidoPage = new PedidoPage(driver);
        
        // Navegar a la aplicación usando EnvironmentManager
        String url = com.osprera.test.utils.EnvironmentManager.getCurrentUrl();
        driver.get(url);
    }
    
    @AfterAll
    static void tearDown() {
        try {
            if (driver != null) {
                Allure.step("🔚 Cerrando navegador al finalizar suite de auditoría provincial");
                driver.quit();
                Allure.step("✅ Navegador cerrado exitosamente");
            }
        } catch (Exception e) {
            Allure.step("⚠️ Error al cerrar navegador en tearDown: " + e.getMessage());
        }
    }
    
    @Test
    @Order(1)
    @Description("Flujo completo de auditoría provincial: ingresar como auditor, buscar pedido y autorizarlo")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("auditoria")
    @Tag("provincial")
    @Tag("autorizacion")
    public void flujoCompletoAuditoriaProvincial() {
        
        try {
            // Step 1: Ingresar al sistema como auditor
            Allure.step("🔐 PASO 1: Iniciando sesión como auditor provincial");
            ingresarComoAuditor();
            
            // Step 2: Entrar al módulo SGP
            Allure.step("📋 PASO 2: Accediendo al módulo SGP");
            entrarModuloSGP();
            
            // Step 3: Buscar el pedido generado
            Allure.step("🔍 PASO 3: Buscando pedido para auditoría");
            buscarPedidoGenerado();
            
            // Step 4: Autorizar el pedido
            Allure.step("✅ PASO 4: Autorizando pedido");
            autorizarPedido();
            
            // Step 5: Capturar screenshot final (antes de cerrar popup)
            Allure.step("📸 PASO 5: Capturando evidencia de autorización exitosa");
            try {
                byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                io.qameta.allure.Allure.getLifecycle().addAttachment("✅ AUDITORÍA PROVINCIAL EXITOSA", "image/png", "png", screenshot);
            } catch (Exception e) {
                Allure.step("⚠️ No se pudo capturar screenshot final: " + e.getMessage());
            }
            
            // Step 6: Cerrar el popup de resultados manualmente
            Allure.step("🔚 PASO 6: Finalizando proceso de auditoría");
            cerrarPopupResultados();
            
            // Mensaje de éxito
            Allure.step("🎉 AUDITORÍA PROVINCIAL COMPLETADA EXITOSAMENTE");
            
        } catch (Exception e) {
            // Manejo detallado de errores con captura de pantalla
            String errorMessage = "❌ FALLO EN AUDITORÍA PROVINCIAL: " + e.getMessage();
            Allure.step(errorMessage);
            
            // Capturar screenshot del error
            try {
                byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                io.qameta.allure.Allure.getLifecycle().addAttachment("❌ ERROR - Auditoría Provincial Falló", "image/png", "png", screenshot);
                
                // Agregar información detallada del error
                String errorDetails = String.format(
                    "🚨 DETALLES DEL ERROR:\n" +
                    "=====================================\n" +
                    "❌ Test: Auditoría Provincial\n" +
                    "⏰ Timestamp: %s\n" +
                    "🌍 Ambiente: %s\n" +
                    "👤 Usuario: %s\n" +
                    "🔗 URL: %s\n" +
                    "💥 Error: %s\n" +
                    "📍 Stack Trace: %s\n" +
                    "=====================================\n" +
                    "🛑 EJECUCIÓN DETENIDA - Revisar logs y screenshot",
                    java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    com.osprera.test.utils.EnvironmentManager.getCurrentEnvironment(),
                    com.osprera.test.utils.EnvironmentManager.getUsuarioAuditoriaProvincial(),
                    driver.getCurrentUrl(),
                    e.getMessage(),
                    e.getClass().getSimpleName()
                );
                
                Allure.addAttachment("Error Details", "text/plain", errorDetails);
                
            } catch (Exception screenshotException) {
                Allure.addAttachment("❌ Error Screenshot", "text/plain", 
                    "No se pudo capturar screenshot: " + screenshotException.getMessage());
            }
            
            // Cerrar el navegador antes de fallar
            try {
                if (driver != null) {
                    driver.quit();
                    Allure.step("🔚 Navegador cerrado después del error");
                }
            } catch (Exception closeException) {
                Allure.step("⚠️ Error al cerrar navegador: " + closeException.getMessage());
            }
            
            // Marcar el test como fallido con mensaje claro
            Assertions.fail("🚨 AUDITORÍA PROVINCIAL FALLÓ: " + e.getMessage() + 
                          "\n📸 Revisar screenshot adjunto para más detalles" +
                          "\n🛑 Ejecución detenida para investigación" +
                          "\n🔚 Navegador cerrado automáticamente");
        }
    }
    
    @Step("Ingresar al sistema como auditor")
    private void ingresarComoAuditor() {
        Allure.step("Iniciando sesión como auditor provincial en el sistema");
        
        try {
            // Esperar a que la página esté completamente cargada
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Completar formulario de login con esperas explícitas
            org.openqa.selenium.WebElement usuarioInput = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                    org.openqa.selenium.By.xpath("//input[@placeholder='Usuario']")));
            usuarioInput.clear();
            usuarioInput.sendKeys(com.osprera.test.utils.EnvironmentManager.getUsuarioAuditoriaProvincial());

            org.openqa.selenium.WebElement passwordInput = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                    org.openqa.selenium.By.xpath("//input[@type='password']")));
            passwordInput.clear();
            passwordInput.sendKeys(com.osprera.test.utils.EnvironmentManager.getPasswordAuditoriaProvincial());

            // Seleccionar tipo de usuario
            String tipoUsuario = com.osprera.test.utils.EnvironmentManager.getCurrentTipoUsuario();
            
            // Hacer click en el dropdown usando JavaScript para evitar problemas
            org.openqa.selenium.WebElement dropdownToggle = driver.findElement(
                org.openqa.selenium.By.cssSelector("button.dropdown-toggle"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownToggle);

            // Esperar que aparezcan las opciones y hacer click usando JavaScript
            org.openqa.selenium.WebElement opcionUsuario = driver.findElement(
                org.openqa.selenium.By.xpath("//span[contains(text(),'" + tipoUsuario + "')]"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", opcionUsuario);

            // Hacer click en login
            org.openqa.selenium.WebElement loginButton = driver.findElement(
                org.openqa.selenium.By.xpath("//button[contains(text(),'Iniciar Sesión')]"));
            loginButton.click();

            // Esperar que se complete el login
            try {
                wait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("dashboard"));
            } catch (Exception e) {
                wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.cssSelector("body:not(.loading)")));
            }
            
            Allure.step("✅ Sesión iniciada como auditor provincial exitosamente");
            
        } catch (Exception e) {
            Allure.step("❌ Error al iniciar sesión como auditor: " + e.getMessage());
            throw e;
        }
    }
    
    @Step("Entrar al módulo SGP")
    private void entrarModuloSGP() {
        Allure.step("Accediendo al módulo SGP");
        
        pedidoPage.clickSGP();
        
        // Solo en producción, hacer click en la pestaña "Pendientes"
        String ambiente = com.osprera.test.utils.EnvironmentManager.getCurrentEnvironment();
        if ("produccion".equals(ambiente)) {
            Allure.step("Haciendo click en pestaña 'Pendientes' (solo en producción)");
            pedidoPage.clickPestanaPendientes();
        }
        
        Allure.step("Módulo SGP accedido correctamente");
    }
    
    @Step("Buscar el pedido generado")
    private void buscarPedidoGenerado() {
        Allure.step("Buscando pedido por número de orden");
        
        // Intentar leer desde archivo temporal primero
        String numero = null;
        try {
            if (java.nio.file.Files.exists(java.nio.file.Paths.get("numero_pedido_temp.txt"))) {
                numero = new String(java.nio.file.Files.readAllBytes(
                    java.nio.file.Paths.get("numero_pedido_temp.txt"))).trim();
                System.out.println("📁 Número leído desde archivo: " + numero);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al leer archivo: " + e.getMessage());
        }
        
        // Si no hay archivo, intentar desde variable global
        if (numero == null || numero.isEmpty()) {
            numero = ContextoGlobal.numeroOrden;
            System.out.println("🔍 Intentando desde ContextoGlobal: " + numero);
        }
        
        System.out.println("🔎 Buscando pedido nro: " + numero);
        
        if (numero != null && !numero.isEmpty()) {
            auditoriaPage.buscarPedidoPorNumero(numero);
            Allure.step("Pedido encontrado y seleccionado: " + numero);
        } else {
            Allure.step("⚠️ No hay número de orden disponible para buscar");
            throw new RuntimeException("No se pudo obtener el número de pedido para buscar");
        }
    }
    
    @Step("Autorizar el pedido")
    private void autorizarPedido() {
        Allure.step("Procediendo a autorizar el pedido");
        
        auditoriaPage.hacerClickEnAutorizar();
        
        Allure.step("Pedido autorizado exitosamente");
    }

    @Step("Cerrar el popup de resultados manualmente")
    private void cerrarPopupResultados() {
        Allure.step("Cerrando el popup de resultados de la auditoría provincial");
        try {
            org.openqa.selenium.WebElement cerrarPopupButton = driver.findElement(
                org.openqa.selenium.By.xpath("//button[contains(text(),'Cerrar')]"));
            cerrarPopupButton.click();
            Allure.step("Popup de resultados cerrado exitosamente");
        } catch (org.openqa.selenium.NoSuchElementException e) {
            Allure.step("No se encontró el botón de cerrar popup de resultados. Continuando sin cerrar.");
        } catch (Exception e) {
            Allure.step("Error al intentar cerrar el popup de resultados: " + e.getMessage());
        }
    }
}

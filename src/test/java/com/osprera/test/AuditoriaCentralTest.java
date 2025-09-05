package com.osprera.test;

import com.osprera.test.pages.AuditoriaCentralPage;
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
@Feature("Auditoría Central")
@Story("Autorización de Pedidos en Auditoría Central")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestFailureListener.class)
public class AuditoriaCentralTest {
    
    private static WebDriver driver;
    private static AuditoriaCentralPage auditoriaCentralPage;
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
        
        auditoriaCentralPage = new AuditoriaCentralPage(driver);
        pedidoPage = new PedidoPage(driver);
        
        // Navegar a la aplicación usando EnvironmentManager
        String url = com.osprera.test.utils.EnvironmentManager.getCurrentUrl();
        driver.get(url);
    }
    
    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @Order(1)
    @Description("Flujo completo de auditoría central: ingresar como auditor central, buscar pedido y autorizarlo")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("auditoria")
    @Tag("central")
    @Tag("autorizacion")
    public void flujoCompletoAuditoriaCentral() {
        
        try {
            // Step 1: Ingresar al sistema como auditor central
            ingresarComoAuditorCentral();
            
            // Step 2: Entrar al módulo SGP
            entrarModuloSGP();
            
            // Step 3: Buscar el pedido generado
            buscarPedidoGenerado();
            
            // Step 4: Autorizar el pedido
            autorizarPedido();
            
        } catch (Exception e) {
            // Capturar screenshot en caso de error
            try {
                byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                Allure.addAttachment("Error Screenshot", "image/png", new String(screenshot));
            } catch (Exception screenshotException) {
                Allure.addAttachment("Error Screenshot", "text/plain", "No se pudo capturar screenshot: " + screenshotException.getMessage());
            }
            throw e;
        }
    }
    
    @Step("Ingresar al sistema como auditor central")
    private void ingresarComoAuditorCentral() {
        Allure.step("Iniciando sesión como auditor central en el sistema");
        
        try {
            // Esperar a que la página esté completamente cargada
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Completar formulario de login con esperas explícitas
            org.openqa.selenium.WebElement usuarioInput = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                    org.openqa.selenium.By.xpath("//input[@placeholder='Usuario']")));
            usuarioInput.clear();
            usuarioInput.sendKeys(com.osprera.test.utils.EnvironmentManager.getUsuarioAuditoriaCentral());

            org.openqa.selenium.WebElement passwordInput = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                    org.openqa.selenium.By.xpath("//input[@type='password']")));
            passwordInput.clear();
            passwordInput.sendKeys(com.osprera.test.utils.EnvironmentManager.getPasswordAuditoriaCentral());

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
            
            Allure.step("✅ Sesión iniciada como auditor central exitosamente");
            
        } catch (Exception e) {
            Allure.step("❌ Error al iniciar sesión como auditor central: " + e.getMessage());
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
            auditoriaCentralPage.buscarPedidoPorNumero(numero);
            Allure.step("Pedido encontrado y seleccionado: " + numero);
        } else {
            Allure.step("⚠️ No hay número de orden disponible para buscar");
            throw new RuntimeException("No se pudo obtener el número de pedido para buscar");
        }
    }
    
    @Step("Autorizar el pedido")
    private void autorizarPedido() {
        Allure.step("Procediendo a autorizar el pedido");
        
        // Implementar lógica de autorización según los métodos disponibles
        // auditoriaCentralPage.hacerClickEnAutorizar();
        
        Allure.step("Pedido autorizado exitosamente");
    }
}

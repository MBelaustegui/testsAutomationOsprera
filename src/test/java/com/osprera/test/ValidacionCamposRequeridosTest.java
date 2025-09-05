package com.osprera.test;

import com.osprera.test.pages.PedidoPage;
import com.osprera.test.utils.Vars;
import com.osprera.test.utils.EnvironmentManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;
import com.osprera.test.hooks.TestFailureListener;

@Epic("SGP - Sistema de Gestión de Pedidos")
@Feature("Validación de Campos Requeridos")
@Story("Pedidos de Medicamentos")
@Story("Validación de Formularios")
@Tag("smoke")                    // Test crítico para smoke testing
@Tag("regression")               // Test de regresión
@Tag("validacion")               // Test de validación
@Tag("pedidos")                  // Test del módulo pedidos
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestFailureListener.class)
public class ValidacionCamposRequeridosTest {
    
    private static WebDriver driver;
    private static PedidoPage pedidoPage;
    private static WebDriverWait wait;
    
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
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        pedidoPage = new PedidoPage(driver);
        
        // Navegar a la aplicación del ambiente configurado
        String url = EnvironmentManager.getCurrentUrl();
        driver.get(url);
        
        // Log del ambiente actual
        System.out.println("🌍 " + EnvironmentManager.getEnvironmentInfo());
        System.out.println("🔗 URL: " + EnvironmentManager.getCurrentUrl());
        System.out.println("👤 Usuario Pedidos: " + EnvironmentManager.getUsuarioPedidos());
        System.out.println("🔑 Password: " + EnvironmentManager.getCurrentPassword());
        System.out.println("🏷️ Tipo Usuario: " + EnvironmentManager.getCurrentTipoUsuario());
        
        // Métricas del ambiente para Allure
        long startTime = System.currentTimeMillis();
        String ambienteInfo = EnvironmentManager.getEnvironmentInfo();
        
        Allure.addAttachment("Ambiente de Testing", "text/plain", ambienteInfo);
        Allure.addAttachment("Configuración del Test", "text/plain", 
            "URL: " + EnvironmentManager.getCurrentUrl() + "\n" +
            "Usuario: " + EnvironmentManager.getUsuarioPedidos() + "\n" +
            "Tipo Usuario: " + EnvironmentManager.getCurrentTipoUsuario() + "\n" +
            "Timestamp: " + new java.util.Date());
        
        // Métricas de performance
        long setupTime = System.currentTimeMillis() - startTime;
        Allure.addAttachment("Performance Metrics", "text/plain", 
            "Setup Time: " + setupTime + "ms");
    }
    
    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @Order(1)
    @Description("Validar que aparezcan mensajes de campos requeridos al enviar pedido incompleto")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("validacion")
    @Tag("camposRequeridos")
    @Tag("medicamentos")
    public void validarMensajesCamposRequeridos() {
        
        try {
            // Step 1: Iniciar sesión
            iniciarSesion();
            
            // Step 2: Entrar al módulo SGP
            entrarModuloSGP();
            
            // Step 3: Ingresar DNI del beneficiario
            ingresarDNIBeneficiario();
            
            // Step 4: Seleccionar tipo de pedido medicamento
            seleccionarTipoPedidoMedicamento();
            
            // Step 5: Completar datos mínimos requeridos
            completarDatosMinimosRequeridos();
            
            // Step 6: Hacer click en enviar a auditoría
            hacerClickEnviarAuditoria();
            
            // Step 7: Validar mensajes de campos requeridos
            validarMensajesCamposRequeridosEnPantalla();
            
        } catch (Exception e) {
            // Capturar screenshot en caso de error
            try {
                if (driver != null) {
                    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    Allure.getLifecycle().addAttachment("Error Screenshot", "image/png", "png", screenshot);
                    Allure.addAttachment("Error Details", "text/plain", "Error: " + e.getMessage() + "\nStack Trace: " + e.toString());
                } else {
                    Allure.addAttachment("Error Screenshot", "text/plain", "Driver no disponible para screenshot");
                }
            } catch (Exception screenshotException) {
                Allure.addAttachment("Error Screenshot", "text/plain", "No se pudo capturar screenshot: " + screenshotException.getMessage());
            }
            throw e;
        }
    }
    
    @Step("Iniciar sesión correctamente")
    private void iniciarSesion() {
        Allure.step("Iniciando sesión en la aplicación");
        
        long loginStartTime = System.currentTimeMillis();
    
        // Usar las credenciales específicas para pedidos del ambiente configurado
        String usuario = EnvironmentManager.getUsuarioPedidos();
        String password = EnvironmentManager.getCurrentPassword();
        
        System.out.println("🔑 Usuario: " + usuario);
        System.out.println("🔑 Password: " + password);
        
        WebElement usuarioInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//input[@placeholder='Usuario']")));
        usuarioInput.sendKeys(usuario);
        
        WebElement passwordInput = driver.findElement(By.xpath("//input[@type='password']"));
        passwordInput.sendKeys(password);
        
        // Seleccionar tipo de usuario según el ambiente configurado
        String tipoUsuario = EnvironmentManager.getCurrentTipoUsuario();
        driver.findElement(By.cssSelector("button.dropdown-toggle")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//span[contains(text(),'" + tipoUsuario + "')]"))).click();
        
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Iniciar Sesión')]"));
        loginButton.click();
        
        // Esperar que se complete el login (ajustar según la página real)
        try {
            wait.until(ExpectedConditions.urlContains("dashboard"));
        } catch (Exception e) {
            // Si no hay dashboard, esperar que cambie la URL o aparezca algún elemento
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("body:not(.loading)")));
        }
        
        // Métricas de performance del login
        long loginTime = System.currentTimeMillis() - loginStartTime;
        Allure.addAttachment("Login Performance", "text/plain", 
            "Tiempo de Login: " + loginTime + "ms");
        
        Allure.step("Sesión iniciada exitosamente en " + loginTime + "ms");
        
        // Capturar screenshot después del login
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment("Screenshot Login Exitoso", "image/png", "png", screenshot);
        } catch (Exception e) {
            Allure.addAttachment("Screenshot Error", "text/plain", "No se pudo capturar screenshot: " + e.getMessage());
        }
    }
    
    @Step("Entrar al módulo SGP")
    private void entrarModuloSGP() {
        Allure.step("Navegando al módulo SGP");
        
        // Usar el método que ya funciona en PedidoPage
        pedidoPage.esperarSGPVisible();
        pedidoPage.clickSGP();
        
        Allure.step("Módulo SGP accedido correctamente");
    }
    
    @Step("Ingresar DNI del beneficiario")
    private void ingresarDNIBeneficiario() {
        Allure.step("Ingresando DNI del beneficiario");
        
        String dni = Vars.get("beneficiario.dni");
        pedidoPage.completarDatosGenerales(dni);
        
        Allure.step("DNI del beneficiario ingresado: " + dni);
    }
    
    @Step("Seleccionar tipo de pedido medicamento")
    private void seleccionarTipoPedidoMedicamento() {
        Allure.step("Seleccionando tipo de pedido: Medicamentos");
        
        // Usar el método que ya funciona en PedidoPage
        pedidoPage.seleccionarTipoPedidoYFecha();
        
        Allure.step("Tipo de pedido 'Medicamentos' seleccionado");
    }
    
    @Step("Completar datos mínimos requeridos")
    private void completarDatosMinimosRequeridos() {
        Allure.step("Completando SOLO datos mínimos para validar campos requeridos");
        
        // NO completar nada más - solo lo que ya se hizo:
        // 1. DNI del beneficiario ✅
        // 2. Tipo de pedido medicamentos ✅
        // 3. Fecha (se completa automáticamente en seleccionarTipoPedidoYFecha)
        
        // NO completar: diagnóstico, médico, farmacia, medicamento, etc.
        // Esto es intencional para que aparezcan los mensajes de campos requeridos
        
        Allure.step("Datos mínimos completados (solo DNI, tipo y fecha)");
    }
    
    @Step("Hacer click en enviar a auditoría")
    private void hacerClickEnviarAuditoria() {
        Allure.step("Haciendo click en 'Enviar a Auditoría'");
        
        pedidoPage.enviarAAuditoria();
        
        Allure.step("Click en 'Enviar a Auditoría' realizado");
        
        // Capturar screenshot después de enviar a auditoría
        try {
            Thread.sleep(2000); // Esperar que aparezcan los mensajes
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment("Screenshot Mensajes Validación", "image/png", "png", screenshot);
        } catch (Exception e) {
            Allure.addAttachment("Screenshot Error", "text/plain", "No se pudo capturar screenshot: " + e.getMessage());
        }
    }
    
    @Step("Validar mensajes de campos requeridos en pantalla")
    private void validarMensajesCamposRequeridosEnPantalla() {
        Allure.step("Validando mensajes de campos requeridos");
        
        // Esperar un momento para que aparezcan los mensajes de validación
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Mensajes específicos que deben aparecer según la aplicación
        String[] mensajesEsperados = {
            "Debe ingresar un diagnóstico presuntivo",
            "Debe indicar el médico solicitante",
            "Debe seleccionar una Farmacia de entrega",
            "Debe seleccionar al menos una opción entre Crónico, Internado, Oncológico, Urgente, Reposición o Ambulatorio",
            "No existen posiciones cargadas para el pedido",
            "Debe adjuntar al menos un pedido médico"
        };
        
        StringBuilder mensajesEncontrados = new StringBuilder();
        StringBuilder mensajesFaltantes = new StringBuilder();
        int mensajesValidos = 0;
        
        // Verificar cada mensaje esperado
        for (String mensaje : mensajesEsperados) {
            if (driver.getPageSource().contains(mensaje)) {
                mensajesEncontrados.append("✅ ").append(mensaje).append("\n");
                mensajesValidos++;
                Allure.step("Mensaje encontrado: " + mensaje);
            } else {
                mensajesFaltantes.append("❌ ").append(mensaje).append("\n");
                Allure.step("Mensaje NO encontrado: " + mensaje);
            }
        }
        
        // Agregar información al reporte de Allure
        Allure.addAttachment("Mensajes Encontrados", "text/plain", mensajesEncontrados.toString());
        if (mensajesFaltantes.length() > 0) {
            Allure.addAttachment("Mensajes Faltantes", "text/plain", mensajesFaltantes.toString());
        }
        
        // Verificar que al menos algunos mensajes de validación estén presentes
        assertTrue(mensajesValidos > 0, 
            "Deben aparecer al menos algunos mensajes de campos requeridos. " +
            "Mensajes encontrados: " + mensajesValidos + "/" + mensajesEsperados.length);
        
        // Verificar que el formulario permanezca visible (indicando que no se envió)
        boolean sigueEnFormulario = driver.findElements(By.xpath("//button[contains(text(),'Enviar a Auditoría')]")).size() > 0 ||
                                   driver.findElements(By.xpath("//form")).size() > 0 ||
                                   driver.findElements(By.xpath("//input")).size() > 0 ||
                                   driver.getCurrentUrl().contains("pedido") ||
                                   driver.getCurrentUrl().contains("formulario");
        
        if (sigueEnFormulario) {
            Allure.step("✅ El formulario permanece visible (no se envió el pedido)");
        } else {
            Allure.step("⚠️ No se pudo confirmar que el formulario permanezca visible");
        }
        
        // Verificar que no haya mensajes de éxito o confirmación
        String pageSource = driver.getPageSource().toLowerCase();
        boolean hayMensajeExito = pageSource.contains("enviado exitosamente") || 
                                 pageSource.contains("pedido enviado") ||
                                 pageSource.contains("orden generada") ||
                                 pageSource.contains("pedido aprobado");
        
        if (!hayMensajeExito) {
            Allure.step("✅ No hay mensajes de éxito (el pedido no se envió)");
        } else {
            Allure.step("⚠️ Se detectaron posibles mensajes de éxito");
        }
        
        Allure.step("Validación completada: " + mensajesValidos + "/" + mensajesEsperados.length + " mensajes encontrados");
    }
}

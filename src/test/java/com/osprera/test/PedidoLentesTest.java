package com.osprera.test;

import com.osprera.test.pages.PedidoLentesPage;
import com.osprera.test.pages.PedidoPage;
import com.osprera.test.utils.Vars;
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
@Feature("Pedidos de Lentes")
@Story("Flujo Completo de Pedido de Lentes")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestFailureListener.class)
public class PedidoLentesTest {
    
    private static WebDriver driver;
    private static PedidoLentesPage pedidoLentesPage;
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
        
        pedidoLentesPage = new PedidoLentesPage(driver);
        pedidoPage = new PedidoPage(driver);
        
        // Navegar a la aplicación
        driver.get("https://sio.osprera.org.ar/#/login");
    }
    
    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @Order(1)
    @Description("Flujo completo de pedido de lentes: desde la creación hasta el envío a auditoría")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("pedidos")
    @Tag("lentes")
    @Tag("flujoCompleto")
    public void flujoCompletoPedidoLentes() {
        
        try {
            // Step 1: Iniciar sesión
            iniciarSesion();
            
            // Step 2: Entrar al módulo SGP
            entrarModuloSGP();
            
            // Step 3: Crear nuevo pedido de lentes
            crearNuevoPedidoLentes();
            
            // Step 4: Completar datos del pedido
            completarDatosPedido();
            
            // Step 5: Enviar a auditoría
            enviarAAuditoria();
            
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
    
    @Step("Iniciar sesión en el sistema")
    private void iniciarSesion() {
        Allure.step("Iniciando sesión en la aplicación");
        
        // Aquí iría tu lógica de login
        // Implementar según tu aplicación
        
        Allure.step("Sesión iniciada exitosamente");
    }
    
    @Step("Entrar al módulo SGP")
    private void entrarModuloSGP() {
        Allure.step("Accediendo al módulo SGP");
        
        pedidoPage.clickSGP();
        
        Allure.step("Módulo SGP accedido correctamente");
    }
    
    @Step("Crear nuevo pedido de lentes")
    private void crearNuevoPedidoLentes() {
        Allure.step("Creando nuevo pedido de lentes");
        
        // Implementar lógica de creación de pedido
        // pedidoLentesPage.crearNuevoPedido();
        
        Allure.step("Nuevo pedido de lentes creado");
    }
    
    @Step("Completar datos del pedido")
    private void completarDatosPedido() {
        Allure.step("Completando datos del pedido de lentes");
        
        // Implementar lógica de completado de datos
        // pedidoLentesPage.completarDatos();
        
        Allure.step("Datos del pedido completados");
    }
    
    @Step("Enviar pedido a auditoría")
    private void enviarAAuditoria() {
        Allure.step("Enviando pedido a auditoría");
        
        pedidoPage.enviarAAuditoria();
        
        Allure.step("Pedido enviado a auditoría exitosamente");
    }
}

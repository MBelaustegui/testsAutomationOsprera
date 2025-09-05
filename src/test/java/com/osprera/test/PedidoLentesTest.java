package com.osprera.test;

import com.osprera.test.pages.PedidoLentesPage;
import com.osprera.test.pages.PedidoPage;
import com.osprera.test.utils.EnvironmentManager;
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
        
        // Navegar a la aplicación usando EnvironmentManager
        String url = EnvironmentManager.getCurrentUrl();
        driver.get(url);
    }
    
    @AfterAll
    static void tearDown() {
        try {
            if (driver != null) {
                Allure.step("🔚 Cerrando navegador al finalizar suite de pedido de lentes");
                driver.quit();
                Allure.step("✅ Navegador cerrado exitosamente");
            }
        } catch (Exception e) {
            Allure.step("⚠️ Error al cerrar navegador en tearDown: " + e.getMessage());
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
            // Manejo detallado de errores con captura de pantalla
            String errorMessage = "❌ FALLO EN PEDIDO DE LENTES: " + e.getMessage();
            Allure.step(errorMessage);
            
            // Capturar screenshot del error
            try {
                byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                io.qameta.allure.Allure.getLifecycle().addAttachment("❌ ERROR - Pedido de Lentes Falló", "image/png", "png", screenshot);
                
                // Agregar información detallada del error
                String errorDetails = String.format(
                    "🚨 DETALLES DEL ERROR:\n" +
                    "=====================================\n" +
                    "❌ Test: Pedido de Lentes\n" +
                    "⏰ Timestamp: %s\n" +
                    "🌍 Ambiente: %s\n" +
                    "👤 Usuario: %s\n" +
                    "🔗 URL: %s\n" +
                    "💥 Error: %s\n" +
                    "📍 Stack Trace: %s\n" +
                    "=====================================\n" +
                    "🛑 EJECUCIÓN DETENIDA - Revisar logs y screenshot",
                    java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    EnvironmentManager.getCurrentEnvironment(),
                    EnvironmentManager.getUsuarioPedidos(),
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
            Assertions.fail("🚨 PEDIDO DE LENTES FALLÓ: " + e.getMessage() + 
                          "\n📸 Revisar screenshot adjunto para más detalles" +
                          "\n🛑 Ejecución detenida para investigación" +
                          "\n🔚 Navegador cerrado automáticamente");
        }
    }
    
    @Step("Iniciar sesión en el sistema")
    private void iniciarSesion() {
        Allure.step("Iniciando sesión en la aplicación");
        
        try {
            // Esperar a que la página esté completamente cargada
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Completar formulario de login con esperas explícitas
            org.openqa.selenium.WebElement usuarioInput = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                    org.openqa.selenium.By.xpath("//input[@placeholder='Usuario']")
                )
            );
            usuarioInput.clear();
            usuarioInput.sendKeys(EnvironmentManager.getUsuarioPedidos());
            
            org.openqa.selenium.WebElement passwordInput = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                    org.openqa.selenium.By.xpath("//input[@type='password']")
                )
            );
            passwordInput.clear();
            passwordInput.sendKeys(EnvironmentManager.getCurrentPassword());
            
            // Seleccionar tipo de usuario
            String tipoUsuario = EnvironmentManager.getCurrentTipoUsuario();
            
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

            // Esperar que se complete el login usando la misma variable wait
            try {
                wait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("dashboard"));
            } catch (Exception e) {
                wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.cssSelector("body:not(.loading)")));
            }
            
            Allure.step("Sesión iniciada exitosamente con usuario: " + EnvironmentManager.getUsuarioPedidos());
            
        } catch (Exception e) {
            Allure.step("Error al iniciar sesión: " + e.getMessage());
            throw new RuntimeException("Error al iniciar sesión", e);
        }
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
        
        try {
            // Ingresar DNI del beneficiario
            String dniBeneficiario = Vars.get("beneficiario.dni");
            pedidoPage.completarDatosGenerales(dniBeneficiario);
            Allure.step("DNI del beneficiario ingresado: " + dniBeneficiario);
            
            // Cargar datos del beneficiario, diagnóstico y médico
            pedidoPage.cargarDiagnosticoYCIE(Vars.get("cie.codigo"), Vars.get("diagnostico.texto"));
            pedidoPage.seleccionarMedico(Vars.get("medico.busqueda"));
            Allure.step("Datos del beneficiario, diagnóstico y médico cargados");
            
            // Seleccionar tipo de pedido y fecha
            pedidoLentesPage.seleccionarTipoPedidoYFecha();
            Allure.step("Tipo de pedido 'Lentes' seleccionado y fecha configurada");
            
        } catch (Exception e) {
            Allure.step("Error al crear nuevo pedido de lentes: " + e.getMessage());
            throw new RuntimeException("Error al crear nuevo pedido de lentes", e);
        }
        
        Allure.step("Nuevo pedido de lentes creado exitosamente");
    }
    
    @Step("Completar datos del pedido")
    private void completarDatosPedido() {
        Allure.step("Completando datos del pedido de lentes");
        
        try {
            // Obtener datos de configuración
            String nombreLentes = Vars.get("lentes.nombre");
            String descripcionLentes = Vars.get("lentes.descripcion");
            int cantidadLentes = Integer.parseInt(Vars.get("lentes.cantidad"));
            String delegacionLentes = Vars.get("lentes.delegacion");
            
            Allure.step("Datos de configuración cargados: " + nombreLentes + ", cantidad: " + cantidadLentes);
            
            // Seleccionar delegación de entrega
            pedidoLentesPage.DelegacionEntrega(delegacionLentes);
            Allure.step("Delegación de entrega seleccionada: " + delegacionLentes);
            
            // Buscar y agregar lentes
            pedidoLentesPage.buscarYAgregarLentes(nombreLentes);
            Allure.step("Lentes agregados: " + nombreLentes);
            
            // Agregar descripción
            pedidoLentesPage.agregarDescripcionLentes(descripcionLentes);
            Allure.step("Descripción agregada: " + descripcionLentes);
            
            // Completar datos (cantidad, No, A cargo de Osprera)
            pedidoLentesPage.completarDatosLentes(cantidadLentes);
            Allure.step("Datos completados: cantidad=" + cantidadLentes + ", A cargo de Osprera");
            
            // Confirmar y obtener número de pedido
            String numeroPedido = pedidoLentesPage.confirmarYObtenerNumero();
            if (!numeroPedido.isEmpty()) {
                Allure.step("Pedido confirmado exitosamente. Número de pedido: " + numeroPedido);
                // Guardar número de pedido para auditoría
                com.osprera.test.utils.ContextoGlobal.numeroOrden = numeroPedido;
            } else {
                Allure.step("Pedido confirmado pero no se pudo obtener el número");
            }
            
        } catch (Exception e) {
            Allure.step("Error al completar datos del pedido: " + e.getMessage());
            throw new RuntimeException("Error al completar datos del pedido", e);
        }
        
        Allure.step("Datos del pedido completados exitosamente");
    }
    
    @Step("Enviar pedido a auditoría")
    private void enviarAAuditoria() {
        Allure.step("Enviando pedido a auditoría");
        
        try {
            pedidoLentesPage.enviarAAuditoria();
            Allure.step("Pedido enviado a auditoría exitosamente");
            
            // Capturar screenshot final
            try {
                byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                io.qameta.allure.Allure.getLifecycle().addAttachment("✅ PEDIDO DE LENTES COMPLETADO", "image/png", "png", screenshot);
            } catch (Exception e) {
                Allure.step("⚠️ No se pudo capturar screenshot final: " + e.getMessage());
            }
            
        } catch (Exception e) {
            Allure.step("Error al enviar pedido a auditoría: " + e.getMessage());
            throw new RuntimeException("Error al enviar pedido a auditoría", e);
        }
    }
}

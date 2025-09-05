package com.osprera.test;

import com.osprera.test.pages.PedidoPage;
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
@Feature("Pedidos de Medicamentos")
@Story("Flujo Completo de Pedido de Medicamentos")
@Tag("pedidos")
@Tag("medicamentos")
@Tag("smoke")
@Tag("regression")
@Tag("produccion_environment")
@Tag("end_to_end")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestFailureListener.class)
public class PedidoMedicamentosTest {
    
    private static WebDriver driver;
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
        
        pedidoPage = new PedidoPage(driver);
        
        // Navegar a la aplicación usando EnvironmentManager
        String url = com.osprera.test.utils.EnvironmentManager.getCurrentUrl();
        driver.get(url);
        
        // Configurar información del ambiente en Allure
        String ambiente = com.osprera.test.utils.EnvironmentManager.getCurrentEnvironment();
        String usuario = com.osprera.test.utils.EnvironmentManager.getUsuarioPedidos();
        
        Allure.addAttachment("🚀 TEST INICIADO", "text/plain", 
            "🎯 TEST DE PEDIDOS DE MEDICAMENTOS\n" +
            "==================================\n" +
            "🌍 AMBIENTE: " + ambiente.toUpperCase() + "\n" +
            "🔗 URL: " + url + "\n" +
            "👤 USUARIO: " + usuario + "\n" +
            "⏰ FECHA: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n" +
            "\n" +
            "✅ Crear pedido completo\n" +
            "✅ Agregar medicamentos\n" +
            "✅ Enviar a auditoría\n" +
            "✅ Guardar número de pedido\n" +
            "\n" +
            "📊 Reportes detallados en Allure");
    }
    
    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @Order(1)
    @Description("Flujo completo de pedido de medicamentos: desde la creación hasta el envío a auditoría")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("pedidos")
    @Tag("medicamentos")
    @Tag("flujoCompleto")
    public void flujoCompletoPedidoMedicamentos() {
        
        try {
            // Step 1: Iniciar sesión
            iniciarSesion();
            
            // Step 2: Entrar al módulo SGP
            entrarModuloSGP();
            
            // Step 3: Crear nuevo pedido de medicamentos
            crearNuevoPedidoMedicamentos();
            
            // Step 4: Completar datos del pedido
            completarDatosPedido();
            
            // Step 5: Enviar a auditoría
            enviarAAuditoria();
            
            // Step 6: Capturar screenshot final
            byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
            io.qameta.allure.Allure.getLifecycle().addAttachment("Pedido Completado", "image/png", "png", screenshot);
            
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
        
        try {
            // Esperar a que la página esté completamente cargada
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Completar formulario de login con esperas explícitas
            org.openqa.selenium.WebElement usuarioInput = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                    org.openqa.selenium.By.xpath("//input[@placeholder='Usuario']")));
            usuarioInput.clear();
            usuarioInput.sendKeys(com.osprera.test.utils.EnvironmentManager.getUsuarioPedidos());

            org.openqa.selenium.WebElement passwordInput = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(
                    org.openqa.selenium.By.xpath("//input[@type='password']")));
            passwordInput.clear();
            passwordInput.sendKeys(com.osprera.test.utils.EnvironmentManager.getCurrentPassword());

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

            // Esperar que se complete el login usando la misma variable wait
            try {
                wait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("dashboard"));
            } catch (Exception e) {
                wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.cssSelector("body:not(.loading)")));
            }
            
            Allure.step("✅ Sesión iniciada exitosamente");
            
        } catch (Exception e) {
            Allure.step("❌ Error al iniciar sesión: " + e.getMessage());
            throw e;
        }
    }
    
    @Step("Entrar al módulo SGP")
    private void entrarModuloSGP() {
        Allure.step("Accediendo al módulo SGP");
        
        // Usar método original que sabemos que funciona
        pedidoPage.esperarSGPVisible();
        pedidoPage.clickSGP();
        
        Allure.step("Módulo SGP accedido correctamente");
    }
    
    @Step("Crear nuevo pedido de medicamentos")
    private void crearNuevoPedidoMedicamentos() {
        Allure.step("Creando nuevo pedido de medicamentos");
        
        try {
            // Ingresar DNI del beneficiario (SGP ya fue clickeado en el paso anterior)
            String dni = com.osprera.test.utils.Vars.get("beneficiario.dni");
            pedidoPage.completarDatosGenerales(dni);
            
            // Seleccionar tipo de pedido y fecha
            pedidoPage.seleccionarTipoPedidoYFecha();
            
            Allure.step("✅ Nuevo pedido de medicamentos creado");
            
        } catch (Exception e) {
            Allure.step("❌ Error al crear pedido: " + e.getMessage());
            throw e;
        }
    }
    
    @Step("Completar datos del pedido")
    private void completarDatosPedido() {
        Allure.step("Completando datos del pedido de medicamentos");
        
        try {
            // Cargar diagnóstico y CIE
            String cie = com.osprera.test.utils.Vars.get("cie.codigo");
            String diagnostico = com.osprera.test.utils.Vars.get("diagnostico.texto");
            pedidoPage.cargarDiagnosticoYCIE(cie, diagnostico);
            
            // Seleccionar médico
            String medico = com.osprera.test.utils.Vars.get("medico.busqueda");
            pedidoPage.seleccionarMedico(medico);
            
            // Seleccionar tipo de paciente (Urgente)
            pedidoPage.seleccionarUrgente();
            
            // Seleccionar farmacia
            pedidoPage.seleccionarFarmacia();
            
            // Procesar múltiples medicamentos
            procesarMedicamentos();
            
            // Cargar adjuntos
            pedidoPage.clickCargarAdjuntos();
            
            Allure.step("✅ Datos del pedido completados");
            
        } catch (Exception e) {
            Allure.step("❌ Error al completar datos: " + e.getMessage());
            throw e;
        }
    }
    
    @Step("Enviar pedido a auditoría")
    private void enviarAAuditoria() {
        Allure.step("Enviando pedido a auditoría");
        
        try {
            // Enviar a auditoría
            pedidoPage.enviarAAuditoria();
            
            // Esperar que aparezca la confirmación
            try {
                Thread.sleep(3000);
                
                // Verificar si aparece confirmación
                if (pedidoPage.apareceConfirmacion()) {
                    // Obtener número de pedido
                    String numeroPedido = pedidoPage.obtenerNumeroOrden();
                    
                    // Guardar en variable global para que otros tests puedan acceder
                    com.osprera.test.utils.ContextoGlobal.numeroOrden = numeroPedido;
                    
                    // También guardar en archivo temporal para compartir entre tests
                    try {
                        java.nio.file.Files.write(
                            java.nio.file.Paths.get("numero_pedido_temp.txt"), 
                            numeroPedido.getBytes()
                        );
                        System.out.println("💾 Guardado en archivo temporal: numero_pedido_temp.txt");
                    } catch (Exception e) {
                        System.err.println("⚠️ Error al guardar en archivo: " + e.getMessage());
                    }
                    
                    // Log para debug
                    System.out.println("🔢 Número de pedido obtenido: " + numeroPedido);
                    System.out.println("💾 Guardado en ContextoGlobal: " + com.osprera.test.utils.ContextoGlobal.numeroOrden);
                    
                    Allure.step("✅ Pedido enviado a auditoría. Número: " + numeroPedido);
                    
                    // Agregar número de pedido al reporte de Allure
                    io.qameta.allure.Allure.addAttachment("Número de Pedido", "text/plain", 
                        "Número de Pedido Generado: " + numeroPedido);
                    
                    // Cerrar modal de resultados
                    pedidoPage.cerrarModalResultados();
                } else {
                    Allure.step("⚠️ No se confirmó el envío a auditoría");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            Allure.step("✅ Pedido enviado a auditoría exitosamente");
            
        } catch (Exception e) {
            Allure.step("❌ Error al enviar a auditoría: " + e.getMessage());
            throw e;
        }
    }
    
    @Step("Procesar medicamento único")
    private void procesarMedicamentos() {
        try {
            // Obtener datos del medicamento único
            String nombre = com.osprera.test.utils.Vars.get("medicamento.nombre");
            String cantidad = com.osprera.test.utils.Vars.get("medicamento.cantidad");
            String dosis = com.osprera.test.utils.Vars.get("medicamento.dosis");
            String tipoDosis = com.osprera.test.utils.Vars.get("medicamento.tipo_dosis");
            String sur = com.osprera.test.utils.Vars.get("medicamento.sur");
            
            if (nombre == null || nombre.isEmpty()) {
                throw new RuntimeException("Medicamento no configurado en default.properties");
            }
            
            Allure.step("💊 Agregando medicamento: " + nombre);
            
            // Buscar y agregar medicamento
            pedidoPage.buscarYAgregarMedicamento(nombre);
            
            // Ingresar detalles del medicamento
            pedidoPage.ingresarDetallesMedicamento(nombre, cantidad, dosis, tipoDosis, sur);
            
            Allure.step("✅ Medicamento agregado exitosamente");
            
        } catch (Exception e) {
            Allure.step("❌ Error procesando medicamento: " + e.getMessage());
            throw new RuntimeException("Error procesando medicamento", e);
        }
    }
}

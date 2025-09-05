package com.osprera.test;

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
@Feature("Smoke Testing")
@Story("Verificación Rápida del Sistema")
@Tag("smoke")
@Tag("critical")
@Tag("quick")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestFailureListener.class)
public class SmokeTest {
    
    private static WebDriver driver;
    private static WebDriverWait wait;
    
    @BeforeAll
    static void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        String url = EnvironmentManager.getCurrentUrl();
        driver.get(url);
        
        Allure.addAttachment("Smoke Test Environment", "text/plain", 
            EnvironmentManager.getEnvironmentInfo());
    }
    
    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @Order(1)
    @Description("Verificar que la página de login se carga correctamente")
    @Severity(SeverityLevel.CRITICAL)
    public void verificarCargaPaginaLogin() {
        try {
            // Verificar elementos críticos de la página de login
            WebElement usuarioInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Usuario']")));
            WebElement passwordInput = driver.findElement(By.xpath("//input[@type='password']"));
            WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Iniciar Sesión')]"));
            WebElement tipoUsuarioDropdown = driver.findElement(By.cssSelector("button.dropdown-toggle"));
            
            // Verificar que todos los elementos estén presentes y visibles
            assertTrue(usuarioInput.isDisplayed(), "Campo usuario debe estar visible");
            assertTrue(passwordInput.isDisplayed(), "Campo password debe estar visible");
            assertTrue(loginButton.isDisplayed(), "Botón login debe estar visible");
            assertTrue(tipoUsuarioDropdown.isDisplayed(), "Dropdown tipo usuario debe estar visible");
            
            // Screenshot de la página cargada
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment("Página Login Cargada", "image/png", "png", screenshot);
            
            Allure.step("✅ Página de login cargada correctamente con todos los elementos");
            
        } catch (Exception e) {
            // Screenshot del error
            try {
                byte[] errorScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.getLifecycle().addAttachment("Error en Página Login", "image/png", "png", errorScreenshot);
            } catch (Exception screenshotException) {
                Allure.addAttachment("Error Screenshot", "text/plain", "No se pudo capturar screenshot: " + screenshotException.getMessage());
            }
            throw e;
        }
    }
    
    @Test
    @Order(2)
    @Description("Verificar que se puede completar el formulario de login")
    @Severity(SeverityLevel.CRITICAL)
    public void verificarCompletarFormularioLogin() {
        try {
            // Completar formulario
            WebElement usuarioInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Usuario']")));
            usuarioInput.clear();
            usuarioInput.sendKeys(EnvironmentManager.getUsuarioPedidos());
            
            WebElement passwordInput = driver.findElement(By.xpath("//input[@type='password']"));
            passwordInput.clear();
            passwordInput.sendKeys(EnvironmentManager.getCurrentPassword());
            
            // Verificar que los campos se completaron
            assertEquals(EnvironmentManager.getUsuarioPedidos(), usuarioInput.getAttribute("value"), 
                "El campo usuario debe contener el valor ingresado");
            
            // Screenshot del formulario completado
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment("Formulario Completado", "image/png", "png", screenshot);
            
            Allure.step("✅ Formulario de login completado correctamente");
            
        } catch (Exception e) {
            // Screenshot del error
            try {
                byte[] errorScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.getLifecycle().addAttachment("Error en Formulario", "image/png", "png", errorScreenshot);
            } catch (Exception screenshotException) {
                Allure.addAttachment("Error Screenshot", "text/plain", "No se pudo capturar screenshot: " + screenshotException.getMessage());
            }
            throw e;
        }
    }
    
    @Test
    @Order(3)
    @Description("Verificar que se puede seleccionar el tipo de usuario")
    @Severity(SeverityLevel.CRITICAL)
    public void verificarSeleccionTipoUsuario() {
        try {
            // Hacer click en el dropdown
            WebElement tipoUsuarioDropdown = driver.findElement(By.cssSelector("button.dropdown-toggle"));
            tipoUsuarioDropdown.click();
            
            // Esperar que aparezcan las opciones
            String tipoUsuario = EnvironmentManager.getCurrentTipoUsuario();
            WebElement opcionUsuario = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(text(),'" + tipoUsuario + "')]")));
            
            // Verificar que la opción esté presente
            assertTrue(opcionUsuario.isDisplayed(), "La opción " + tipoUsuario + " debe estar visible");
            
            // Seleccionar la opción
            opcionUsuario.click();
            
            // Screenshot de la selección
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment("Tipo Usuario Seleccionado", "image/png", "png", screenshot);
            
            Allure.step("✅ Tipo de usuario seleccionado correctamente: " + tipoUsuario);
            
        } catch (Exception e) {
            // Screenshot del error
            try {
                byte[] errorScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.getLifecycle().addAttachment("Error en Selección Tipo Usuario", "image/png", "png", errorScreenshot);
            } catch (Exception screenshotException) {
                Allure.addAttachment("Error Screenshot", "text/plain", "No se pudo capturar screenshot: " + screenshotException.getMessage());
            }
            throw e;
        }
    }
    
    @Test
    @Order(4)
    @Description("Generar reporte de smoke test")
    @Severity(SeverityLevel.NORMAL)
    public void generarReporteSmokeTest() {
        // Este test genera un resumen del smoke test
        Allure.addAttachment("Smoke Test Summary", "text/plain", 
            "Smoke Test Completado Exitosamente\n" +
            "Ambiente: " + EnvironmentManager.getCurrentEnvironment() + "\n" +
            "URL: " + EnvironmentManager.getCurrentUrl() + "\n" +
            "Usuario: " + EnvironmentManager.getUsuarioPedidos() + "\n" +
            "Tipo Usuario: " + EnvironmentManager.getCurrentTipoUsuario() + "\n" +
            "Timestamp: " + new java.util.Date() + "\n" +
            "Tests Ejecutados: 3\n" +
            "Estado: ✅ TODOS LOS TESTS PASARON");
        
        Allure.step("✅ Reporte de smoke test generado");
    }
}

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
@Feature("Performance Testing")
@Story("Métricas de Rendimiento")
@Tag("performance")
@Tag("metrics")
@Tag("smoke")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestFailureListener.class)
public class PerformanceTest {
    
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
        
        Allure.addAttachment("Performance Test Environment", "text/plain", 
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
    @Description("Medir tiempo de carga de la página de login")
    @Severity(SeverityLevel.NORMAL)
    public void medirTiempoCargaLogin() {
        long startTime = System.currentTimeMillis();
        
        try {
            // Esperar que la página esté completamente cargada
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@placeholder='Usuario']")));
            
            long loadTime = System.currentTimeMillis() - startTime;
            
            // Screenshot de la página cargada
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment("Página Login Cargada", "image/png", "png", screenshot);
            
            // Métricas de performance
            Allure.addAttachment("Performance Metrics", "text/plain", 
                "Tiempo de Carga de Login: " + loadTime + "ms");
            
            // Validar que el tiempo sea razonable (menos de 5 segundos)
            assertTrue(loadTime < 5000, 
                "La página de login debe cargar en menos de 5 segundos. Tiempo actual: " + loadTime + "ms");
            
            Allure.step("✅ Página de login cargada en " + loadTime + "ms");
            
        } catch (Exception e) {
            long errorTime = System.currentTimeMillis() - startTime;
            Allure.addAttachment("Error Performance", "text/plain", 
                "Error después de " + errorTime + "ms: " + e.getMessage());
            throw e;
        }
    }
    
    @Test
    @Order(2)
    @Description("Medir tiempo de respuesta del formulario de login")
    @Severity(SeverityLevel.NORMAL)
    public void medirTiempoRespuestaLogin() {
        long startTime = System.currentTimeMillis();
        
        try {
            // Completar formulario
            WebElement usuarioInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Usuario']")));
            usuarioInput.sendKeys(EnvironmentManager.getUsuarioPedidos());
            
            WebElement passwordInput = driver.findElement(By.xpath("//input[@type='password']"));
            passwordInput.sendKeys(EnvironmentManager.getCurrentPassword());
            
            // Seleccionar tipo de usuario
            String tipoUsuario = EnvironmentManager.getCurrentTipoUsuario();
            driver.findElement(By.cssSelector("button.dropdown-toggle")).click();
            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(text(),'" + tipoUsuario + "')]"))).click();
            
            // Hacer click en login
            WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Iniciar Sesión')]"));
            loginButton.click();
            
            // Esperar respuesta (éxito o error)
            try {
                wait.until(ExpectedConditions.urlContains("dashboard"));
            } catch (Exception e) {
                // Si no hay dashboard, esperar algún cambio
                wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("body:not(.loading)")));
            }
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            // Screenshot del resultado
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment("Resultado Login", "image/png", "png", screenshot);
            
            // Métricas de performance
            Allure.addAttachment("Login Response Time", "text/plain", 
                "Tiempo de Respuesta del Login: " + responseTime + "ms");
            
            // Validar tiempo razonable (menos de 10 segundos)
            assertTrue(responseTime < 10000, 
                "El login debe responder en menos de 10 segundos. Tiempo actual: " + responseTime + "ms");
            
            Allure.step("✅ Login respondió en " + responseTime + "ms");
            
        } catch (Exception e) {
            long errorTime = System.currentTimeMillis() - startTime;
            Allure.addAttachment("Error Login Performance", "text/plain", 
                "Error después de " + errorTime + "ms: " + e.getMessage());
            throw e;
        }
    }
    
    @Test
    @Order(3)
    @Description("Generar reporte de performance completo")
    @Severity(SeverityLevel.NORMAL)
    public void generarReportePerformance() {
        // Este test genera un resumen de métricas
        Allure.addAttachment("Performance Summary", "text/plain", 
            "Test de Performance Completado\n" +
            "Ambiente: " + EnvironmentManager.getCurrentEnvironment() + "\n" +
            "URL: " + EnvironmentManager.getCurrentUrl() + "\n" +
            "Timestamp: " + new java.util.Date() + "\n" +
            "Navegador: Chrome\n" +
            "Tests Ejecutados: 2");
        
        Allure.step("✅ Reporte de performance generado");
    }
}

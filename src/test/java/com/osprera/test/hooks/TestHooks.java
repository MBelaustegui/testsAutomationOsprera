package com.osprera.test.hooks;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;

/**
 * Hooks centralizados para JUnit 5
 * Maneja la configuración del WebDriver y operaciones comunes
 */
public class TestHooks implements TestWatcher {
    
    private static WebDriver driver;
    private static WebDriverWait wait;
    
    /**
     * Configura y retorna el WebDriver configurado
     */
    public static WebDriver getDriver() {
        if (driver == null) {
            setupDriver();
        }
        return driver;
    }
    
    /**
     * Retorna el WebDriverWait configurado
     */
    public static WebDriverWait getWait() {
        if (wait == null) {
            setupDriver();
        }
        return wait;
    }
    
    /**
     * Configura el WebDriver con opciones optimizadas
     */
    private static void setupDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    /**
     * Cierra el WebDriver
     */
    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            wait = null;
        }
    }
    
    /**
     * Navega a la URL de login
     */
    public static void navigateToLogin() {
        driver.get("https://sio.osprera.org.ar/#/login");
    }
    
    /**
     * Captura screenshot y lo adjunta al reporte de Allure
     */
    public static void captureScreenshot(String name) {
        try {
            if (driver instanceof TakesScreenshot) {
                File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                File dst = new File("target/screenshots/" + name + ".png");
                dst.getParentFile().mkdirs();
                Files.copy(src.toPath(), dst.toPath());
                
                // Adjuntar al reporte de Allure
                Allure.addAttachment(name, "image/png", 
                    new String(Files.readAllBytes(dst.toPath())));
            }
        } catch (IOException e) {
            Allure.addAttachment("Error Screenshot", "text/plain", 
                "No se pudo capturar screenshot: " + e.getMessage());
        }
    }
    
    /**
     * Hook que se ejecuta cuando un test falla
     */
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        captureScreenshot("test_failed_" + context.getTestMethod().get().getName());
    }
    
    /**
     * Hook que se ejecuta cuando un test es exitoso
     */
    @Override
    public void testSuccessful(ExtensionContext context) {
        // Opcional: capturar screenshot en tests exitosos
        // captureScreenshot("test_success_" + context.getTestMethod().get().getName());
    }
}

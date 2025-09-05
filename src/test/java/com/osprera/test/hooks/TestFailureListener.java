package com.osprera.test.hooks;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;

public class TestFailureListener implements TestWatcher {
    
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        try {
            // Intentar obtener el driver del test que falló
            WebDriver driver = getDriverFromTest(context);
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.getLifecycle().addAttachment("Test Failed Screenshot", "image/png", "png", screenshot);
                Allure.addAttachment("Test Failure Details", "text/plain", 
                    "Test: " + context.getTestMethod().get().getName() + "\n" +
                    "Error: " + cause.getMessage() + "\n" +
                    "Stack Trace: " + cause.toString());
            } else {
                Allure.addAttachment("Test Failure Details", "text/plain", 
                    "Test: " + context.getTestMethod().get().getName() + "\n" +
                    "Error: " + cause.getMessage() + "\n" +
                    "No se pudo capturar screenshot - Driver no disponible");
            }
        } catch (Exception e) {
            Allure.addAttachment("Test Failure Details", "text/plain", 
                "Test: " + context.getTestMethod().get().getName() + "\n" +
                "Error: " + cause.getMessage() + "\n" +
                "Error al capturar screenshot: " + e.getMessage());
        }
    }
    
    private WebDriver getDriverFromTest(ExtensionContext context) {
        try {
            // Obtener la instancia del test
            Object testInstance = context.getRequiredTestInstance();
            
            // Buscar el campo 'driver' en la clase del test
            Field driverField = findField(testInstance.getClass(), "driver");
            if (driverField != null) {
                driverField.setAccessible(true);
                Object driver = driverField.get(testInstance);
                if (driver instanceof WebDriver) {
                    return (WebDriver) driver;
                }
            }
            
            // Si no se encuentra 'driver', buscar en campos estáticos
            Field[] fields = testInstance.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (WebDriver.class.isAssignableFrom(field.getType()) && 
                    java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    Object driver = field.get(null);
                    if (driver instanceof WebDriver) {
                        return (WebDriver) driver;
                    }
                }
            }
            
        } catch (Exception e) {
            // Ignorar errores de reflexión
        }
        return null;
    }
    
    private Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return findField(superClass, fieldName);
            }
        }
        return null;
    }
}

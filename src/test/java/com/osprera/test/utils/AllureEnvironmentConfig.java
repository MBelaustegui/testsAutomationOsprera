package com.osprera.test.utils;

import io.qameta.allure.Allure;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AllureEnvironmentConfig {
    
    public static void configureEnvironment() {
        String ambiente = EnvironmentManager.getCurrentEnvironment();
        String url = EnvironmentManager.getCurrentUrl();
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        
        // Configurar información del ambiente
        Allure.addAttachment("Environment Info", "text/plain", 
            "🌍 AMBIENTE: " + ambiente.toUpperCase() + "\n" +
            "🔗 URL: " + url + "\n" +
            "⏰ FECHA: " + fecha + "\n" +
            "👤 USUARIO: " + EnvironmentManager.getUsuarioPedidos() + "\n" +
            "🔧 BROWSER: Chrome\n" +
            "💻 OS: Windows 11\n" +
            "☕ JAVA: " + System.getProperty("java.version") + "\n" +
            "🏗️ MAVEN: " + System.getProperty("maven.version", "3.9.0") + "\n" +
            "🤖 SELENIUM: 4.21.0"
        );
        
        // Agregar tags visuales según el ambiente
        if (ambiente.equals("qa")) {
            Allure.addAttachment("QA Environment", "text/plain", 
                "🧪 AMBIENTE DE PRUEBAS\n" +
                "====================\n" +
                "✅ Datos de prueba\n" +
                "✅ Usuarios de prueba\n" +
                "✅ Configuración segura"
            );
        } else if (ambiente.equals("produccion")) {
            Allure.addAttachment("PRODUCTION Environment", "text/plain", 
                "🚨 AMBIENTE DE PRODUCCIÓN\n" +
                "========================\n" +
                "⚠️ Datos reales\n" +
                "⚠️ Usuarios reales\n" +
                "⚠️ Configuración crítica"
            );
        }
    }
}

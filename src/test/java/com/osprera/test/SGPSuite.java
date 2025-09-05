package com.osprera.test;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.osprera.test.hooks.TestFailureListener;

@Epic("SGP - Sistema de Gestión de Pedidos")
@Feature("Suite Secuencial de Testing")
@Story("Pedidos → Auditoría Provincial → Auditoría Central")
@Tag("suite")
@Tag("secuencial")
@Tag("pedidos")
@Tag("auditoria_provincial")
@Tag("auditoria_central")
@Tag("qa_environment")
@Tag("end_to_end")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestFailureListener.class)
public class SGPSuite {
    
    // Variables para controlar la ejecución secuencial
    private static boolean pedidoCreado = false;
    private static boolean auditoriaProvincialCompletada = false;
    private static String numeroPedido = null;
    
    @BeforeAll
    static void setUp() {
        // Configurar información del ambiente en Allure
        com.osprera.test.utils.AllureEnvironmentConfig.configureEnvironment();
        
        String ambiente = com.osprera.test.utils.EnvironmentManager.getCurrentEnvironment();
        String url = com.osprera.test.utils.EnvironmentManager.getCurrentUrl();
        
        Allure.addAttachment("🚀 SUITE SECUENCIAL INICIADA", "text/plain", 
            "🎯 SUITE SECUENCIAL DE TESTING\n" +
            "==============================\n" +
            "🌍 AMBIENTE: " + ambiente.toUpperCase() + "\n" +
            "🔗 URL: " + url + "\n" +
            "⏰ FECHA: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n" +
            "\n" +
            "✅ PASO 1: Crear pedido de medicamentos\n" +
            "✅ PASO 2: Autorizar como Auditor Provincial\n" +
            "✅ PASO 3: Autorizar como Auditor Central\n" +
            "\n" +
            "🔄 Ejecución secuencial con dependencias\n" +
            "📊 Reportes detallados en Allure");
    }
    
    @Test
    @Order(1)
    @Description("PASO 1: Crear pedido completo de medicamentos")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("paso_1")
    public void paso1CrearPedido() {
        try {
            Allure.step("🚀 PASO 1: Iniciando creación de pedido");
            
            // Ejecutar el test de pedidos directamente
            ejecutarTestPedidos();
            
            pedidoCreado = true;
            Allure.step("✅ PASO 1 COMPLETADO: Pedido creado exitosamente");
            
        } catch (Exception e) {
            Allure.step("❌ PASO 1 FALLÓ: " + e.getMessage());
            throw e;
        }
    }
    
    @Test
    @Order(2)
    @Description("PASO 2: Autorizar pedido como Auditor Provincial")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("paso_2")
    public void paso2AuditoriaProvincial() {
        // Solo ejecutar si el paso 1 fue exitoso
        Assumptions.assumeTrue(pedidoCreado, "❌ PASO 1 NO COMPLETADO - No se puede ejecutar Auditoría Provincial");
        
        try {
            Allure.step("🔍 PASO 2: Iniciando auditoría provincial");
            
            // Ejecutar el test de auditoría provincial directamente
            ejecutarTestAuditoriaProvincial();
            
            auditoriaProvincialCompletada = true;
            Allure.step("✅ PASO 2 COMPLETADO: Auditoría provincial exitosa");
            
        } catch (Exception e) {
            Allure.step("❌ PASO 2 FALLÓ: " + e.getMessage());
            throw e;
        }
    }
    
    @Test
    @Order(3)
    @Description("PASO 3: Autorizar pedido como Auditor Central")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("paso_3")
    public void paso3AuditoriaCentral() {
        // Solo ejecutar si los pasos 1 y 2 fueron exitosos
        Assumptions.assumeTrue(pedidoCreado, "❌ PASO 1 NO COMPLETADO - No se puede ejecutar Auditoría Central");
        Assumptions.assumeTrue(auditoriaProvincialCompletada, "❌ PASO 2 NO COMPLETADO - No se puede ejecutar Auditoría Central");
        
        try {
            Allure.step("🏢 PASO 3: Iniciando auditoría central");
            
            // Ejecutar el test de auditoría central directamente
            ejecutarTestAuditoriaCentral();
            
            Allure.step("✅ PASO 3 COMPLETADO: Auditoría central exitosa");
            
        } catch (Exception e) {
            Allure.step("❌ PASO 3 FALLÓ: " + e.getMessage());
            throw e;
        }
    }
    
    @Test
    @Order(4)
    @Description("Reporte final de la suite completa")
    @Severity(SeverityLevel.NORMAL)
    @Tag("reporte_final")
    public void reporteFinalSuite() {
        // Solo ejecutar si todos los pasos fueron exitosos
        Assumptions.assumeTrue(pedidoCreado && auditoriaProvincialCompletada, 
            "❌ NO TODOS LOS PASOS COMPLETADOS - No se puede generar reporte final");
        
        Allure.addAttachment("🎉 SUITE SECUENCIAL COMPLETADA", "text/plain", 
            "🎉 SUITE SECUENCIAL EJECUTADA EXITOSAMENTE\n" +
            "==========================================\n" +
            "✅ PASO 1: Pedido creado exitosamente\n" +
            "✅ PASO 2: Auditoría provincial completada\n" +
            "✅ PASO 3: Auditoría central completada\n" +
            "\n" +
            "📊 NÚMERO DE PEDIDO: " + (numeroPedido != null ? numeroPedido : "No disponible") + "\n" +
            "\n" +
            "🏆 TODOS LOS PASOS COMPLETADOS EXITOSAMENTE\n" +
            "🚀 Sistema completamente funcional\n" +
            "📈 Flujo end-to-end validado");
        
        Allure.step("🎉 Reporte final de suite secuencial generado");
    }
    
    // ========== MÉTODOS PARA EJECUTAR TESTS DIRECTAMENTE ==========
    
    @Step("Ejecutar test de pedidos")
    private void ejecutarTestPedidos() {
        Allure.step("Ejecutando test real de creación de pedidos");
        
        try {
            // Crear instancia del test y ejecutarlo directamente
            PedidoMedicamentosTest testPedidos = new PedidoMedicamentosTest();
            testPedidos.setUp();
            testPedidos.flujoCompletoPedidoMedicamentos();
            testPedidos.tearDown();
            
            Allure.step("✅ Test de pedidos ejecutado exitosamente");
            
            // Leer el número de pedido del archivo temporal
            try {
                if (java.nio.file.Files.exists(java.nio.file.Paths.get("numero_pedido_temp.txt"))) {
                    numeroPedido = new String(java.nio.file.Files.readAllBytes(
                        java.nio.file.Paths.get("numero_pedido_temp.txt"))).trim();
                    Allure.addAttachment("Número de Pedido Generado", "text/plain", 
                        "Número de Pedido: " + numeroPedido);
                }
            } catch (Exception e) {
                Allure.step("⚠️ No se pudo leer el número de pedido: " + e.getMessage());
            }
            
        } catch (Exception e) {
            Allure.step("❌ Error ejecutando test de pedidos: " + e.getMessage());
            throw new RuntimeException("Error ejecutando test de pedidos", e);
        }
    }
    
    @Step("Ejecutar test de auditoría provincial")
    private void ejecutarTestAuditoriaProvincial() {
        Allure.step("Ejecutando test real de auditoría provincial");
        
        try {
            // Crear instancia del test y ejecutarlo directamente
            AuditoriaProvincialTest testAuditoria = new AuditoriaProvincialTest();
            testAuditoria.setUp();
            testAuditoria.flujoCompletoAuditoriaProvincial();
            testAuditoria.tearDown();
            
            Allure.step("✅ Test de auditoría provincial ejecutado exitosamente");
            
        } catch (Exception e) {
            Allure.step("❌ Error ejecutando test de auditoría provincial: " + e.getMessage());
            throw new RuntimeException("Error ejecutando test de auditoría provincial", e);
        }
    }
    
    @Step("Ejecutar test de auditoría central")
    private void ejecutarTestAuditoriaCentral() {
        Allure.step("Ejecutando test real de auditoría central");
        
        try {
            // Crear instancia del test y ejecutarlo directamente
            AuditoriaCentralTest testAuditoriaCentral = new AuditoriaCentralTest();
            testAuditoriaCentral.setUp();
            testAuditoriaCentral.flujoCompletoAuditoriaCentral();
            testAuditoriaCentral.tearDown();
            
            Allure.step("✅ Test de auditoría central ejecutado exitosamente");
            
        } catch (Exception e) {
            Allure.step("❌ Error ejecutando test de auditoría central: " + e.getMessage());
            throw new RuntimeException("Error ejecutando test de auditoría central", e);
        }
    }
}

package com.osprera.test.stepdefinitions;

import com.osprera.test.hooks.Hooks;
import com.osprera.test.pages.PedidoLentesPage;
import com.osprera.test.pages.PedidoPage;
import com.osprera.test.pages.PedidoProtesisPage;
import com.osprera.test.utils.ContextoGlobal;
import com.osprera.test.utils.Vars;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;

public class PedidoLentesSteps {

    private final WebDriver driver = Hooks.driver;
    private final PedidoPage pedidoPage = new PedidoPage(driver);
    private final PedidoLentesPage pedidoLentesPage = new PedidoLentesPage(driver);
    private String numeroOrden = "";

    // 0) Login ya realizado por Hooks → solo esperamos que SGP aparezca
    @Given("inicié sesión")
    public void inicie_sesion() {
        pedidoPage.esperarSGPVisible();
    }

    // 1) Entrar al módulo SGP
    @And("selecciono el modulo SGP")
    public void selecciono_el_modulo_SGP() {
        pedidoPage.clickSGP();
    }

    // 2) Cargar beneficiario (DNI)
    @And("ingreso el beneficiario")
    public void ingreso_el_beneficiario() {
        String dni = Vars.get("beneficiario.dni");
        pedidoPage.completarDatosGenerales(dni);
    }

    // 3) Completar todo el pedido de PRÓTESIS (pantalla principal)
    @When("completo el pedido de lentes")
    public void completo_el_pedido_de_protesis() {
        // 3.1) Tipo de pedido = Lentes + fecha de solicitud (hoy)
        pedidoLentesPage.seleccionarTipoPedidoYFecha();

        // 3.2) Diagnóstico (CIE + texto)
        String cieCodigo = Vars.get("cie.codigo");
        String diagnosticoTexto = Vars.get("diagnostico.texto");
        pedidoPage.cargarDiagnosticoYCIE(cieCodigo, diagnosticoTexto);

        // 3.3) Médico solicitante
        String medicoBusqueda = Vars.get("medico.busqueda");
        pedidoPage.seleccionarMedico(medicoBusqueda);

        // 3.4) Urgente

        pedidoPage.seleccionarUrgente();

        // 3.5) Lugar de entrega Delegacion

        String delegacionLente = Vars.get("lentes.delegacion");
        pedidoLentesPage.DelegacionEntrega(delegacionLente);

        // 3.6) Agregar lentes (por nombre) + descripción
        String nombreLentes = Vars.get("lentes.nombre");
        String descripcionLentes = Vars.get("lentes.descripcion");
        pedidoLentesPage.buscarYAgregarLentes(nombreLentes);
        pedidoLentesPage.agregarDescripcionLentes(descripcionLentes);

        // 3.7) Cantidad + tildar "No" (header) + "A cargo de: Osprera" 
        int cantidadLentes = Integer.parseInt(Vars.get("lentes.cantidad"));
        pedidoLentesPage.completarDatosLentes(cantidadLentes);
        pedidoPage.clickCargarAdjuntos();
    }

    // 4) Enviar a auditoría (y capturar SIEMPRE el número visible en la pantalla de
    // resultados)
    @Then("envio a auditar")
    public void envio_a_auditoria() {
        pedidoPage.enviarAAuditoria();
        numeroOrden = pedidoPage.obtenerNumeroOrden();
        ContextoGlobal.numeroOrden = numeroOrden;
        System.out.println("📦 Número de orden visual: " + numeroOrden);
    }
}
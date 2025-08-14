package com.osprera.test.stepdefinitions;

import com.osprera.test.hooks.Hooks;
import com.osprera.test.pages.PedidoPage;
import com.osprera.test.pages.PedidoProtesisPage;
import com.osprera.test.utils.ContextoGlobal;
import com.osprera.test.utils.Vars;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;

public class PedidoProtesisSteps {

    private final WebDriver driver = Hooks.driver;
    private final PedidoPage pedidoPage = new PedidoPage(driver);
    private final PedidoProtesisPage pedidoProtesisPage = new PedidoProtesisPage(driver);
    private String numeroOrden = "";

    // 0) Login ya realizado por Hooks → solo esperamos que SGP aparezca
    @Given("que ya inicié sesión")
    public void que_ya_inicié_sesion_correctamente() {
        pedidoPage.esperarSGPVisible();
    }

    // 1) Entrar al módulo SGP
    @And("entro a SGP")
    public void entro_al_modulo_SGP() {
        pedidoPage.clickSGP();
    }

    // 2) Cargar beneficiario (DNI)
    @And("ingreso el DNI del beneficiario")
    public void selecciono_al_beneficiario() {
        String dni = Vars.get("beneficiario.dni");
        pedidoPage.completarDatosGenerales(dni);
    }

    // 3) Completar todo el pedido de PRÓTESIS (pantalla principal)
    @When("completo el pedido de protesis")
    public void completo_el_pedido_de_protesis() {
        // 3.1) Tipo de pedido = Prótesis + fecha de solicitud (hoy)
        pedidoProtesisPage.seleccionarTipoPedidoYFecha();

        // 3.2) Diagnóstico (CIE + texto)
        String cieCodigo = Vars.get("cie.codigo");
        String diagnosticoTexto = Vars.get("diagnostico.texto");
        pedidoPage.cargarDiagnosticoYCIE(cieCodigo, diagnosticoTexto);

        // 3.3) Médico solicitante
        String medicoBusqueda = Vars.get("medico.busqueda");
        pedidoPage.seleccionarMedico(medicoBusqueda);

        // 3.4) Urgente + Fecha de intervención
        pedidoPage.seleccionarUrgente();
        String fechaIntervencion = Vars.get("protesis.fechaIntervencion");
        pedidoProtesisPage.fechaIntervencion(fechaIntervencion);

        // 3.5) Lugar de entrega (Entidad Efectora)
        String lugarEntrega = Vars.get("protesis.lugarEntrega");
        pedidoProtesisPage.lugarEntrega(lugarEntrega);

        // 3.6) Agregar prótesis (por nombre) + descripción
        String nombreProtesis = Vars.get("protesis.nombre");
        String descripcionProtesis = Vars.get("protesis.descripcion");
        pedidoProtesisPage.buscarYAgregarProtesis(nombreProtesis);
        pedidoProtesisPage.agregarDescripcionProtesis(descripcionProtesis);

        // 3.7) Cantidad + tildar "No" (header) + "A cargo de: Osprera"
        int cantidad = Integer.parseInt(Vars.get("protesis.cantidad"));
        pedidoProtesisPage.completarDatosProtesis(cantidad);

        // 3.8) Adjuntos específicos de Prótesis (Historia Clínica + Exámenes Compl. + Pedido médico + Tipo Estudio)
        String fechaAdjunto = Vars.get("protesis.fechaAdjunto");
        String comentarioAdj = Vars.get("protesis.comentario") != null
                ? Vars.get("protesis.comentario")
                : "Pedido realizado con automatización (Selenium)";
        String rutaArchivo = "C:\\Users\\BeeUser\\Documents\\pedido_medico.txt";
        String tipoEstudio = Vars.get("protesis.tipoEstudio"); // Ej.: "Radiología"
        pedidoProtesisPage.clickCargarAdjuntosProtesis(fechaAdjunto, comentarioAdj, rutaArchivo, tipoEstudio);

       
    }

    // 4) Enviar a auditoría (y capturar SIEMPRE el número visible en la pantalla de resultados)
  @Then("envio a auditoria provincial")
public void envio_a_auditoria() {
    pedidoProtesisPage.enviarAAuditoria();     // ✅ directo a Enviar a Auditoría
    String numeroOrden = pedidoPage.obtenerNumeroOrden();  // ahora sí hay N°
    ContextoGlobal.numeroOrden = numeroOrden;
    System.out.println("📦 Número de orden visual: " + numeroOrden);
}}

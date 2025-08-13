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
    private final PedidoProtesisPage pedidoProtesisPage = new PedidoProtesisPage(driver);
     private PedidoPage pedidoPage = new PedidoPage(driver);
    private String numeroOrden;

    @Given("que ya inicié sesión")
    public void que_ya_inicié_sesion_correctamente() {
        pedidoPage.esperarSGPVisible();
    }

      @And("entro a SGP")
    public void entro_al_modulo_SGP() {
        pedidoPage.clickSGP();
    }
     @And("ingreso el DNI del beneficiario")
    public void selecciono_al_beneficiario() {
        String dni = Vars.get("beneficiario.dni"); 
        pedidoPage.completarDatosGenerales(dni);
    }

 @When("completo el pedido de protesis")
    public void completo_el_pedido_de_protesis() {
        // Navegar a la sección de Prótesis
        pedidoProtesisPage.seleccionarTipoPedidoYFecha();

        // Pasar los valores como parámetros
        String cieCodigo = Vars.get("cie.codigo");
        String diagnosticoTexto = Vars.get("diagnostico.texto");
        pedidoPage.cargarDiagnosticoYCIE(cieCodigo, diagnosticoTexto);

        String medicoBusqueda = Vars.get("medico.busqueda");
        pedidoPage.seleccionarMedico(medicoBusqueda);

        pedidoPage.seleccionarUrgente();

        String fecha = Vars.get("protesis.fechaIntervencion");
        String lugar = Vars.get("protesis.lugarEntrega");
        String nombre = Vars.get("protesis.nombre");
        String descripcion = Vars.get("protesis.descripcion");
        int cantidad = Integer.parseInt(Vars.get("protesis.cantidad"));

        pedidoProtesisPage.fechaIntervencion(fecha);
        pedidoProtesisPage.lugarEntrega(lugar);
        pedidoProtesisPage.buscarYAgregarProtesis(nombre);
        pedidoProtesisPage.agregarDescripcionProtesis(descripcion);
        pedidoProtesisPage.completarDatosProtesis(cantidad);

        pedidoPage.clickCargarAdjuntos();

        // Confirmar y guardar el número
        numeroOrden = pedidoProtesisPage.confirmarYObtenerNumero();
        ContextoGlobal.numeroOrden = numeroOrden;
        System.out.println("🧾 Pedido de prótesis generado: " + numeroOrden);
    }

    @And("confirmo el pedido")
    public void envio_a_auditoria() {
        pedidoProtesisPage.enviarAAuditoria();
    }
}

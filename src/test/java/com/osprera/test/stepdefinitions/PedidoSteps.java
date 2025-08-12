package com.osprera.test.stepdefinitions;

import com.osprera.test.hooks.Hooks;
import com.osprera.test.pages.PedidoPage;
import com.osprera.test.utils.ContextoGlobal;
import com.osprera.test.utils.Vars;

import io.cucumber.java.en.*;


import org.openqa.selenium.WebDriver;

public class PedidoSteps {
    private WebDriver driver = Hooks.driver;
    private PedidoPage pedidoPage = new PedidoPage(driver);
    private String numeroOrden;  
    @Given("que ya inicié sesión correctamente")
    public void que_ya_inicié_sesion_correctamente() {
        pedidoPage.esperarSGPVisible();
    }

    @And("entro al modulo SGP")
    public void entro_al_modulo_SGP() {
        pedidoPage.clickSGP();
    }

   @And("selecciono al beneficiario")
    public void selecciono_al_beneficiario() {
        String dni = Vars.get("beneficiario.dni"); 
        pedidoPage.completarDatosGenerales(dni);
    }

   @When("completo el pedido de medicamentos")
public void completo_el_pedido_de_medicamentos() {
    pedidoPage.seleccionarTipoPedidoYFecha();
    pedidoPage.cargarDiagnosticoYCIE();
    pedidoPage.seleccionarMedico();
    pedidoPage.seleccionarUrgente(); 
    pedidoPage.seleccionarFarmacia();
    pedidoPage.buscarYAgregarMedicamento();
    pedidoPage.ingresarCantidadYDosis();
    pedidoPage.clickCargarAdjuntos();
}


@And("envio a auditoria")
public void envio_a_auditoria() {
    pedidoPage.enviarAAuditoria();  
    numeroOrden = pedidoPage.obtenerNumeroOrden(); 
    ContextoGlobal.numeroOrden = numeroOrden;
    System.out.println("📦 Número de orden visual: " + numeroOrden);
}

@Then("cierro el navegador")
public void cierro_el_navegador() {
    pedidoPage.cerrarNavegador();
}


}

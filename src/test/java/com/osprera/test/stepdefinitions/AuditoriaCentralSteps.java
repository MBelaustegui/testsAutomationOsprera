package com.osprera.test.stepdefinitions;

import com.osprera.test.pages.AuditoriaCentralPage;
import com.osprera.test.pages.PedidoPage;
import com.osprera.test.utils.ContextoGlobal;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import com.osprera.test.hooks.Hooks;

public class AuditoriaCentralSteps {
    private WebDriver driver;
    private AuditoriaCentralPage auditoriaCentralPage;
    private PedidoPage pedidoPage;

    @Given("ingreso al sistema como auditor central")
    public void ingreso_como_auditor_central() {
        driver = Hooks.driver;
        auditoriaCentralPage = new AuditoriaCentralPage(driver);
        pedidoPage = new PedidoPage(driver);
     
    }



    @And("entro al modulo de SGP para auditar")
    public void entro_al_modulo_SGP() {
        pedidoPage.clickSGP();
    }

    @When("busco el pedido")
    public void busco_el_pedido() {
       // var numero = "511077";
        String numero = ContextoGlobal.numeroOrden;
        System.out.println("🔎 Buscando pedido nro: " + numero);
        auditoriaCentralPage.buscarPedidoPorNumero(numero);
    }

    @And("autorizo el pedido en sede central")
    public void autorizo_el_pedido_en_sede_central() {
        auditoriaCentralPage.autorizarPedido();
    }
}

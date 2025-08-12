package com.osprera.test.stepdefinitions;

import com.osprera.test.hooks.Hooks;
import com.osprera.test.pages.AuditoriaProvincialPage;
import com.osprera.test.pages.PedidoPage;
import com.osprera.test.utils.ContextoGlobal;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;


public class AuditoriaProvincialSteps {

    private WebDriver driver;
    private AuditoriaProvincialPage auditoriaPage;
     private PedidoPage pedidoPage = new PedidoPage(driver);

    @Given("ingreso al sistema como auditor")
public void ingreso_como_auditor() {
    driver = Hooks.driver;
    auditoriaPage = new AuditoriaProvincialPage(driver);
     pedidoPage = new PedidoPage(driver); 
}
 @And("entro al modulo de SGP")
    public void entro_al_modulo_SGP() {
        pedidoPage.clickSGP();
    }

    @When("busco el pedido generado")
    public void busco_el_pedido_generado() {
       
       String numero = ContextoGlobal.numeroOrden;
      // var numero = "511042";
        System.out.println("🔎 Buscando pedido nro: " + numero);
        auditoriaPage.buscarPedidoPorNumero(numero);
    }

    @And("autorizo el pedido")
    public void autorizo_pedido() {
        auditoriaPage.hacerClickEnAutorizar();
          System.out.println("✅ Auditoría finalizada para pedido: " + ContextoGlobal.numeroOrden);
        auditoriaPage.cerrarNavegador();
    }

   
}

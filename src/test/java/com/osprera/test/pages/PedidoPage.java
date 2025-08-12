package com.osprera.test.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.osprera.test.utils.RobotUtils;

import java.time.Duration;

public class PedidoPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public PedidoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void esperarSGPVisible() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h4[contains(text(),'SGP')]")));
    }

    public void clickSGP() {
        WebElement botonSGP = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='SGP (Sistema de Gestión Prestacional)'])[1]/following::button[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botonSGP);
    }

    public void completarDatosGenerales(String documento) {
        WebElement inputDoc = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BeneficiarioDocumento")));
        inputDoc.clear();
        inputDoc.sendKeys(documento);
        WebElement botonBuscar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//buscador-beneficiarios/div/img")));
        botonBuscar.click();
    }

    public void seleccionarTipoPedidoYFecha() {
        WebElement tipoPedido = wait.until(ExpectedConditions.elementToBeClickable(By.id("tipoPedido")));
        tipoPedido.click();
        tipoPedido.findElement(By.xpath("//option[contains(text(),'Medicamentos')]"))
                  .click();

        WebElement fechaInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("FechaDesde2")));
        fechaInput.clear();
        // Fecha de hoy en formato dd/MM/yyyy
        String hoy = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        fechaInput.sendKeys(hoy);
        fechaInput.sendKeys(Keys.TAB);
    }

    public void cargarDiagnosticoYCIE() {
        WebElement inputCie = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//buscador-cie/div/input")));
        inputCie.sendKeys("G43");
        driver.findElement(By.xpath("//buscador-cie/div/img")).click();

        WebElement agregarBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//buscador-cie/div/input[4]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", agregarBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", agregarBtn);

        WebElement diagnosticoTextarea = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//textarea")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", diagnosticoTextarea);
        diagnosticoTextarea.clear();
        diagnosticoTextarea.sendKeys("Dolor de cabeza");
    }

    public void seleccionarMedico() {
        WebElement medicoInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//buscador-medico-solicitante/div/input[2]")));
        medicoInput.click();
        medicoInput.sendKeys("Garriga juan jose");
        medicoInput.sendKeys(Keys.ENTER);
    }
   public void seleccionarUrgente() {
    WebElement urgenteCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//input[@type='checkbox' and @ng-model='ngModel.Urgente.Valor']")));
    if (!urgenteCheckbox.isSelected()) {
        urgenteCheckbox.click();
    }
}
    public void seleccionarFarmacia() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[6]/input"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//buscador-farmacias/div/input[3]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//bloque-provincia-localidad[@id='Buscador-bocasProv']/div/table/tbody/tr/td/simpleselect/div/button/div/span"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//bloque-provincia-localidad[@id='Buscador-bocasProv']/div/table/tbody/tr/td/simpleselect/div/ul/li[2]/label"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Filtrar Resultados']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[@id='tableBodyExamen']/td/span"))).click();
    }

    public void buscarYAgregarMedicamento() {
        WebElement monoInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//buscador-medicamentos-x-monodroga[@id='buscadorMonodrogas']/div/input[3]")));
      //  monoInput.sendKeys("tolfen");
        monoInput.sendKeys("ibup");
        monoInput.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[@id='tableBodyExamen']/td/span"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//buscador-medicamentos-x-monodroga[@id='buscadorMonodrogas']/div/input[4]"))).click();

       // wait.until(ExpectedConditions.elementToBeClickable(
         //       By.xpath("(//*[normalize-space(text()) and normalize-space(.)='tolfenámico,ác.'])[1]/following::td[2]"))).click();
    
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//*[normalize-space(text()) and normalize-space(.)='ibuprofeno'])[1]/following::td[2]"))).click();

        }

    public void ingresarCantidadYDosis() {
        WebElement cantidadInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[2]/table/tbody/tr/td[6]/input")));
        cantidadInput.clear();
        cantidadInput.sendKeys("5");

        WebElement dosisInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[7]/input")));
        dosisInput.clear();
        dosisInput.sendKeys("1");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//simpleselect[@id='idComboDosisPeriodo']/div/button/div/span"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//simpleselect[@id='idComboDosisPeriodo']/div/ul/li[4]/label/span"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='SurNo']"))).click();
     //   wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//simpleselect[@id='idComboACargoDe']/div/button/div/span"))).click();
       // wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//simpleselect[@id='idComboACargoDe']/div/ul/li[3]/label/span"))).click();
    }

    public void clickCargarAdjuntos() {
        WebElement cargarAdjuntoBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Cargar Adjuntos')]")));
        cargarAdjuntoBtn.click();

        WebElement dropdownTipoDoc = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='modaladjuntos']//multiselect//button/span")));
        dropdownTipoDoc.click();

        WebElement opcionPedidoMedico = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='modaladjuntos']//multiselect//ul/li[4]/label")));
        opcionPedidoMedico.click();

        WebElement fechaAdjunto = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("FechaAdjunto")));
        fechaAdjunto.clear();
        fechaAdjunto.sendKeys("05/06/2025");

        WebElement comentario = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='modaladjuntos']//textarea")));
        comentario.clear();
        comentario.sendKeys("Pedido realizado con automatización (Selenium)");

        WebElement botonAgregarArchivo = wait.until(ExpectedConditions.elementToBeClickable(By.id("dale")));
        botonAgregarArchivo.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        RobotUtils.subirArchivo("C:\\Users\\BeeUser\\Documents\\pedido_medico.txt");

        WebElement aceptarBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='modaladjuntos']//button[2]")));
        aceptarBtn.click();
    }

    public void enviarAAuditoria() {
        WebElement botonEnviar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Enviar a Auditoría')]")));
        botonEnviar.click();
    }

     public boolean apareceConfirmacion() {
        return driver.getPageSource().contains("Resultados");
    }

    public String obtenerNumeroOrden() {
    WebElement numero = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//th[contains(text(), 'Estado del Pedido Nro')]")
    ));
    return numero.getText().replaceAll("\\D+", "");
}


    public void cerrarModalResultados() {
        WebElement cerrar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(@class,'ui-icon-close') and @ng-click='cancel()']")));
        cerrar.click();
    }

    public void cerrarNavegador() {
        driver.quit();
    }

}

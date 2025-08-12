package com.osprera.test.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.nio.file.Files;


public class AuditoriaProvincialPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public AuditoriaProvincialPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void loginComoAuditor(String usuario, String clave) {
        driver.get("https://sioqa.osprera.org.ar/#/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Usuario']")))
                .sendKeys(usuario);

        driver.findElement(By.xpath("//input[@type='password']")).sendKeys(clave);

        driver.findElement(By.cssSelector("button.dropdown-toggle")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Externo')]"))).click();

        driver.findElement(By.xpath("//button[contains(text(),'Iniciar Sesión')]")).click();
    }

 public void buscarPedidoPorNumero(String numero) {
    wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[ng-model='consulta.Id']")))
            .sendKeys(numero);
    driver.findElement(By.cssSelector("input[ng-click='filtrarResultados()']")).click();

    String xpathAuditar = "//tr[td[contains(normalize-space(.), '" + numero + "')]]" +
            "//img[@title='Auditar' and @ng-click='doAuditar(solicitud)' and not(contains(@class,'ng-hide'))]";

    WebElement botonAuditar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathAuditar)));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", botonAuditar);

    String ventanaOriginal = driver.getWindowHandle();

    try {
        botonAuditar.click();
    } catch (ElementClickInterceptedException e) {
        new Actions(driver).moveToElement(botonAuditar).click().perform();
    }

    System.out.println("⏳ Esperando que se abra una nueva pestaña...");
    wait.until(driver -> driver.getWindowHandles().size() > 1);

    for (String ventana : driver.getWindowHandles()) {
        if (!ventana.equals(ventanaOriginal)) {
            driver.switchTo().window(ventana);
            break;
        }
    }

    System.out.println("✅ Nueva pestaña activa.");

    System.out.println("⏳ Esperando a que se cargue la pantalla de auditoría...");
    wait.until(ExpectedConditions.textToBePresentInElementLocated(
            By.cssSelector("h3.ng-binding"),
            "Auditando Solicitud de Pedido de Medicamentos"
    ));
    System.out.println("✅ Pantalla de auditoría cargada.");
}


public void hacerClickEnAutorizar() {
    System.out.println("🔍 Buscando input vacío para ingresar código CIE10...");
    WebElement inputVacio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='']")));
    inputVacio.click();
    inputVacio.sendKeys("e10");
    System.out.println("✅ Código 'e10' ingresado.");

    esperar(500);

    System.out.println("🔍 Buscando ícono de búsqueda...");
    WebElement botonBuscar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//buscador-cie/div/img")));
    botonBuscar.click();
    System.out.println("✅ Se hizo clic en el botón buscar.");

    esperar(1000);

    System.out.println("🔍 Esperando botón para agregar diagnóstico...");
    WebElement botonAgregar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//buscador-cie/div/input[4]")));
    botonAgregar.click();
    System.out.println("✅ Diagnóstico agregado.");

    esperar(500);

    System.out.println("🔍 Haciendo clic en checkbox 'Autorizar'...");
    WebElement checkAutorizar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Autorizar']")));
    checkAutorizar.click();
    System.out.println("✅ Checkbox marcado.");

    esperar(500);

    System.out.println("🔍 Haciendo clic en botón 'Confirmar'...");
    WebElement botonConfirmar = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("(//*[normalize-space(text()) and normalize-space(.)='Cancelar'])[1]/following::button[1]")));
    botonConfirmar.click();
    System.out.println("✅ Pedido autorizado y confirmado.");
    System.out.println("🔍 Verificando mensaje de autorización...");
WebElement mensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(
    By.xpath("//*[contains(text(), 'El pedido se ha autorizado en forma exitosa')]")
));
System.out.println("✅ Confirmación recibida: " + mensaje.getText());

esperar(500);

System.out.println("📸 Tomando captura de pantalla...");
TakesScreenshot ts = (TakesScreenshot) driver;
File screenshot = ts.getScreenshotAs(OutputType.FILE);
try {
    File destino = new File("evidencia_autorizacion.png");
    Files.copy(screenshot.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
    System.out.println("✅ Captura guardada en: " + destino.getAbsolutePath());
} catch (IOException e) {
    System.err.println("⚠️ Error al guardar la captura: " + e.getMessage());
}

esperar(500);

System.out.println("🧹 Cerrando el popup de resultado...");
WebElement cerrarPopup = wait.until(ExpectedConditions.elementToBeClickable(
    By.xpath("//div[contains(@class,'ui-dialog-titlebar')]//a[contains(@class,'ui-dialog-titlebar-icon')]")
));
cerrarPopup.click();
System.out.println("✅ Popup cerrado.");

}

private void esperar(long milisegundos) {
    try {
        Thread.sleep(milisegundos);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}


    public void cerrarNavegador() {
        driver.quit();
    }
}

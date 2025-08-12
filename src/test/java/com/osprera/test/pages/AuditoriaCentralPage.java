package com.osprera.test.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class AuditoriaCentralPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public AuditoriaCentralPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    

    public void buscarPedidoPorNumero(String numero) {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[ng-model='consulta.Id']")))
            .sendKeys(numero);
        driver.findElement(By.cssSelector("input[ng-click='filtrarResultados()']")).click();
        String xpathAuditar = "//tr[td[contains(normalize-space(.), '" + numero + "')]]//img[@title='Auditar' and not(contains(@class,'ng-hide'))]";
        WebElement botonAuditar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathAuditar)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", botonAuditar);
        botonAuditar.click();
        // Cambiar a la nueva pestaña
        String ventanaOriginal = driver.getWindowHandle();
        wait.until(d -> d.getWindowHandles().size() > 1);
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(ventanaOriginal)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("h3.ng-binding"),
                "Auditando Solicitud de Pedido de Medicamentos"
        ));

        // Tildar el checkbox Confirmado si está presente
        try {
            WebElement chkConfirmado = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@type='checkbox' and @ng-model='ngModel.Confirmado']")
            ));
            if (!chkConfirmado.isSelected()) {
                chkConfirmado.click();
            }
        } catch (TimeoutException e) {
            System.out.println("⚠️ Checkbox Confirmado no encontrado o no clickable");
        }
    }

    public void autorizarPedido() {
        WebElement checkAutorizar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Autorizar']")));
        checkAutorizar.click();
        WebElement botonConfirmar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//*[normalize-space(text())='Cancelar']/following::button[1])[1]")));
        botonConfirmar.click();
        // Esperar popup de resultado
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'ui-dialog') and contains(.,'AUTORIZADO')]")));
    }
}

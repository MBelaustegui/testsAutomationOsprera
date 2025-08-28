package com.osprera.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class AuditoriaProvincialPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public AuditoriaProvincialPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ====== Login (si lo usás desde acá) ======
    public void loginComoAuditor(String usuario, String clave) {
        driver.get("https://sio.osprera.org.ar/#/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Usuario']")))
            .sendKeys(usuario);

        driver.findElement(By.xpath("//input[@type='password']")).sendKeys(clave);

        driver.findElement(By.cssSelector("button.dropdown-toggle")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Externo')]"))).click();

        driver.findElement(By.xpath("//button[contains(text(),'Iniciar Sesión')]")).click();
    }

    // ====== Buscar pedido por N° y abrir pestaña de Auditoría ======
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

        // Esperar nueva pestaña y cambiar el foco
        wait.until(d -> d.getWindowHandles().size() > 1);
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(ventanaOriginal)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        // Asegurar pantalla cargada
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("h3.ng-binding"),
                "Auditando Solicitud de Pedido de Medicamentos"
        ));
    }

    // ====== Flujo de autorización (CIE, autorizar, confirmar, verificar y cerrar popup) ======
    public void hacerClickEnAutorizar() {
        // Cargar CIE10 (ejemplo: e10) y agregar
        WebElement inputVacio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='']")));
        inputVacio.click();
        inputVacio.sendKeys("e10");

        esperar(500);

        WebElement botonBuscar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//buscador-cie/div/img")));
        botonBuscar.click();

        esperar(800);

        WebElement botonAgregar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//buscador-cie/div/input[4]")));
        botonAgregar.click();

        esperar(300);

        WebElement checkAutorizar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Autorizar']")));
        checkAutorizar.click();

        esperar(300);

        // Confirmar
        WebElement botonConfirmar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//*[normalize-space(text())='Cancelar']/following::button[1])[1]")));
        botonConfirmar.click();

        // Esperar popup “Resultados” y loguear estado
        String estado = leerEstadoResultados();
        System.out.println("📘 Estado leído en popup: " + estado);

        // (Opcional) Validar texto de éxito
        try {
            WebElement mensaje = new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(normalize-space(),'autorizado en forma exitosa')]")
                    ));
            System.out.println("✅ Mensaje: " + mensaje.getText());
        } catch (TimeoutException ignored) { }

        // Screenshot a disco (evidencia)
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dst = new File("evidencia_autorizacion.png");
            Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("📸 Captura guardada en: " + dst.getAbsolutePath());
        } catch (IOException ex) {
            System.err.println("⚠️ Error al guardar la captura: " + ex.getMessage());
        }

        // Verificar si el pedido fue autorizado ANTES de cerrar el popup
        boolean autorizado = resultadoEsAutorizado();
        System.out.println("¿Autorizado?: " + autorizado);
        if (!autorizado) {
            throw new AssertionError("❌ El pedido no fue autorizado (no se encontró la palabra AUTORIZADO en el popup).");
        }
        // Captura adicional antes de cerrar popup/navegador y adjuntar al reporte si Scenario está disponible
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dst = new File("evidencia_autorizacion_final.png");
            Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("📸 Captura FINAL guardada en: " + dst.getAbsolutePath());
            // Adjuntar al reporte si Scenario está disponible
            Object maybeScenario = driver.manage().getCookieNamed("scenarioRef"); // hack: no hay acceso directo
            if (maybeScenario instanceof io.cucumber.java.Scenario) {
                io.cucumber.java.Scenario scenario = (io.cucumber.java.Scenario) maybeScenario;
                scenario.attach(Files.readAllBytes(dst.toPath()), "image/png", "📸 Captura final validación");
            }
        } catch (IOException ex) {
            System.err.println("⚠️ Error al guardar la captura final: " + ex.getMessage());
        } catch (Exception ignore) { }
        // Cerrar popup
        cerrarPopupResultado();
    }

    // ====== Popup “Resultados”: lectura de estado y cierre robusto ======
    private final By dlgResultadosBy = By.xpath(
            "//div[contains(@class,'ui-dialog')]" +
            "[.//div[contains(@class,'ui-dialog-title') and contains(normalize-space(),'Resultados')]]"
    );

    private WebElement waitDialogResultados() {
        return new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(dlgResultadosBy));
    }

    public String leerEstadoResultados() {
        WebElement dlg = waitDialogResultados();
        By estadoBy = By.xpath(".//span[contains(@class,'ResultadoSolicitud_Color')]");
        WebElement estado = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(dlg, estadoBy))
                .get(0);
        return estado.getText().trim(); // p.ej. "AUTORIZADO"
    }

    public boolean resultadoEsAutorizado() {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement popup = customWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'ui-dialog') and not(contains(@style,'display: none'))]")
            ));
            // Buscar 'AUTORIZADO' en todo el texto visible del popup
            String popupText = popup.getText();
            if (popupText != null && popupText.toUpperCase().contains("AUTORIZADO")) {
                return true;
            }
            // Retry rápido (1s) por si el texto aparece con delay
            esperar(1000);
            popupText = popup.getText();
            if (popupText != null && popupText.toUpperCase().contains("AUTORIZADO")) {
                return true;
            }
            // Si no encontró, imprimir HTML y texto plano del popup para depuración
            System.err.println("[DEBUG] Texto plano del popup de resultados:");
            System.err.println(popupText);
            System.err.println("[DEBUG] HTML del popup de resultados:");
            System.err.println(popup.getAttribute("outerHTML"));
            return false;
        } catch (TimeoutException e) {
            System.err.println("[DEBUG] Timeout esperando el popup de resultados");
            return false;
        }
    }

    public void cerrarPopupResultado() {
        try {
            WebElement dlg = waitDialogResultados();

            By closeBy = By.xpath(".//a[contains(@class,'ui-dialog-titlebar-close')]"
                    + " | .//span[contains(@class,'ui-icon-close') or contains(@class,'ui-icon-closethick')]");
            WebElement close = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfNestedElementLocatedBy(dlg, closeBy));

            // Usar JS para evitar intercepts del overlay
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", close);

            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOf(dlg));
            System.out.println("✅ Popup 'Resultados' cerrado.");
        } catch (Exception e) {
            // Fallback: ESC
            try {
                driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.invisibilityOfElementLocated(dlgResultadosBy));
            } catch (Exception ignored) { }
        }
    }

    // ====== Utils ======
    private void esperar(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public void cerrarNavegador() {
        driver.quit();
    }
}

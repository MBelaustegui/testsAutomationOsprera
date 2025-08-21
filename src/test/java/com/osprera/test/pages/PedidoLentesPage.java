package com.osprera.test.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class PedidoLentesPage {

    // ===== Driver & Wait =====
    private final WebDriver driver;
    private final WebDriverWait wait;

    public PedidoLentesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /*
     * ===============
     * 
     * /* ========== (1) Tipo de pedido = LENTES ==========
     */
    public void seleccionarTipoPedidoYFecha() {
        WebElement tipoPedido = wait.until(ExpectedConditions.elementToBeClickable(By.id("tipoPedido")));
        tipoPedido.click();
        tipoPedido.findElement(By.xpath("//option[contains(text(),'Lentes')]")).click();

        WebElement fechaInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("FechaDesde2")));
        fechaInput.clear();
        String hoy = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        fechaInput.sendKeys(hoy);
        fechaInput.sendKeys(Keys.TAB);
    }


/* ========== (3) Lugar de Entrega (Delegacion) ========== */
public void DelegacionEntrega(String delegacionLente) {
    // Click al desplegable
    WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("idComboLugarEntrega")));
    dropdown.click();

    // Buscar la opción por texto (sin restringir al div.dropdown)
    WebElement opcion = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//span[contains(text(),'" + delegacionLente + "')]")
    ));
    opcion.click();
}


    /* ========== (4) Buscar material lentes y agregar [+] ========== */
    public void buscarYAgregarLentes(String nombre) {
        By inputNombre = By.xpath(
                "/html/body/div[2]/div/div/div[2]/div/div/div[2]/fieldset[2]/div[1]/table/tbody/tr/td[2]/buscador-material-x-tipo/div/input[3]");
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputNombre));
        input.clear();
        input.sendKeys(nombre);
        input.sendKeys(Keys.ENTER);

        By botonMas = By.xpath(
                "/html/body/div[2]/div/div/div[2]/div/div/div[2]/fieldset[2]/div[1]/table/tbody/tr/td[2]/buscador-material-x-tipo/div/input[4]");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(botonMas));
        btn.click();
    }

    /* ========== (5) Descripción (modal) ========== */
    public void agregarDescripcionLentes(String descripcion) {
        // Abrir modal
        By iconoDescripcion = By.xpath("//img[contains(@src,'sin-desc')]");
        wait.until(ExpectedConditions.elementToBeClickable(iconoDescripcion)).click();

        // Esperar modal “Descripción del Material”
        By titulo = By.xpath(
                "//span[contains(@class,'t-window-title')][contains(normalize-space(.),'Descripción del Material')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(titulo));

        // Textarea del modal
        By campoDescripcion = By.xpath("//textarea[@ng-model='objeto.Valor']");
        WebElement textarea = wait.until(ExpectedConditions.elementToBeClickable(campoDescripcion));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", textarea);

        try {
            textarea.click();
            textarea.clear();
            textarea.sendKeys(descripcion);
        } catch (Exception ignored) {
        }
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].focus(); arguments[0].value = arguments[1];" +
                        "arguments[0].dispatchEvent(new Event('input',{bubbles:true}));" +
                        "arguments[0].dispatchEvent(new Event('change',{bubbles:true}));",
                textarea, descripcion);

        // Aceptar
        By btnAceptar = By
                .xpath("//div[contains(@class,'modal-footer')]//button[.='Aceptar' or @ng-click='agregar()']");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                wait.until(ExpectedConditions.elementToBeClickable(btnAceptar)));

        // Esperar cierre
        wait.until(ExpectedConditions.invisibilityOfElementLocated(campoDescripcion));
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal-backdrop")));
        } catch (Exception ignored) {
        }
    }

    /* ========== (6) Cantidad + “No” + “A cargo de: Osprera” ========== */
    public void completarDatosLentes(int cantidad) {
        // Cantidad
        By inputCantidad = By.xpath("//input[@ng-model='item.Cantidad']");
        WebElement cantidadInput = wait.until(ExpectedConditions.elementToBeClickable(inputCantidad));
        cantidadInput.clear();
        cantidadInput.sendKeys(String.valueOf(cantidad));

        // “No” (header)
        By checkNo = By.xpath(
                "/html/body/div[2]/div/div/div[2]/div/div/div[2]/fieldset[2]/div[2]/table/thead/tr/th[6]/div/span[2]/input");
        WebElement noCheckbox = wait.until(ExpectedConditions.elementToBeClickable(checkNo));
        if (!noCheckbox.isSelected())
            noCheckbox.click();

        // “A cargo de: Osprera”
        seleccionarACargoDe("Osprera");
    }

    private void seleccionarACargoDe(String opcionTexto) {
        By btnDrop = By.xpath(
                "(//fieldset[.//legend[contains(normalize-space(),'Lentes')]]//button[@ng-click='toggleSelect()'])[last()]");
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(btnDrop));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

        By menuUl = By.xpath(
                "(//fieldset[.//legend[contains(normalize-space(),'Lentes')]]//button[@ng-click='toggleSelect()']/following-sibling::ul[contains(@class,'dropdown-menu')])[last()]");
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(menuUl));

        WebElement opt = menu.findElement(By.xpath(".//li/label/span[normalize-space()='" + opcionTexto + "']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opt);

        WebElement labelSpan = button.findElement(By.xpath(".//span[contains(@class,'pull-left')]"));
        wait.until(ExpectedConditions.textToBePresentInElement(labelSpan, opcionTexto));
    }

    /* ========== (8) Confirmar (tolerante) ========== */
    public String confirmarYObtenerNumero() {
        By btnConfirmar = By.xpath("//button[.='Confirmar' or .='Guardar' or contains(.,'Generar Pedido')]");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnConfirmar));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        String n = tryObtenerNumeroDesdeModalToast(7);
        return (n == null) ? "" : n;
    }

    private String tryObtenerNumeroDesdeModalToast(int seconds) {
        long end = System.currentTimeMillis() + seconds * 1000L;
        By anyModal = By.xpath("//div[contains(@class,'modal') and not(contains(@style,'display: none'))]");
        By anyToast = By
                .xpath("//*[contains(@class,'toast') or contains(@class,'alert') or contains(@class,'ng-toast')]");
        while (System.currentTimeMillis() < end) {
            try {
                for (WebElement m : driver.findElements(anyModal)) {
                    String n = extraerNumero(m.getText());
                    if (n != null)
                        return n;
                }
                for (WebElement t : driver.findElements(anyToast)) {
                    String n = extraerNumero(t.getText());
                    if (n != null)
                        return n;
                }
            } catch (Exception ignored) {
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }

    private String extraerNumero(String texto) {
        if (texto == null)
            return null;
        String n = texto.replaceAll("(?s).*?(\\d{5,}).*", "$1");
        return (n.matches("\\d{5,}")) ? n : null;
    }

    /* ========== (9) Enviar a Auditoría (robusto) ========== */
    public void enviarAAuditoria() {
        // Botón correcto (NO "Guardar")
        By btnEnviar = By
                .xpath("(//button[@ng-click='enviarAuditoria()' and not(contains(@class,'ng-hide'))])[last()]");

        WebElement boton = wait.until(ExpectedConditions.presenceOfElementLocated(btnEnviar));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", boton);
        wait.until(ExpectedConditions.elementToBeClickable(boton));

        try {
            boton.click();
        } catch (ElementClickInterceptedException e) {
            // Si algún backdrop/overlay intercepta, esperar y reintentar con JS
            try {
                new WebDriverWait(driver, Duration.ofSeconds(8))
                        .until(ExpectedConditions.invisibilityOfElementLocated(
                                By.cssSelector(".modal-backdrop, .modal.show, .modal.fade.in")));
            } catch (Exception ignored) {
            }
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", boton);
        }

        // Confirmación (si aparece)
        By ok = By.xpath(
                "//button[normalize-space(.)='Aceptar' or normalize-space(.)='OK' or contains(normalize-space(.),'Confirmar')]");
        try {
            WebElement btnOk = new WebDriverWait(driver, Duration.ofSeconds(6))
                    .until(ExpectedConditions.elementToBeClickable(ok));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnOk);
        } catch (TimeoutException ignored) {
        }

        // Limpieza de overlays
        try {
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.invisibilityOfElementLocated(
                            By.cssSelector(".modal-backdrop, .modal.show, .modal.fade.in")));
        } catch (Exception ignored) {
        }
    }

    /*
     * ======= Extras opcionales (si te sirven en pruebas unitarias de la página)
     * =======
     */
    public void cargarDiagnosticoYCIE() {
        /* … como ya lo tenías … */ }

    public void seleccionarMedico() {
        /* … como ya lo tenías … */ }

    public void ingresarDiagnostico(String texto) {
        /* … */ }

    public void agregarItemLentes(String codigo, int cantidad) {
        /* … */ }
}

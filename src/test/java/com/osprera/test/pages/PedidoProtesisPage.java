package com.osprera.test.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class PedidoProtesisPage {

    // ===== Driver & Wait =====
    private final WebDriver driver;
    private final WebDriverWait wait;

    public PedidoProtesisPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /* =========================================================
       ORDEN DEL FLUJO (PRÓTESIS)
       1) Tipo de pedido = Prótesis + fecha de solicitud
       2) Fecha de intervención (el “Urgente” lo marca PedidoPage)
       3) Lugar de Entrega (Entidad Efectora)
       4) Buscar prótesis por nombre y agregar [+]
       5) Descripción (modal) y aceptar
       6) Cantidad + “No” (header) + “A cargo de: Osprera”
       7) Adjuntos de Prótesis (Historia Clínica, Exámenes Compl., Pedido médico, Tipo Estudio)
       8) Confirmar (opcionalmente lee N° si hay toast/modal)
       9) Enviar a Auditoría (y dejar pantalla lista para leer N°)
       ========================================================= */

    /* ========== (1) Tipo de pedido = Prótesis ========== */
    public void seleccionarTipoPedidoYFecha() {
        WebElement tipoPedido = wait.until(ExpectedConditions.elementToBeClickable(By.id("tipoPedido")));
        tipoPedido.click();
        tipoPedido.findElement(By.xpath("//option[contains(text(),'Prótesis')]")).click();

        WebElement fechaInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("FechaDesde2")));
        fechaInput.clear();
        String hoy = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        fechaInput.sendKeys(hoy);
        fechaInput.sendKeys(Keys.TAB);
    }

    /* ========== (2) Fecha de intervención ========== */
    public void fechaIntervencion(String fecha) {
        By campo = By.id("FechaDesde");
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(campo));
        input.clear();
        input.sendKeys(fecha);
        input.sendKeys(Keys.TAB);
    }

    /* ========== (3) Lugar de Entrega (Efector) ========== */
    public void lugarEntrega(String nombreEfector) {
        By inputEfector = By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/fieldset[1]/form/table/tbody/tr[2]/td/buscador-efector/div/input[2]");
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputEfector));
        input.clear();
        input.sendKeys(nombreEfector);
        input.sendKeys(Keys.ENTER); // dispara búsqueda
    }

    /* ========== (4) Buscar prótesis y agregar [+] ========== */
    public void buscarYAgregarProtesis(String nombre) {
        By inputNombre = By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/fieldset[2]/div[1]/table/tbody/tr/td[2]/buscador-material-x-tipo/div/input[3]");
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputNombre));
        input.clear();
        input.sendKeys(nombre);
        input.sendKeys(Keys.ENTER);

        By botonMas = By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/fieldset[2]/div[1]/table/tbody/tr/td[2]/buscador-material-x-tipo/div/input[4]");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(botonMas));
        btn.click();
    }

    /* ========== (5) Descripción (modal) ========== */
    public void agregarDescripcionProtesis(String descripcion) {
        // Abrir modal
        By iconoDescripcion = By.xpath("//img[contains(@src,'sin-desc')]");
        wait.until(ExpectedConditions.elementToBeClickable(iconoDescripcion)).click();

        // Esperar modal “Descripción del Material”
        By titulo = By.xpath("//span[contains(@class,'t-window-title')][contains(normalize-space(.),'Descripción del Material')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(titulo));

        // Textarea del modal
        By campoDescripcion = By.xpath("//textarea[@ng-model='objeto.Valor']");
        WebElement textarea = wait.until(ExpectedConditions.elementToBeClickable(campoDescripcion));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", textarea);

        try { textarea.click(); textarea.clear(); textarea.sendKeys(descripcion); } catch (Exception ignored) {}
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].focus(); arguments[0].value = arguments[1];" +
            "arguments[0].dispatchEvent(new Event('input',{bubbles:true}));" +
            "arguments[0].dispatchEvent(new Event('change',{bubbles:true}));",
            textarea, descripcion
        );

        // Aceptar
        By btnAceptar = By.xpath("//div[contains(@class,'modal-footer')]//button[.='Aceptar' or @ng-click='agregar()']");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
            wait.until(ExpectedConditions.elementToBeClickable(btnAceptar)));

        // Esperar cierre
        wait.until(ExpectedConditions.invisibilityOfElementLocated(campoDescripcion));
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal-backdrop"))); } catch (Exception ignored) {}
    }

    /* ========== (6) Cantidad + “No” + “A cargo de: Osprera” ========== */
    public void completarDatosProtesis(int cantidad) {
        // Cantidad
        By inputCantidad = By.xpath("//input[@ng-model='item.Cantidad']");
        WebElement cantidadInput = wait.until(ExpectedConditions.elementToBeClickable(inputCantidad));
        cantidadInput.clear();
        cantidadInput.sendKeys(String.valueOf(cantidad));

        // “No” (header)
        By checkNo = By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/fieldset[2]/div[2]/table/thead/tr/th[6]/div/span[2]/input");
        WebElement noCheckbox = wait.until(ExpectedConditions.elementToBeClickable(checkNo));
        if (!noCheckbox.isSelected()) noCheckbox.click();

        // “A cargo de: Osprera”
        seleccionarACargoDe("Osprera");
    }

    private void seleccionarACargoDe(String opcionTexto) {
        By btnDrop = By.xpath("(//fieldset[.//legend[contains(normalize-space(),'Prótesis')]]//button[@ng-click='toggleSelect()'])[last()]");
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(btnDrop));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

        By menuUl = By.xpath("(//fieldset[.//legend[contains(normalize-space(),'Prótesis')]]//button[@ng-click='toggleSelect()']/following-sibling::ul[contains(@class,'dropdown-menu')])[last()]");
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(menuUl));

        WebElement opt = menu.findElement(By.xpath(".//li/label/span[normalize-space()='" + opcionTexto + "']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opt);

        WebElement labelSpan = button.findElement(By.xpath(".//span[contains(@class,'pull-left')]"));
        wait.until(ExpectedConditions.textToBePresentInElement(labelSpan, opcionTexto));
    }

    /* ========== (7) Adjuntos de Prótesis ========== */
    public void clickCargarAdjuntosProtesis(String fechaDMY, String comentarioTxt,
                                            String rutaArchivo, String tipoEstudio) {
        // Abrir modal
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(normalize-space(),'Cargar Adjuntos')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        By modal = By.id("modaladjuntos");
        wait.until(ExpectedConditions.visibilityOfElementLocated(modal));

        // Multiselect “Tipo Documento” → Historia Clínica + Exámenes Complementarios + Pedido médico
        WebElement msBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@id='modaladjuntos']//multiselect//button")));
        msBtn.click();
        try {
            WebElement ninguna = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='modaladjuntos']//multiselect//ul//li/label[contains(normalize-space(.),'Ninguna')]")));
            ninguna.click();
        } catch (Exception ignored) {}
        seleccionarOpcionMultiselect("Historia Clínica");
        seleccionarOpcionMultiselect("Exámenes Complementarios");
        seleccionarOpcionMultiselect("Pedido médico");
        driver.findElement(By.xpath("//div[@id='modaladjuntos']//label[contains(.,'Tipo Documento')]")).click(); // cerrar

        // “Tipo Estudio” (simpleselect)
        WebElement tipoEstudioBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@id='modaladjuntos']//label[contains(.,'Tipo Estudio')]/following::button[@ng-click='toggleSelect()'][1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tipoEstudioBtn);

        WebElement opcionEstudio = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@id='modaladjuntos']//label[contains(.,'Tipo Estudio')]/following::ul[1]//li/label/span[normalize-space()='" + tipoEstudio + "']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opcionEstudio);

        // Fecha + Comentario
        WebElement fecha = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("FechaAdjunto")));
        fecha.clear(); fecha.sendKeys(fechaDMY);

        WebElement comentario = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[@id='modaladjuntos']//textarea")));
        comentario.clear(); comentario.sendKeys(comentarioTxt);

        // Archivo
        try {
            WebElement agregarHidden = wait.until(ExpectedConditions.elementToBeClickable(By.id("dale")));
            agregarHidden.click();
        } catch (TimeoutException e) {
            WebElement agregarBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='modaladjuntos']//button[contains(.,'Agregar Archivos')]")));
            agregarBtn.click();
        }
        try { Thread.sleep(800); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
        com.osprera.test.utils.RobotUtils.subirArchivo(rutaArchivo);

        // Aceptar y limpiar backdrops
        WebElement aceptarBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@id='modaladjuntos']//button[normalize-space()='Aceptar']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", aceptarBtn);

        try {
            new WebDriverWait(driver, Duration.ofSeconds(8))
                .until(ExpectedConditions.invisibilityOfElementLocated(modal));
            new WebDriverWait(driver, Duration.ofSeconds(8))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal-backdrop")));
        } catch (Exception ignored) {}
    }

    private void seleccionarOpcionMultiselect(String textoExacto) {
        WebElement label = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@id='modaladjuntos']//multiselect//ul//li/label[normalize-space()='" + textoExacto + "']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", label);
    }

    /* ========== (8) Confirmar (tolerante) ========== */
    public String confirmarYObtenerNumero() {
        By btnConfirmar = By.xpath("//button[.='Confirmar' or .='Guardar' or contains(.,'Generar Pedido')]");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnConfirmar));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        String n = tryObtenerNumeroDesdeModalToast(7);
        return (n == null) ? "" : n; // en Prótesis suele leerse después de Enviar a Auditoría
    }
    private String tryObtenerNumeroDesdeModalToast(int seconds) {
        long end = System.currentTimeMillis() + seconds * 1000L;
        By anyModal = By.xpath("//div[contains(@class,'modal') and not(contains(@style,'display: none'))]");
        By anyToast = By.xpath("//*[contains(@class,'toast') or contains(@class,'alert') or contains(@class,'ng-toast')]");
        while (System.currentTimeMillis() < end) {
            try {
                for (WebElement m : driver.findElements(anyModal)) {
                    String n = extraerNumero(m.getText()); if (n != null) return n;
                }
                for (WebElement t : driver.findElements(anyToast)) {
                    String n = extraerNumero(t.getText()); if (n != null) return n;
                }
            } catch (Exception ignored) {}
            try { Thread.sleep(250); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
        }
        return null;
    }
    private String extraerNumero(String texto) {
        if (texto == null) return null;
        String n = texto.replaceAll("(?s).*?(\\d{5,}).*", "$1");
        return (n.matches("\\d{5,}")) ? n : null;
    }

    /* ========== (9) Enviar a Auditoría (robusto) ========== */
public void enviarAAuditoria() {
    // Botón correcto (NO "Guardar")
    By btnEnviar = By.xpath("(//button[@ng-click='enviarAuditoria()' and not(contains(@class,'ng-hide'))])[last()]");

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
        } catch (Exception ignored) {}
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", boton);
    }

    // Confirmación (si aparece)
    By ok = By.xpath("//button[normalize-space(.)='Aceptar' or normalize-space(.)='OK' or contains(normalize-space(.),'Confirmar')]");
    try {
        WebElement btnOk = new WebDriverWait(driver, Duration.ofSeconds(6))
            .until(ExpectedConditions.elementToBeClickable(ok));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnOk);
    } catch (TimeoutException ignored) {}

    // Limpieza de overlays
    try {
        new WebDriverWait(driver, Duration.ofSeconds(8))
            .until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".modal-backdrop, .modal.show, .modal.fade.in")));
    } catch (Exception ignored) {}
}


    /* ======= Extras opcionales (si te sirven en pruebas unitarias de la página) ======= */
    public void cargarDiagnosticoYCIE() { /* … como ya lo tenías … */ }
    public void seleccionarMedico()      { /* … como ya lo tenías … */ }
    public void ingresarDiagnostico(String texto) { /* … */ }
    public void agregarItemProtesis(String codigo, int cantidad) { /* … */ }
}

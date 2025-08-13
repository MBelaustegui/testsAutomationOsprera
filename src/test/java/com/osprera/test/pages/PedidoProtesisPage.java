package com.osprera.test.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class PedidoProtesisPage {

    /* =========================================================
       Contexto del flujo (resumen por orden de ejecución)
       0) (Fuera de esta clase) Completar beneficiario
       1) Seleccionar tipo de pedido = Prótesis + fecha de solicitud
       2) Marcar Urgente (se hace en PedidoPage) + Fecha de intervención
       3) Lugar de Entrega (Entidad Efectora)
       4) Buscar y agregar Prótesis (por nombre) [+]
       5) Agregar Descripción (modal) y aceptar
       6) Cantidad + tildar “No” (header) + seleccionar “Osprera” en dropdown
       7) (Opcional) Adjuntar prescripción/archivo
       8) Confirmar y obtener N° de pedido
       9) Enviar a Auditoría
       ========================================================= */

    // Driver & wait
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Constructor
    public PedidoProtesisPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /* ====================== (1) Tipo de pedido ====================== */

    /**
     * Selecciona "Prótesis" como tipo de pedido y setea la fecha de solicitud (hoy).
     */
    public void seleccionarTipoPedidoYFecha() {
        WebElement tipoPedido = wait.until(ExpectedConditions.elementToBeClickable(By.id("tipoPedido")));
        tipoPedido.click();
        tipoPedido.findElement(By.xpath("//option[contains(text(),'Prótesis')]"))
                  .click();

        WebElement fechaInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("FechaDesde2")));
        fechaInput.clear();
        // Fecha de hoy en formato dd/MM/yyyy
        String hoy = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        fechaInput.sendKeys(hoy);
        fechaInput.sendKeys(Keys.TAB);
    }

    /* ====================== (2) Fecha intervención ====================== */

    /**
     * Ingresa la fecha de intervención quirúrgica (formato dd/MM/yyyy).
     * Nota: el check "Urgente" se marca desde PedidoPage.seleccionarUrgente().
     */
    public void fechaIntervencion(String fecha) {
        By fechaIntervencionInput = By.id("FechaDesde");
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(fechaIntervencionInput));
        input.clear();
        input.sendKeys(fecha);
        input.sendKeys(Keys.TAB);
    }

    /* ====================== (3) Lugar de Entrega ====================== */

    /**
     * Selecciona la Entidad Efectora (Lugar de Entrega) buscando por descripción.
     */
    public void lugarEntrega(String nombre) {
        // Input de descripción de efector (xpath absoluto provisto)
        By inputEfector = By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/fieldset[1]/form/table/tbody/tr[2]/td/buscador-efector/div/input[2]");
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputEfector));
        input.clear();
        input.sendKeys(nombre);
        input.sendKeys(Keys.ENTER); // dispara la búsqueda

       
    }

    /* ====================== (4) Prótesis: búsqueda [+] ====================== */

    /**
     * Busca una prótesis por nombre y la agrega (botón +).
     */
    public void buscarYAgregarProtesis(String nombre) {
        // Campo de nombre (xpath absoluto provisto)
        By inputNombre = By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/fieldset[2]/div[1]/table/tbody/tr/td[2]/buscador-material-x-tipo/div/input[3]");
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputNombre));
        input.clear();
        input.sendKeys(nombre);
        input.sendKeys(Keys.ENTER);

        // Botón +
        By botonMas = By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/fieldset[2]/div[1]/table/tbody/tr/td[2]/buscador-material-x-tipo/div/input[4]");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(botonMas));
        btn.click();
    }

    /* ====================== (5) Descripción (modal) ====================== */

    /**
     * Abre el modal de descripción de la prótesis, carga el texto y acepta.
     */
/** Abre el modal de descripción, escribe el texto y confirma. */
public void agregarDescripcionProtesis(String descripcion) {
    // 1) Abrir modal (icono del lápiz/sin-desc)
    By iconoDescripcion = By.xpath("//img[contains(@src,'sin-desc')]");
    WebElement icono = wait.until(ExpectedConditions.elementToBeClickable(iconoDescripcion));
    icono.click();

    // 2) Esperar a que el modal "Descripción del Material" esté visible
    By modalTitulo = By.xpath("//span[contains(@class,'t-window-title')][contains(normalize-space(.),'Descripción del Material')]");
    wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitulo));

    // 3) Tomar el textarea correcto dentro del modal
    By campoDescripcion = By.xpath("//textarea[@ng-model='objeto.Valor']");
    WebElement textarea = wait.until(ExpectedConditions.elementToBeClickable(campoDescripcion));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", textarea);

    // 4) Escribir con seguridad (focus + limpiar + set + eventos Angular)
    try {
        textarea.click();
        textarea.clear();
        textarea.sendKeys(descripcion);
    } catch (Exception ignored) {
        // fallback por si el click normal no enfoca
    }
    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].focus();" +
        "arguments[0].value = arguments[1];" +
        "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
        "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));",
        textarea, descripcion
    );

    // 5) Click en Aceptar del MISMO modal
    By btnAceptar = By.xpath("//div[contains(@class,'modal-footer')]//button[.='Aceptar' or @ng-click='agregar()']");
    WebElement aceptar = wait.until(ExpectedConditions.elementToBeClickable(btnAceptar));
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", aceptar);

    // 6) Esperar que el modal se cierre (textarea ya no visible)
    wait.until(ExpectedConditions.invisibilityOfElementLocated(campoDescripcion));
    // (opcional) esperar backdrop
    try {
        By backdrop = By.cssSelector(".modal-backdrop");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(backdrop));
    } catch (Exception ignored) {}
}


 
   /* ====================== (6) Cantidad + No (SurNo) + Osprera ====================== */

/**
 * Completa cantidad, tilda "No" (header) y selecciona "Osprera" en "A cargo de".
 */
public void completarDatosProtesis(int cantidad) {
    // Cantidad
    By inputCantidad = By.xpath("//input[@ng-model='item.Cantidad']");
    WebElement cantidadInput = wait.until(ExpectedConditions.elementToBeClickable(inputCantidad));
    cantidadInput.clear();
    cantidadInput.sendKeys(String.valueOf(cantidad));

    // Check "No" del header (xpath absoluto provisto)
    By checkNo = By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/fieldset[2]/div[2]/table/thead/tr/th[6]/div/span[2]/input");
    WebElement noCheckbox = wait.until(ExpectedConditions.elementToBeClickable(checkNo));
    if (!noCheckbox.isSelected()) {
        noCheckbox.click();
    }

    // Dropdown "A cargo de:" -> seleccionar "Osprera"
    seleccionarACargoDe("Osprera");
}

/** Abre el dropdown "A cargo de" (en Prótesis) y selecciona la opción indicada. */
private void seleccionarACargoDe(String opcionTexto) {
    // Button del simpleselect dentro del fieldset de Prótesis
    By btnDrop = By.xpath(
        "(//fieldset[.//legend[contains(normalize-space(),'Prótesis')]]" +
        "//button[@ng-click='toggleSelect()'])[last()]"
    );
    WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(btnDrop));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);
    wait.until(ExpectedConditions.elementToBeClickable(button));
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

    // Menú <ul> hermano del botón dentro del mismo .dropdown
    By menuUl = By.xpath(
        "(//fieldset[.//legend[contains(normalize-space(),'Prótesis')]]" +
        "//button[@ng-click='toggleSelect()']/following-sibling::ul[contains(@class,'dropdown-menu')])[last()]"
    );
    WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(menuUl));

    // Opción por texto (p. ej., "Osprera") dentro de ese menú
    WebElement opt = menu.findElement(By.xpath(".//li/label/span[normalize-space()='" + opcionTexto + "']"));
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opt);

    // Verificar que el label del botón refleje la selección
    WebElement labelSpan = button.findElement(By.xpath(".//span[contains(@class,'pull-left')]"));
    wait.until(ExpectedConditions.textToBePresentInElement(labelSpan, opcionTexto));
}

    /* ====================== (7) Adjuntos (opcional) ====================== */

    /**
     * Adjunta prescripción/archivo (si el input file está visible).
     */
    public void adjuntarPrescripcion(String rutaAbsoluta) {
        By inputFile = By.xpath("//input[@type='file' and (contains(@name,'prescrip') or contains(@id,'prescrip'))]");
        WebElement file = wait.until(ExpectedConditions.presenceOfElementLocated(inputFile));
        file.sendKeys(rutaAbsoluta);
        // Si se abre diálogo del SO, usar Robot/AutoIT/Katalon según tu entorno.
    }

    /* ====================== (8) Confirmar y obtener N° ====================== */

    /**
     * Confirma el pedido y devuelve el número (desde modal/toast).
     */
    public String confirmarYObtenerNumero() {
        By btnConfirmar = By.xpath("//button[.='Confirmar' or .='Guardar' or contains(.,'Generar Pedido')]");
        wait.until(ExpectedConditions.elementToBeClickable(btnConfirmar)).click();

        By popup = By.xpath("//div[contains(@class,'modal') or contains(@class,'toast')][.//text()[contains(.,'Pedido') or contains(.,'generado') or contains(.,'N°')]]");
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(popup));
        String texto = modal.getText();

        String numero = texto.replaceAll("(?s).*?(\\d{5,}).*", "$1");
        if (numero == null || numero.equals(texto)) {
            By numLocator = By.xpath("//*[contains(text(),'N°') or contains(text(),'Número')]");
            try {
                WebElement n = driver.findElement(numLocator);
                numero = n.getText().replaceAll("(?s).*?(\\d{5,}).*", "$1");
            } catch (NoSuchElementException ignore) {}
        }
        return numero;
    }

    /* ====================== (9) Enviar a Auditoría ====================== */

    /**
     * Envía el pedido a Auditoría (y acepta confirmación si aparece).
     */
    public void enviarAAuditoria() {
        By btnEnviar = By.xpath("//button[.='Enviar a auditoría' or contains(.,'Enviar a auditor')]");
        wait.until(ExpectedConditions.elementToBeClickable(btnEnviar)).click();

        By btnOk = By.xpath("//button[.='Aceptar' or .='OK' or contains(.,'Confirmar')]");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btnOk)).click();
        } catch (TimeoutException ignored) {}
    }

    /* ====================== (Extras / apoyo si los necesitás) ====================== */

    /**
     * (Extra) Carga CIE y diagnóstico (versión embebida aquí).
     */
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

    /**
     * (Extra) Selección de médico (versión embebida aquí).
     */
    public void seleccionarMedico() {
        WebElement medicoInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//buscador-medico-solicitante/div/input[2]")));
        medicoInput.click();
        medicoInput.sendKeys("Garriga juan jose");
        medicoInput.sendKeys(Keys.ENTER);
    }

    /**
     * (Extra) Escribe diagnóstico directo en campo libre.
     */
    public void ingresarDiagnostico(String texto) {
        By diagInput = By.xpath("//label[contains(.,'Diagnóstico')]/following::textarea[1]|//label[contains(.,'Diagnóstico')]/following::input[1]");
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(diagInput));
        el.clear();
        el.sendKeys(texto);
    }

    /**
     * (Extra) Alternativa para agregar ítem de prótesis por código (no usada en el flujo principal).
     */
    public void agregarItemProtesis(String codigo, int cantidad) {
        By buscador = By.xpath("//input[@placeholder='Buscar ítem de prótesis' or @placeholder='Código de prótesis']");
        WebElement search = wait.until(ExpectedConditions.elementToBeClickable(buscador));
        search.clear();
        search.sendKeys(codigo);

        By primerResultado = By.xpath("(//div[contains(@class,'autocomplete')]//li|//ul[contains(@class,'dropdown')]//li)[1]");
        wait.until(ExpectedConditions.elementToBeClickable(primerResultado)).click();

        By cantidadInput = By.xpath("//label[contains(.,'Cantidad')]/following::input[1]");
        WebElement qty = wait.until(ExpectedConditions.visibilityOfElementLocated(cantidadInput));
        qty.clear();
        qty.sendKeys(String.valueOf(cantidad));

        By btnAgregar = By.xpath("//button[.='Agregar' or contains(.,'Agregar ítem')]");
        wait.until(ExpectedConditions.elementToBeClickable(btnAgregar)).click();

        By filaItem = By.xpath("//table//tr[td[contains(.,'" + codigo + "')]]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(filaItem));
    }
}

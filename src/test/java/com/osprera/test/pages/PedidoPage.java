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

   public void cargarDiagnosticoYCIE(String cieCodigo, String diagnosticoTexto) {
    WebElement inputCie = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//buscador-cie/div/input")));
    inputCie.clear();
    inputCie.sendKeys(cieCodigo);
    driver.findElement(By.xpath("//buscador-cie/div/img")).click();

   // WebElement agregarBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//buscador-cie/div/input[4]")));
   // ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", agregarBtn);
   // ((JavascriptExecutor) driver).executeScript("arguments[0].click();", agregarBtn);

    WebElement diagnosticoTextarea = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//textarea")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", diagnosticoTextarea);
    diagnosticoTextarea.clear();
    diagnosticoTextarea.sendKeys(diagnosticoTexto);
}

    public void seleccionarMedico(String medicoBusqueda) {
    WebElement medicoInput = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//buscador-medico-solicitante/div/input[2]")));
    medicoInput.click();
    medicoInput.clear();
    medicoInput.sendKeys(medicoBusqueda);
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

    public void buscarYAgregarMedicamento(String medicamentoNombre) {
        // Limpiar el campo de búsqueda
        WebElement monoInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//buscador-medicamentos-x-monodroga[@id='buscadorMonodrogas']/div/input[3]")));
        monoInput.clear();
        monoInput.sendKeys(medicamentoNombre);
        monoInput.sendKeys(Keys.ENTER);

        // Esperar y hacer clic en el medicamento encontrado
        WebElement spanMedicamento = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[@id='tableBodyExamen']/td/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", spanMedicamento);

        // Hacer clic en el botón Agregar
        WebElement botonAgregar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//buscador-medicamentos-x-monodroga[@id='buscadorMonodrogas']/div/input[4]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botonAgregar);

        // Hacer clic en el medicamento seleccionado para agregarlo a la tabla
        String xpathMedicamento = String.format("(//*[normalize-space(text()) and contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'%s')])[1]/following::td[2]", medicamentoNombre.toLowerCase());
        WebElement medicamentoSeleccionado = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathMedicamento)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", medicamentoSeleccionado);
    }

    public void ingresarDetallesMedicamento(String medicamentoNombre, String cantidad, String dosis, String tipoDosis, String sur) {
        // Esperar un momento para que el medicamento se agregue a la tabla
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Solo para el primer medicamento (ibu)
        // Ingresar cantidad
        WebElement cantidadInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[2]/table/tbody/tr/td[6]/input")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", cantidadInput);
        cantidadInput.sendKeys(cantidad);

        // Ingresar dosis
        WebElement dosisInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[2]/table/tbody/tr/td[7]/input")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", dosisInput);
        dosisInput.sendKeys(dosis);

        // Seleccionar tipo de dosis
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//simpleselect[@id='idComboDosisPeriodo']/div/button/div/span"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//simpleselect[@id='idComboDosisPeriodo']/div/ul/li[4]/label/span"))).click();

        // Seleccionar SUR
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='SurNo']"))).click();
        
        // NUEVO PASO: Seleccionar "Osprera" después de la selección SUR
        seleccionarOspreraDespuesDeSUR();
    }

    public void seleccionarACargoDeOSPRERA() {
        String ambiente = com.osprera.test.utils.EnvironmentManager.getCurrentEnvironment();
        if ("produccion".equals(ambiente)) {
            // Hacer click en el botón del dropdown "A cargo de" - usar el botón directamente
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//simpleselect[@id='idComboACargoDe']//button[@ng-click='toggleSelect()']"))).click();
            
            // Seleccionar "Osprera" (segunda opción en el dropdown)
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//simpleselect[@id='idComboACargoDe']//label[@class='sip-multiselect-opciones']//span[text()='Osprera']"))).click();
        }
    }

    public void seleccionarOspreraDespuesDeSUR() {
        try {
            System.out.println("🔍 Buscando dropdown después de selección SUR...");
            Thread.sleep(2000);
            
            // Buscar todos los botones con toggleSelect para debug
            var botones = driver.findElements(By.xpath("//button[@ng-click='toggleSelect()']"));
            System.out.println("📊 Encontrados " + botones.size() + " botones con toggleSelect");
            
            // Analizar cada botón para encontrar el correcto
            for (int i = 0; i < botones.size(); i++) {
                try {
                    WebElement boton = botones.get(i);
                    String texto = boton.getText();
                    String clases = boton.getAttribute("class");
                    String estilo = boton.getAttribute("style");
                    boolean visible = boton.isDisplayed();
                    boolean habilitado = boton.isEnabled();
                    
                    System.out.println("🔍 Botón " + (i+1) + ":");
                    System.out.println("   Texto: '" + texto + "'");
                    System.out.println("   Clases: " + clases);
                    System.out.println("   Estilo: " + estilo);
                    System.out.println("   Visible: " + visible + ", Habilitado: " + habilitado);
                    
                    // Buscar el botón que tenga "Seleccione..." en el texto
                    if (texto.contains("Seleccione") && visible && habilitado) {
                        System.out.println("✅ ¡Encontrado! Botón con 'Seleccione...'");
                        boton.click();
                        System.out.println("✅ Click realizado en dropdown");
                        
                        Thread.sleep(1500);
                        
                        // Buscar "Osprera"
                        var opciones = driver.findElements(By.xpath("//span[text()='Osprera']"));
                        System.out.println("📊 Encontradas " + opciones.size() + " opciones con texto 'Osprera'");
                        
                        if (!opciones.isEmpty()) {
                            opciones.get(0).click();
                            System.out.println("✅ Osprera seleccionado exitosamente");
                            return;
                        } else {
                            System.out.println("⚠️ No se encontró la opción 'Osprera'");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("❌ Error analizando botón " + (i+1) + ": " + e.getMessage());
                }
            }
            
            System.out.println("⚠️ No se encontró ningún botón con 'Seleccione...'");
            
        } catch (Exception e) {
            System.out.println("⚠️ Error en seleccionarOspreraDespuesDeSUR: " + e.getMessage());
        }
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
        RobotUtils.subirArchivo("pedido_medico.txt");

        WebElement aceptarBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='modaladjuntos']//button[2]")));
        aceptarBtn.click();
    }

    public void enviarAAuditoria() {
        WebElement botonEnviar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Enviar a Auditoría')]")));
        
        // Usar JavaScript click para evitar intercepts del modal backdrop
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botonEnviar);
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

    public void clickPestanaPendientes() {
        // Este método hace click en la pestaña "Pendientes" después de ingresar al módulo SGP
        // Solo se usa en producción
        try {
            System.out.println("🔍 Buscando pestaña 'Pendientes'...");
            Thread.sleep(2000);
            
            // Buscar todos los elementos que contengan "Pendientes" para debug
            var elementosPendientes = driver.findElements(By.xpath("//*[contains(text(),'Pendientes')]"));
            System.out.println("📊 Encontrados " + elementosPendientes.size() + " elementos con texto 'Pendientes'");
            
            // Analizar cada elemento encontrado
            for (int i = 0; i < elementosPendientes.size(); i++) {
                try {
                    WebElement elemento = elementosPendientes.get(i);
                    String tagName = elemento.getTagName();
                    String texto = elemento.getText();
                    String href = elemento.getAttribute("href");
                    boolean visible = elemento.isDisplayed();
                    boolean habilitado = elemento.isEnabled();
                    
                    System.out.println("🔍 Elemento " + (i+1) + ":");
                    System.out.println("   Tag: " + tagName);
                    System.out.println("   Texto: '" + texto + "'");
                    System.out.println("   Href: " + href);
                    System.out.println("   Visible: " + visible + ", Habilitado: " + habilitado);
                    
                    // Buscar el elemento que sea un enlace clickeable con "Pendientes"
                    if (texto.trim().equals("Pendientes") && visible && habilitado) {
                        System.out.println("✅ ¡Encontrado! Elemento clickeable con 'Pendientes'");
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
                        System.out.println("✅ Click realizado en pestaña 'Pendientes'");
                        return;
                    }
                } catch (Exception e) {
                    System.out.println("❌ Error analizando elemento " + (i+1) + ": " + e.getMessage());
                }
            }
            
            // Si no se encontró con el método anterior, intentar selectores alternativos
            String[] selectores = {
                "//a[contains(text(),'Pendientes')]",
                "//li//a[contains(text(),'Pendientes')]",
                "//span[contains(text(),'Pendientes')]",
                "//*[@href and contains(text(),'Pendientes')]",
                "//a[@href='#/solicitudes/auditoria/consultas']",
                "//a[contains(@href,'auditoria') and contains(text(),'Pendientes')]"
            };
            
            for (String selector : selectores) {
                try {
                    WebElement elemento = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(selector)));
                    System.out.println("✅ Elemento encontrado con selector: " + selector);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
                    System.out.println("✅ Click realizado en pestaña 'Pendientes'");
                    return;
                } catch (Exception e) {
                    System.out.println("❌ Falló selector: " + selector);
                }
            }
            
            System.out.println("⚠️ No se encontró ningún elemento clickeable con 'Pendientes'");
            
        } catch (Exception e) {
            System.out.println("⚠️ Error en clickPestanaPendientes: " + e.getMessage());
            // No lanzar excepción para que el test continúe
        }
    }

    public void cerrarNavegador() {
        driver.quit();
    }

}

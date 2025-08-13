package com.osprera.test.hooks;

import io.cucumber.java.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;

import static org.junit.Assume.assumeFalse;

import com.osprera.test.utils.FailFast;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class Hooks {
    public static WebDriver driver;
    private WebDriverWait wait;

    // 1) Guard: corre PRIMERO y corta si ya falló un escenario del flujo
    @Before(value = "@flujoPedidoCompleto", order = 0)
    public void failFastGuard() {
        assumeFalse("Se detiene el flujo porque falló un escenario anterior.", FailFast.shouldStop());
    }

    // 2) ÚNICO setup que puede levantar driver (para ambos escenarios del flujo)
   @Before(value = "@admDeleg or @admDelegProtesis or @audDeleg or @audCentral or @audCentralProtesis or @flujoPedidoCompleto or @flujoPedidoProtesis", order = 10)
public void setUp(Scenario scenario) {
    assumeFalse("Se detiene el flujo porque falló un escenario anterior.", FailFast.shouldStop());

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--start-maximized", "--disable-notifications", "--remote-allow-origins=*");
    driver = new ChromeDriver(options);
    wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    driver.get("https://sioqa.osprera.org.ar/#/login");

    // Login automático por tag
    if (scenario.getSourceTagNames().contains("@admDeleg") || 
        scenario.getSourceTagNames().contains("@admDelegProtesis")) {
        login("prueba_admdeleg", "test1234");
    } else if (scenario.getSourceTagNames().contains("@audDeleg")) {
        login("prueba_auddeleg", "test1234");
    } else if (scenario.getSourceTagNames().contains("@audCentral")) {
        login("prueba_audcentral", "test1234");
    } else if (scenario.getSourceTagNames().contains("@audCentralProtesis")) {
        login("prueba_protCentral", "test1234");
    }
}

    private void login(String usuario, String clave) {
        driver.get("https://siodev.osprera.org.ar/#/login");

        WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//input[@placeholder='Usuario']")));
        user.clear();
        user.sendKeys(usuario);

        WebElement pass = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//input[@type='password']")));
        pass.clear();
        pass.sendKeys(clave);

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.dropdown-toggle"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Externo')]"))).click();

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(),'Iniciar Sesión')]")));
        loginButton.click();
        System.out.println("✅ Login: " + usuario);
    }

    // 3) Marca STOP si falla un escenario del flujo
    @After("@flujoPedidoCompleto")
    public void marcarFail(Scenario scenario) {
        if (scenario.isFailed()) {
            FailFast.trigger();
        }
    }

    // 4) Screenshot cuando falla un paso (opcional, útil para debug)
    @AfterStep
    public void screenshotOnStepFailure(Scenario scenario) {
        if (scenario.isFailed() && driver instanceof TakesScreenshot) {
            try {
                byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(png, "image/png", "🔴 Falla en paso");
            } catch (Exception e) {
                System.out.println("⚠️ No se pudo capturar screenshot de paso: " + e.getMessage());
            }
        }
    }

    // 5) Teardown + evidencias SIEMPRE (no adjunta si fue SKIPPED). Corre último.
    @After(order = 1000)
    public void tearDown(Scenario scenario) {
        try {
            if (driver != null && !"SKIPPED".equals(String.valueOf(scenario.getStatus()))) {
                if (driver instanceof TakesScreenshot) {
                    byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    String title = (scenario.isFailed() ? "❌ Falla - " : "✅ OK - ") + scenario.getName();
                    scenario.attach(png, "image/png", title);
                }
                // Resumen textual
                String resumen = "Escenario: " + scenario.getName() + "\nEstado: " + scenario.getStatus();
                scenario.attach(resumen.getBytes(StandardCharsets.UTF_8), "text/plain", "📝 Resultado");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Teardown: " + e.getMessage());
        } finally {
            try { if (driver != null) driver.quit(); } catch (Exception ignored) {}
            driver = null;
        }
    }
}

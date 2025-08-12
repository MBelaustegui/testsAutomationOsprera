package com.osprera.test.utils;

import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public final class UiWaits {
    private UiWaits() {}

    public static void waitBackdropGone(WebDriver driver, long seconds) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            w.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".backdrop, .modal-backdrop, .cdk-overlay-backdrop, .modal-backdrop.in")
            ));
        } catch (TimeoutException ignored) {}
    }

    public static boolean appears(WebDriver driver, By by, long seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}

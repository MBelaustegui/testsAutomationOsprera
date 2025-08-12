package com.osprera.test.utils;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class RobotUtils {
    public static void subirArchivo(String rutaCompleta) {
        try {
            StringSelection ss = new StringSelection(rutaCompleta);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
            Robot robot = new Robot();
            robot.setAutoDelay(500);

            // Ctrl + V
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);

            // Enter
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}

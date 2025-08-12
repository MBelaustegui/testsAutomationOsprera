package com.osprera.test.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Vars {
    private static final Properties props = new Properties();
    static {
        try {
            props.load(new FileInputStream("src/test/resources/testdata/default.properties"));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar default.properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static String required(String key) {
        String value = props.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Variable requerida no encontrada: " + key);
        }
        return value;
    }
}

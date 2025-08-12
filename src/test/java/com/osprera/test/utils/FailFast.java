package com.osprera.test.utils;

import java.util.concurrent.atomic.AtomicBoolean;

public class FailFast {
    private static final AtomicBoolean STOP = new AtomicBoolean(false);

    public static void reset() { STOP.set(false); }
    public static void trigger() { STOP.set(true); }
    public static boolean shouldStop() { return STOP.get(); }
}

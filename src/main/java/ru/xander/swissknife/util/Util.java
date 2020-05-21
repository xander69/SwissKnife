package ru.xander.swissknife.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Alexander Shakhov
 */
public final class Util {
    private Util() {
        throw new IllegalStateException("Utility class");
    }

    public static File getJdkBin() {
        File javaHome = new File(System.getProperty("java.home"));
        return new File(javaHome.getParentFile(), "bin");
    }

    public static <T> T nvl(T value, T defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof String) {
            if (((String) value).isEmpty()) {
                return defaultValue;
            }
        }
        return value;
    }

    public static Integer parseInt(String value, Integer defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}

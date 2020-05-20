package ru.xander.swissknife.util;

/**
 * @author Alexander Shakhov
 */
public final class Util {
    private Util() {
        throw new IllegalStateException("Utility class");
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
}

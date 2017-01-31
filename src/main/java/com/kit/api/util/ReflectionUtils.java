package com.kit.api.util;

import java.lang.reflect.Field;

/**
 */
public final class ReflectionUtils {

    private ReflectionUtils() {

    }

    public static Object stringToValue(Field field, String value) {
        if (float.class.isAssignableFrom(field.getType())) {
            return Float.valueOf(value);
        } else if (double.class.isAssignableFrom(field.getType())) {
            return Double.valueOf(value);
        } else if (int.class.isAssignableFrom(field.getType())) {
            return Integer.valueOf(value);
        } else if (long.class.isAssignableFrom(field.getType())) {
            return Long.valueOf(value);
        } else if (boolean.class.isAssignableFrom(field.getType())) {
            return Boolean.valueOf(value);
        }
        return value;
    }

}

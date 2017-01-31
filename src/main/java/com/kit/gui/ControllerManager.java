package com.kit.gui;

import java.util.HashMap;

/**
 * @author : const_
 */
public class ControllerManager {

    private static final HashMap<Class<?>, Controller> CONTROLLER_MAP = new HashMap<>();

    public static <T> T get(Class<T> clazz) {
        return (T) CONTROLLER_MAP.get(clazz);
    }

    public static void add(Class<?> clazz, Controller controller) {
        CONTROLLER_MAP.put(clazz, controller);
    }

}

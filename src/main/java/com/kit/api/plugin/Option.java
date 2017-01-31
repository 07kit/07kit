package com.kit.api.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Option {

    enum Type {
        TEXT, NUMBER, TOGGLE, HIDDEN
    }

    String label();

    String value();

    Type type();

}

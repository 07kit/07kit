package com.kit.game.transform.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation that indicates the presence of a getter.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Getter {

    /**
     * Name of the field that is obtained.
     *
     * @return name
     */
    String value();

}

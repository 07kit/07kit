package com.kit.game.transform.annotation;

/**
 * An annotation that indicates the presence of a setter.
 *
 */
public @interface Setter {

    /**
     * Name of the field that this setter affects.
     *
     * @return name
     */
    String value();

}

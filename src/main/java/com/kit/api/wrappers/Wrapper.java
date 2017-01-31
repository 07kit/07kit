package com.kit.api.wrappers;

/**
 * Contract for wrapper classes
 *
 */
public interface Wrapper<T> {

    /**
     * Obtain the original (wrapped) object.
     *
     * @return original
     */
    T unwrap();

}

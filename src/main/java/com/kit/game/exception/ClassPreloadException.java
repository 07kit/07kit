package com.kit.game.exception;

/**
 * An exception thrown when our custom classloader fails.
 *
 */
public final class ClassPreloadException extends RuntimeException {

    public ClassPreloadException(String name, Throwable cause) {
        super("Class preloading failed for " + name, cause);
    }

}

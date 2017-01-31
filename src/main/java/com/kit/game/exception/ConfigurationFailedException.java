package com.kit.game.exception;

/**
 * An exception thrown when something is wrong with the client configuration.
 *
 */
public final class ConfigurationFailedException extends RuntimeException {

    public ConfigurationFailedException(Exception cause) {
        super("There was a problem loading the client configuration.", cause);
    }

}

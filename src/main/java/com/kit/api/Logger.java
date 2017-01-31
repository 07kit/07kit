package com.kit.api;

import java.util.logging.Level;

/**
 * Provides logging functionality
 *
 * @author tommo
 */
public interface Logger {

    /**
     * Logs a message with default severity {@link Level} of <i>Level.INFO</i>
     *
     * @param message The message to log
     */
    void log(Object message);

    /**
     * Logs a message with a specified severity level
     *
     * @param level   The severity level
     * @param message The message to log
     */
    void log(Level level, Object message);

    /**
     * Logs a message with a specified severity level and a throwable
     *
     * @param level   The severity level
     * @param message The message to log
     * @param t       The throwable
     */
    void log(Level level, Object message, Throwable t);

}

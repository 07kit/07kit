package com.kit.api.event.exeption;

/**
 * Exception thrown if the invocation of an event handler fails.
 *
 */
public class HandlerInvocationException extends RuntimeException {

    public HandlerInvocationException(String message, Throwable t) {
        super(message, t);
    }

}

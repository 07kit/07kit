package com.kit.core.event;

/**
 * An event fired when a property in the environment is changed.
 *
 */
public final class EnvironmentChangedEvent {
    private final String propertyName;
    private final Object oldValue;
    private final Object newValue;

    /**
     * Constructor
     *
     * @param propertyName Name of the changed property
     * @param oldValue     The old value of the property (can be null)
     * @param newValue     The new value of the property (can be null)
     */
    public EnvironmentChangedEvent(String propertyName, Object oldValue, Object newValue) {
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}

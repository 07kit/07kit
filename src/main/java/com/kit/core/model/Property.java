package com.kit.core.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.kit.api.event.PropertyChangedEvent;
import com.kit.core.Session;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A property to be serialized to disk
 *
 * @author tommo
 */
public final class Property {
    private static final Container<Property> container = new Container<Property>("properties") {
        @Override
        public Type getElementsType() {
            return new TypeToken<List<Property>>(){}.getType();
        }
    };

    @SerializedName("key")
    private String key;
    @SerializedName("value")
    private String value;

    public Property(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Property() {

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "[key=" + key + ", value=" + value + "]";
    }

    public static Container<Property> getContainer() {
        return container;
    }

    public void save() {
        if (container.contains(this)) {
            container.remove(this);
        }
        container.add(this);
        container.save();
        Session.get().getEventBus().submit(new PropertyChangedEvent(this));
    }

    public void remove() {
        container.remove(this);
        container.save();
    }

    public static Property get(String key) {
        for (Property p : getAll()) {
            if (p.getKey() != null && p.getKey().equals(key)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;

        return key != null ? key.equals(property.key) : property.key == null;

    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    public static List<Property> getAll() {
        return container.getAll();
    }

}


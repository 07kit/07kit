package com.kit.api.plugin;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.kit.core.model.Container;
import com.kit.core.model.Container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 */
public class PersistentOptions extends Container<PersistentOptions.OptionProxy> {

    public PersistentOptions(String fileName) {
        super(fileName);
    }

    @Override
    public Type getElementsType() {
        return new TypeToken<List<OptionProxy>>() {
        }.getType();
    }

    public static class OptionProxy {
        @SerializedName("field")
        public String field;
        @SerializedName("type")
        public Option.Type type;
        @SerializedName("label")
        public String label;
        @SerializedName("value")
        public String value;
    }

}

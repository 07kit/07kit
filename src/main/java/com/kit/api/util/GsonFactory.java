package com.kit.api.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * This class is used for building json data
 *
 */
public final class GsonFactory {
    private final GsonBuilder builder;

    /**
     * Constructor
     */
    public GsonFactory() {
        this.builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));
        builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
                return jsonSerializationContext.serialize(date.getTime());
            }
        });

    }

    /**
     * Create new instance of Gson
     *
     * @return the new instance
     */
    public Gson newInstance() {
        return builder.create();
    }

}

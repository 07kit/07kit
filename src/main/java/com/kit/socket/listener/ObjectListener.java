package com.kit.socket.listener;

import com.google.gson.Gson;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;
import com.kit.socket.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ObjectListener<T> extends SimpleListener {

    private final Class<T> eventClass;

    public ObjectListener(String eventName, Class<T> eventClass, Client client) {
        super(eventName, client);
        this.eventClass = eventClass;
    }

    public abstract void onReceive(T evt);

    @Override
    public void call(Object... objects) {
        for (Object o : objects) {
            JSONObject jsonObject = (JSONObject) o;
            T evt = GSON.fromJson(jsonObject.toString(), eventClass);
            onReceive(evt);
        }
    }
}

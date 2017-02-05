package com.kit.socket.listener;

import org.json.JSONObject;
import com.kit.socket.ClientService;

public abstract class ObjectListener<T> extends SimpleListener {

    private final Class<T> eventClass;

    public ObjectListener(String eventName, Class<T> eventClass, ClientService clientService) {
        super(eventName, clientService);
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

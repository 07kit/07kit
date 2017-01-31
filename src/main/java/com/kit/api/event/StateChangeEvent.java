package com.kit.api.event;

import com.kit.core.Session;
import com.kit.core.Session;

/**
 */
public class StateChangeEvent {
    private Session.State state;

    public StateChangeEvent(Session.State state) {
        this.state = state;
    }

    public Session.State getState() {
        return state;
    }

    public void setState(Session.State state) {
        this.state = state;
    }
}

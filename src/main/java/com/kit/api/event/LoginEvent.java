package com.kit.api.event;

/**
 * @author : const_
 */
public class LoginEvent {

    private long time;

    public LoginEvent(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}

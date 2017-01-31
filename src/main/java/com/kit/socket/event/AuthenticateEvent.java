package com.kit.socket.event;

import com.google.gson.annotations.SerializedName;

public class AuthenticateEvent {

    public enum Status {
        PENDING, SUCCESS, FAILED;
    }

    public static final String EVENT_NAME = "authenticate_evt";

    @SerializedName("token")
    private final String token;
    @SerializedName("status")
    private Status status;

    public AuthenticateEvent(String token, Status status) {
        this.token = token;
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public Status getStatus() {
        return status;
    }
}

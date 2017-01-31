package com.kit.http;

import com.google.gson.annotations.SerializedName;

public final class SuccessResponse {

    @SerializedName("message")
    private final String message;

    public SuccessResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

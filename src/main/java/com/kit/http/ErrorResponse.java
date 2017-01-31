package com.kit.http;

import com.google.gson.annotations.SerializedName;

/**
 * A response type that wraps an error message
 *
 */
public final class ErrorResponse {

    @SerializedName("error")
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}

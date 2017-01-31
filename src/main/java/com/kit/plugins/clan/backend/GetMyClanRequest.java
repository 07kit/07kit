package com.kit.plugins.clan.backend;

import com.google.gson.annotations.SerializedName;

public class GetMyClanRequest {

    @SerializedName("loginName")
    public String loginName;

    public GetMyClanRequest(String loginName) {
        this.loginName = loginName;
    }
}

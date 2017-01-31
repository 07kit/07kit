package com.kit.plugins.twitch;

import com.google.gson.annotations.SerializedName;

public class TwitchUserInfo {

    @SerializedName("identified")
    private boolean identified;
    @SerializedName("token")
    private Token token;

    public boolean isIdentified() {
        return identified;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "TwitchUserInfo{" +
                "identified=" + identified +
                ", token=" + token +
                '}';
    }

    public class Token {

        @SerializedName("valid")
        private boolean valid;
        @SerializedName("user_name")
        private String username;
        @SerializedName("client_id")
        private String clientId;

        public Token(boolean valid, String username, String clientId) {
            this.valid = valid;
            this.username = username;
            this.clientId = clientId;
        }

        public boolean isValid() {
            return valid;
        }

        public String getUsername() {
            return username;
        }

        public String getClientId() {
            return clientId;
        }

        @Override
        public String toString() {
            return "Token{" +
                    "valid=" + valid +
                    ", username='" + username + '\'' +
                    ", clientId='" + clientId + '\'' +
                    '}';
        }
    }
}

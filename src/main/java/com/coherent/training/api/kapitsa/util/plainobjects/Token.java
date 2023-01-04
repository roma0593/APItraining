package com.coherent.training.api.kapitsa.util.plainobjects;

public class Token {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String scope;

    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        return token_type;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public String getScope() {
        return scope;
    }
}

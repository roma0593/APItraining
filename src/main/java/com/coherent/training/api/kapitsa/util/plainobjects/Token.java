package com.coherent.training.api.kapitsa.util.plainobjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("token_type")
    private String token_type;
    @JsonProperty("expires_in")
    private String expires_in;
    @JsonProperty("scope")
    private String scope;

    public Token() {
    }

    public Token(String access_token, String token_type, String expires_in, String scope) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.scope = scope;
    }

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

    @Override
    public String toString() {
        return "Token{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}

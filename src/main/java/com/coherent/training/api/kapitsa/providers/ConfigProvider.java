package com.coherent.training.api.kapitsa.providers;

public enum ConfigProvider {
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    PROFILE("profile"),
    HOST_KEY("%s.host");

    private final String propertyKey;

    ConfigProvider(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyKey() {
        return propertyKey;
    }
}

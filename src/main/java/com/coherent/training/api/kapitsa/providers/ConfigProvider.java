package com.coherent.training.api.kapitsa.providers;

public enum ConfigProvider {
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret");

    private final String propertyKey;

    ConfigProvider(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyKey() {
        return propertyKey;
    }
}

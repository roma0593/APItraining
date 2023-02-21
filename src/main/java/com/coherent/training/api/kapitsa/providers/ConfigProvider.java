package com.coherent.training.api.kapitsa.providers;

public enum ConfigProvider {
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    PROFILE("profile"),
    HOST_KEY("%s.host"),
    CLIENT_CLASS("client.class"),
    PATH_TO("path.to.%s");

    private final String propertyKey;

    ConfigProvider(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyKey() {
        return propertyKey;
    }
}

package com.coherent.training.api.kapitsa.providers;

public enum UrlProvider {
    OAUTH_URL("/oauth/token"),
    GET_ZIP_CODES("/zip-codes"),
    EXPAND_ZIP_CODES("/zip-codes/expand");

    private static final String HOST_URL = ConfigFileReader.getInstance().getHostUrl();

    private final String endpoint;

    UrlProvider(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return HOST_URL + endpoint;
    }
}

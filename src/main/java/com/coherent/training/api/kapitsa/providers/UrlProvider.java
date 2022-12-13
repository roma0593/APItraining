package com.coherent.training.api.kapitsa.providers;

public enum UrlProvider {
    OAUTH_URL("http://localhost:5050/oauth/token"),
    GET_ZIP_CODES("http://localhost:5050/zip-codes"),
    EXPAND_ZIP_CODES("http://localhost:5050/zip-codes/expand");

    private final String url;

    UrlProvider(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

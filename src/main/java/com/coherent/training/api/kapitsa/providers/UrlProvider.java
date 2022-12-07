package com.coherent.training.api.kapitsa.providers;

public enum UrlProvider {
    OAUTH_URL("http://localhost:5050/oauth/token");

    private final String url;

    UrlProvider(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

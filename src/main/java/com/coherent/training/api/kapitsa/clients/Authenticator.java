package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.base.Client;
import com.coherent.training.api.kapitsa.base.ResponseWrapper;
import com.coherent.training.api.kapitsa.providers.ConfigFileReader;
import com.coherent.training.api.kapitsa.util.plainobjects.Token;
import lombok.SneakyThrows;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.coherent.training.api.kapitsa.providers.UrlProvider.OAUTH_URL;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class Authenticator {
    private static final String CLIENT_ID = ConfigFileReader.getInstance().getClientId();
    private static final String CLIENT_SECRET = ConfigFileReader.getInstance().getClientSecret();
    private static final String oauthUrl = OAUTH_URL.getEndpoint();

    private Authenticator() {
    }

    private static class SingletonAuthenticator {
        private static final Authenticator INSTANCE = new Authenticator();
    }

    public static Authenticator getInstance() {
        return SingletonAuthenticator.INSTANCE;
    }

    public String getBearerTokenForReadScope(Client client) {
        return getBearerTokenForScope("read", client);
    }

    public String getBearerTokenForWriteScope(Client client) {
        return getBearerTokenForScope("write", client);
    }

    @SneakyThrows
    private String getBearerTokenForScope(String scope, Client client) {
        try (ResponseWrapper response = client.post(oauthUrl, setHeadersMap(), getFormMap(scope))) {

            Token token = client.getResponseBody(Token.class);

            return token.getAccessToken();
        }
    }

    @SneakyThrows
    private Map<String, String> getFormMap(String authScope) {
        Map<String, String> formMap = new HashMap<>();

        formMap.put("grant_type", "client_credentials");
        formMap.put("client_id", CLIENT_ID);
        formMap.put("scope", authScope);

        return formMap;
    }

    private String getEncodedBasicAuthData() {
        String keys = CLIENT_ID + ":" + CLIENT_SECRET;

        return Base64.getEncoder().encodeToString(keys.getBytes());
    }

    private Map<String, String> setHeadersMap() {
        Map<String, String> headersMap = new HashMap<>();

        headersMap.put(AUTHORIZATION, "Basic " + getEncodedBasicAuthData());
        headersMap.put(CONTENT_TYPE, "application/x-www-form-urlencoded");

        return headersMap;
    }
}

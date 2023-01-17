package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.base.BaseClientObject;
import com.coherent.training.api.kapitsa.providers.ConfigFileReader;
import com.coherent.training.api.kapitsa.util.plainobjects.Token;
import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

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

    public String getBearerTokenForReadScope(CloseableHttpClient client) {
        return getBearerTokenForScope("read", client);
    }

    public String getBearerTokenForWriteScope(CloseableHttpClient client) {
        return getBearerTokenForScope("write", client);
    }

    @SneakyThrows
    private String getBearerTokenForScope(String scope, CloseableHttpClient client) {
        BaseClientObject baseClient = new BaseClientObject(client);

        try(CloseableHttpResponse response = baseClient.post(oauthUrl, setHeadersMap(), getFormEntity(scope))) {
            Token token = baseClient.getSuccessResponseBody(Token.class, response);

            return token.getAccessToken();
        }
    }

    @SneakyThrows
    private StringEntity getFormEntity(String authScope) {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("grant_type", "client_credentials"));
        params.add(new BasicNameValuePair("client_id", CLIENT_ID));
        params.add(new BasicNameValuePair("scope", authScope));

        return new UrlEncodedFormEntity(params, "utf-8");
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

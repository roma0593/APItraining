package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.base.BaseClientObject;
import com.coherent.training.api.kapitsa.util.plainobjects.Scope;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.HashMap;
import java.util.Map;

import static com.coherent.training.api.kapitsa.clients.Authenticator.getInstance;
import static com.coherent.training.api.kapitsa.util.plainobjects.Scope.READ;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class BaseClient {
    protected static final Authenticator authenticator = getInstance();
    protected BaseClientObject baseClient;
    protected CloseableHttpResponse response;

    public BaseClient (CloseableHttpClient client) {
        baseClient = new BaseClientObject(client);
    }

    protected Map<String, String> setHeadersMap(Scope scope){
        CloseableHttpClient client = baseClient.getClient();
        String bearerToken = (scope.equals(READ)) ? authenticator.getBearerTokenForReadScope(client)
                : authenticator.getBearerTokenForWriteScope(client);

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(AUTHORIZATION, "Bearer " + bearerToken);
        headersMap.put(CONTENT_TYPE, "application/json");

        return headersMap;
    }

    public int getStatusCodeOfResponse(){
        return baseClient.getResponseCode(response);
    }
}

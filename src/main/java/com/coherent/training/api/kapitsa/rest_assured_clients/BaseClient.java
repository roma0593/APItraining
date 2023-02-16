package com.coherent.training.api.kapitsa.rest_assured_clients;

import com.coherent.training.api.kapitsa.base.RestAssuredClient;
import com.coherent.training.api.kapitsa.util.plainobjects.Scope;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static com.coherent.training.api.kapitsa.util.plainobjects.Scope.READ;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class BaseClient {
    protected static final com.coherent.training.api.kapitsa.rest_assured_clients.Authenticator authenticator = com.coherent.training.api.kapitsa.rest_assured_clients.Authenticator.getInstance();
    protected RestAssuredClient client;
    protected Response response;

    public BaseClient() {
        client = new RestAssuredClient();
    }

    protected Map<String, String> setHeadersMap(Scope scope) {
        String bearerToken = (scope.equals(READ)) ? authenticator.getBearerTokenForReadScope()
                : authenticator.getBearerTokenForWriteScope();

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(AUTHORIZATION, "Bearer " + bearerToken);
        headersMap.put(CONTENT_TYPE, "application/json");

        return headersMap;
    }

    public int getStatusCodeOfResponse() {
        return response.getStatusCode();
    }
}

package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.base.Client;
import com.coherent.training.api.kapitsa.util.plainobjects.Scope;

import java.util.HashMap;
import java.util.Map;

import static com.coherent.training.api.kapitsa.util.plainobjects.Scope.READ;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class BaseClient {
    private static final Authenticator authenticator = Authenticator.getInstance();
    protected Client client;

    public BaseClient(Client client) {
        this.client = client;
    }

    protected Map<String, String> setHeadersMap(Scope scope){
        String bearerToken = (scope.equals(READ)) ? authenticator.getBearerTokenForReadScope(client)
                : authenticator.getBearerTokenForWriteScope(client);

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(AUTHORIZATION, "Bearer " + bearerToken);
        headersMap.put(CONTENT_TYPE, "application/json");

        return headersMap;
    }

    public int getStatusCodeOfResponse(){
        return client.getResponseCode();
    }
}

package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.base.BaseClientObject;
import com.coherent.training.api.kapitsa.providers.Scope;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.HashMap;
import java.util.Map;

import static com.coherent.training.api.kapitsa.clients.Authenticator.getInstance;
import static com.coherent.training.api.kapitsa.providers.Scope.READ;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class BaseClient {
    protected static final Authenticator authenticator = getInstance();
    protected static final BaseClientObject.BaseClientObjectBuilder builder = new BaseClientObject.BaseClientObjectBuilder();
    protected static final String POST = "POST";
    protected static final String GET = "GET";
    protected BaseClientObject baseClient;
    protected CloseableHttpResponse response;

    public BaseClient (CloseableHttpClient client) {
        builder.setClient(client);
    }

    protected Map<String, String> setHeadersMap(Scope scope){
        String bearerToken = (scope.equals(READ)) ? authenticator.getBearerTokenForReadScope(builder)
                : authenticator.getBearerTokenForWriteScope(builder);

        Map<String, String> headersMap = new HashMap<>();

        headersMap.put(AUTHORIZATION, "Bearer " + bearerToken);
        headersMap.put(CONTENT_TYPE, "application/json");

        return headersMap;
    }

    public int getStatusCodeOfResponse(){
        return baseClient.getResponseCode(response);
    }
}

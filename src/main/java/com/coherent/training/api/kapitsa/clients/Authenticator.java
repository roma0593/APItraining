package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.base.BaseClientObject;
import com.coherent.training.api.kapitsa.providers.ConfigFileReader;
import com.coherent.training.api.kapitsa.util.plainobjects.Token;
import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.coherent.training.api.kapitsa.base.BaseClientObject.BaseClientObjectBuilder;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.OAUTH_URL;
import static com.coherent.training.api.kapitsa.util.DataHandler.getTokenObj;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_OK;

public class Authenticator {
    private static final String CLIENT_ID = ConfigFileReader.getInstance().getClientId();
    private static final String CLIENT_SECRET = ConfigFileReader.getInstance().getClientSecret();
    private static final String oauthUrl = OAUTH_URL.getEndpoint();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ZipCode.class.getSimpleName());

    private Authenticator(){
    }

    private static class SingletonAuthenticator{
        private static final Authenticator INSTANCE = new Authenticator();
    }

    public static Authenticator getInstance(){
        return SingletonAuthenticator.INSTANCE;
    }

    public String getBearerTokenForReadScope(BaseClientObjectBuilder builder){
        return getBearerTokenForScope("read", builder);
    }

    public String getBearerTokenForWriteScope(BaseClientObjectBuilder builder){
        return getBearerTokenForScope("write", builder);
    }

    private Map<String, String> setHeadersMapForOauth(){
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(AUTHORIZATION, "Basic " + getEncodedBasicAuthData());
        headersMap.put(CONTENT_TYPE, "application/x-www-form-urlencoded");

        return headersMap;
    }

    @SneakyThrows
    private String getBearerTokenForScope(String scope, BaseClientObjectBuilder builder){
        BaseClientObject baseClient = builder.setAuthorizationRequest(oauthUrl, getAuthForm(scope), setHeadersMapForOauth())
                .build();

        logger.info("Request: {}", baseClient.getPOSTRequest());

        CloseableHttpResponse response = baseClient.executePostRequest();

        String responseBody = baseClient.getResponseBody();

        logger.info("Response: {}", response);

        int responseCode = baseClient.getResponseCode();

        response.close();

        if(responseCode == SC_OK) return getToken(responseBody).getAccessToken();
        else throw new RuntimeException("Access token has not been returned");
    }

    private List<NameValuePair> getAuthForm(String authScope){
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("grant_type", "client_credentials"));
        params.add(new BasicNameValuePair("client_id", CLIENT_ID));
        params.add(new BasicNameValuePair("scope", authScope));

        return params;
    }

    private String getEncodedBasicAuthData(){
        String keys = CLIENT_ID + ":" + CLIENT_SECRET;

        return Base64.getEncoder().encodeToString(keys.getBytes());
    }

    private Token getToken(String responseBody){
        return getTokenObj(responseBody);
    }
}

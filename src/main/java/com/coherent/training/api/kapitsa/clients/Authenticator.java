package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.base.BaseClientObject;
import com.coherent.training.api.kapitsa.providers.ConfigFileReader;
import com.coherent.training.api.kapitsa.util.plainobjects.Token;
import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.coherent.training.api.kapitsa.base.BaseClientObject.BaseClientObjectBuilder;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.OAUTH_URL;
import static com.coherent.training.api.kapitsa.util.DataHandler.getTokenObj;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class Authenticator {
    private static final String CLIENT_ID = ConfigFileReader.getInstance().getClientId();
    private static final String CLIENT_SECRET = ConfigFileReader.getInstance().getClientSecret();
    private static final String oauthUrl = OAUTH_URL.getEndpoint();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ZipCode.class.getSimpleName());
    private static final String POST = "POST";

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

    @SneakyThrows
    private String getBearerTokenForScope(String scope, BaseClientObjectBuilder builder){
        StringEntity entity = new UrlEncodedFormEntity(getAuthForm(scope), "utf-8");

        BaseClientObject baseClient = builder.setRequest(oauthUrl, POST, setHeadersMap(), entity)
                .build();

        logger.info("Request: {}", baseClient.getRequest());

        CloseableHttpResponse response = baseClient.executeRequest();

        String responseBody = baseClient.getResponseBody(response);

        logger.info("Response: {}", response);

        response.close();

        return getToken(responseBody).getAccessToken();
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

    private Map<String, String> setHeadersMap(){
        Map<String, String> headersMap = new HashMap<>();

        headersMap.put(AUTHORIZATION, "Basic " + getEncodedBasicAuthData());
        headersMap.put(CONTENT_TYPE, "application/x-www-form-urlencoded");

        return headersMap;
    }
}

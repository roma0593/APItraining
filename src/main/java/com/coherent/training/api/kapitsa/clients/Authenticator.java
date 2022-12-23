package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.base.BaseClientObject;
import com.coherent.training.api.kapitsa.providers.ConfigFileReader;
import com.coherent.training.api.kapitsa.util.plainobjects.Token;
import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.coherent.training.api.kapitsa.providers.UrlProvider.OAUTH_URL;
import static com.coherent.training.api.kapitsa.util.DataHandler.getTokenObj;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_OK;

public class Authenticator extends BaseClientObject {
    private static final String CLIENT_ID = ConfigFileReader.getInstance().getClientId();
    private static final String CLIENT_SECRET = ConfigFileReader.getInstance().getClientSecret();
    private static final String oauthUrl = OAUTH_URL.getEndpoint();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ZipCode.class.getSimpleName());

    private Authenticator() {}

    private static class SingletonAuthenticator{
        private static final Authenticator INSTANCE = new Authenticator();
    }

    public static Authenticator getInstance(){
        return SingletonAuthenticator.INSTANCE;
    }

    public String getBearerTokenForReadScope(){
        return getBearerTokenForScope("read");
    }

    public String getBearerTokenForWriteScope(){
        return getBearerTokenForScope("write");
    }

    @SneakyThrows
    private String getBearerTokenForScope(String scope){
        setUpAuthorizationRequest(oauthUrl, getAuthForm(scope),
                AUTHORIZATION, "Basic " + getEncodedBasicAuthData(),
                CONTENT_TYPE, "application/x-www-form-urlencoded");

        logger.info("Request: {}", postRequest);

        executePostRequest();

        String responseBody = getResponseBody();

        logger.info("Response: {}", response);

        int responseCode = getResponseCode();

        response.close();

        if(responseCode == SC_OK) return getAccessToken(responseBody);
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

    private String getAccessToken(String responseBody){
        Token tokenObj = getTokenObj(responseBody);

        return tokenObj.getAccessToken();
    }
}

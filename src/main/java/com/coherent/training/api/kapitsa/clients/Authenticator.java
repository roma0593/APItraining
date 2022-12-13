package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.providers.ConfigFileReader;
import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.coherent.training.api.kapitsa.base.BaseTest.client;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.OAUTH_URL;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.logging.Logger.getLogger;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_OK;

public class Authenticator {
    private static final String CLIENT_ID = ConfigFileReader.getInstance().getClientId();
    private static final String CLIENT_SECRET = ConfigFileReader.getInstance().getClientSecret();
    private static final String oauthUrl = OAUTH_URL.getUrl();
    private static final Logger LOGGER = getLogger(Authenticator.class.getName());

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
        HttpPost postRequest = new HttpPost(oauthUrl);

        postRequest.setHeader(AUTHORIZATION, "Basic " + getEncodedBasicAuthData());
        postRequest.setHeader(CONTENT_TYPE, "application/x-www-form-urlencoded");
        postRequest.setEntity(new UrlEncodedFormEntity(getAuthForm(scope), "utf-8"));

        LOGGER.log(Level.INFO, "Request: " + postRequest);

        CloseableHttpResponse response = client.execute(postRequest);

        String responseBody = EntityUtils.toString(response.getEntity(), UTF_8);

        LOGGER.log(Level.INFO, "Response: " + response);

        int statusCode = response.getStatusLine().getStatusCode();

        response.close();

        if(statusCode == SC_OK) return new JSONObject(responseBody).getString("access_token");

        else throw new RuntimeException("Access toke is not returned because status code is not 200");
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
}

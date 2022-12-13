package com.coherent.training.api.kapitsa.base;

import com.coherent.training.api.kapitsa.clients.Authenticator;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import static com.coherent.training.api.kapitsa.base.BaseTest.client;
import static com.coherent.training.api.kapitsa.clients.Authenticator.getInstance;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class BaseClientObject {
    protected HttpGet getRequest;
    protected HttpPost postRequest;
    protected CloseableHttpResponse response;
    private static final Authenticator AUTHENTICATOR = getInstance();

    protected void setupGetRequest(String endpoint){
        String bearerToken = AUTHENTICATOR.getBearerTokenForReadScope();
        getRequest = new HttpGet(endpoint);

        getRequest.setHeader(AUTHORIZATION, "Bearer " + bearerToken);
    }

    protected void setupPostRequest(String endpoint, String bodyValue){
        String bearerToken = AUTHENTICATOR.getBearerTokenForWriteScope();
        postRequest = new HttpPost(endpoint);

        setPostRequestHeaders(AUTHORIZATION, "Bearer " + bearerToken, CONTENT_TYPE, "application/json");
        setRequestBody(bodyValue);
    }

    protected int getResponseCode(){
        return response.getStatusLine().getStatusCode();
    }

    @SneakyThrows
    protected void executeGetRequest(){
        response = client.execute(getRequest);
    }

    @SneakyThrows
    protected void executePostRequest(){
        response = client.execute(postRequest);
    }

    @SneakyThrows
    protected String getResponseBody(){
        return EntityUtils.toString(response.getEntity(), UTF_8);
    }

    @SneakyThrows
    protected String getRequestBody(){
        return EntityUtils.toString(postRequest.getEntity());
    }

    @SneakyThrows
    private void setRequestBody(String json){
        StringEntity entity = new StringEntity(json);

        postRequest.setEntity(entity);
    }

    private void setPostRequestHeaders(String... headerValues){
        int size = headerValues.length;

        for(int i = 0; i < size; i+=2){
            postRequest.setHeader(headerValues[i], headerValues[i+1]);
        }
    }
}

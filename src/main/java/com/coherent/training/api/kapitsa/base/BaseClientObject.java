package com.coherent.training.api.kapitsa.base;

import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.util.List;

import static com.coherent.training.api.kapitsa.base.BaseTest.client;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class BaseClientObject {
    protected HttpGet getRequest;
    protected HttpPost postRequest;
    protected CloseableHttpResponse response;

    protected void setupGetRequest(String endpoint, String token){
        getRequest = new HttpGet(endpoint);

        getRequest.setHeader(AUTHORIZATION, "Bearer " + token);
    }

    protected void setupPostRequest(String endpoint, String bodyValue, String token){
        postRequest = new HttpPost(endpoint);

        setPostRequestHeaders(AUTHORIZATION, "Bearer " + token, CONTENT_TYPE, "application/json");
        setRequestBody(bodyValue);
    }

    protected void setUpAuthorizationRequest(String endpoint, List<NameValuePair> nameValuePairs, String... headers){
        postRequest = new HttpPost(endpoint);

        setPostRequestHeaders(headers);
        setEncodedRequestBody(nameValuePairs);
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

    @SneakyThrows
    private void setEncodedRequestBody(List<NameValuePair> nameValuePairs){
        postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
    }

    private void setPostRequestHeaders(String... headerValues){
        int size = headerValues.length;

        for(int i = 0; i < size; i+=2){
            postRequest.setHeader(headerValues[i], headerValues[i+1]);
        }
    }
}

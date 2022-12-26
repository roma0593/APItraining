package com.coherent.training.api.kapitsa.base;

import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class BaseClientObject {
    private final CloseableHttpClient client;
    private final HttpGet getRequest;
    private final HttpPost postRequest;
    private CloseableHttpResponse response;

    public BaseClientObject(BaseClientObjectBuilder builder) {
        this.client = builder.client;
        this.getRequest = builder.getRequest;
        this.postRequest = builder.postRequest;
    }

    public CloseableHttpClient getClient(){
        return client;
    }

    public int getResponseCode(){
        return response.getStatusLine().getStatusCode();
    }

    public HttpGet getGETRequest(){
        return getRequest;
    }

    public HttpPost getPOSTRequest(){
        return postRequest;
    }

    @SneakyThrows
    public CloseableHttpResponse executeGetRequest(){
        response = client.execute(getRequest);

        return response;
    }

    @SneakyThrows
    public CloseableHttpResponse executePostRequest(){
        response = client.execute(postRequest);

        return response;
    }

    @SneakyThrows
    public String getResponseBody(){
        return EntityUtils.toString(response.getEntity(), UTF_8);
    }

    @SneakyThrows
    public String getRequestBody(){
        return EntityUtils.toString(postRequest.getEntity());
    }

    public static class BaseClientObjectBuilder{
        private CloseableHttpClient client;
        private HttpGet getRequest;
        private HttpPost postRequest;

        public BaseClientObjectBuilder() {
        }

        public BaseClientObjectBuilder setClient(CloseableHttpClient client){
            this.client = client;

            return this;
        }

        public BaseClientObjectBuilder setGetRequest(String endpoint, String token){
            getRequest = new HttpGet(endpoint);

            getRequest.setHeader(AUTHORIZATION, "Bearer " + token);

            return this;
        }

        public BaseClientObjectBuilder setPostRequest(String endpoint, String bodyValue, String token){
            postRequest = new HttpPost(endpoint);
            Map<String, String> headersMap = new HashMap<>();

            headersMap.put(AUTHORIZATION, "Bearer " + token);
            headersMap.put(CONTENT_TYPE, "application/json");

            setPostRequestHeaders(headersMap);
            setRequestBody(bodyValue);

            return this;
        }

        public BaseClientObjectBuilder setAuthorizationRequest(String endpoint, List<NameValuePair> nameValuePairs, Map<String, String> headersMap){
            postRequest = new HttpPost(endpoint);

            setPostRequestHeaders(headersMap);
            setEncodedRequestBody(nameValuePairs);

            return this;
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

        private void setPostRequestHeaders(Map<String, String> headersMap){
            for(String key : headersMap.keySet()){
                postRequest.setHeader(key, headersMap.get(key));
            }
        }

        public BaseClientObject build(){
            return new BaseClientObject(this);
        }
    }
}

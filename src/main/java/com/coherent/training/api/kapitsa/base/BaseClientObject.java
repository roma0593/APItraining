package com.coherent.training.api.kapitsa.base;

import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

public class BaseClientObject {
    private final CloseableHttpClient client;
    private final HttpUriRequest GETRequest;
    private final HttpUriRequest POSTRequest;
    private CloseableHttpResponse response;
    private final RequestBuilder requestBuilder;

    public BaseClientObject(BaseClientObjectBuilder builder) {
        this.client = builder.client;
        this.GETRequest = builder.GETRequest;
        this.POSTRequest = builder.POSTRequest;
        this.requestBuilder = builder.requestBuilder;
    }

    public CloseableHttpClient getClient(){
        return client;
    }

    public int getResponseCode(){
        return response.getStatusLine().getStatusCode();
    }

    public HttpUriRequest getGETRequest(){
        return GETRequest;
    }

    public HttpUriRequest getPOSTRequest(){
        return POSTRequest;
    }

    @SneakyThrows
    public CloseableHttpResponse executeGetRequest(){
        response = client.execute(GETRequest);

        return response;
    }

    @SneakyThrows
    public CloseableHttpResponse executePostRequest(){
        response = client.execute(POSTRequest);

        return response;
    }

    @SneakyThrows
    public <T extends HttpResponse> String getResponseBody(T httpData){
        return EntityUtils.toString(httpData.getEntity(), UTF_8);
    }

    @SneakyThrows
    public String getRequestBody(){
        HttpEntity entity = requestBuilder.getEntity();

        return EntityUtils.toString(entity, UTF_8);
    }

    public static class BaseClientObjectBuilder{
        private CloseableHttpClient client;
        private HttpUriRequest GETRequest;
        private HttpUriRequest POSTRequest;
        private RequestBuilder requestBuilder;

        public BaseClientObjectBuilder() {
        }

        public BaseClientObjectBuilder setClient(CloseableHttpClient client){
            this.client = client;

            return this;
        }

        @SneakyThrows
        public BaseClientObjectBuilder setGetRequest(String endpoint, String token, String... entity){
            requestBuilder = RequestBuilder.get(endpoint)
                    .setHeader(AUTHORIZATION, "Bearer " + token);

            if(entity.length > 0) requestBuilder.setEntity(new StringEntity(entity[0]))
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            GETRequest = requestBuilder.build();

            return this;
        }

        public BaseClientObjectBuilder setPostRequest(String endpoint, Map<String, String> headersMap, StringEntity entity){
            requestBuilder = RequestBuilder.post(endpoint)
                    .setEntity(entity);

            setHeaders(headersMap);

            POSTRequest = requestBuilder.build();

            return this;
        }

        private void setHeaders(Map<String, String> headersMap){
            for(String key : headersMap.keySet()){
                requestBuilder.setHeader(key, headersMap.get(key));
            }
        }

        public BaseClientObject build(){
            return new BaseClientObject(this);
        }
    }
}

package com.coherent.training.api.kapitsa.base;

import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class BaseClientObject {
    private final CloseableHttpClient client;
    private final HttpUriRequest request;
    private CloseableHttpResponse response;
    private final RequestBuilder requestBuilder;

    public BaseClientObject(BaseClientObjectBuilder builder) {
        this.client = builder.client;
        this.request = builder.request;
        this.requestBuilder = builder.requestBuilder;
    }

    public CloseableHttpClient getClient(){
        return client;
    }

    public int getResponseCode(){
        return response.getStatusLine().getStatusCode();
    }

    public HttpUriRequest getRequest(){
        return request;
    }

    @SneakyThrows
    public CloseableHttpResponse executeRequest(){
        response = client.execute(request);

        return response;
    }

    @SneakyThrows
    public <T extends HttpResponse> String getResponseBody(T httpData){
        String responseCode = String.valueOf(getResponseCode());

        if (responseCode.startsWith("2")) return EntityUtils.toString(httpData.getEntity(), UTF_8);
        else throw new RuntimeException("Response body is not returned due to invalid status code");
    }

    @SneakyThrows
    public String getRequestBody(){
        HttpEntity entity = requestBuilder.getEntity();

        return EntityUtils.toString(entity, UTF_8);
    }

    public static class BaseClientObjectBuilder{
        private CloseableHttpClient client;
        private HttpUriRequest request;
        private RequestBuilder requestBuilder;

        public BaseClientObjectBuilder() {
        }

        public BaseClientObjectBuilder setClient(CloseableHttpClient client){
            this.client = client;

            return this;
        }

        public BaseClientObjectBuilder setRequest(String endpoint, String method, Map<String, String> headersMap, HttpEntity... entity){
            requestBuilder = RequestBuilder.create(method)
                    .setUri(endpoint);

            setHeaders(headersMap);

            if(entity.length > 0) requestBuilder.setEntity(entity[0]);

            request = requestBuilder.build();

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

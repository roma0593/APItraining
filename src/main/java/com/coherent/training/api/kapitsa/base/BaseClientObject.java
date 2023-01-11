package com.coherent.training.api.kapitsa.base;

import com.coherent.training.api.kapitsa.util.DataHandler;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class BaseClientObject {
    private final DataHandler handler = new DataHandler();
    private final CloseableHttpClient client;
    private final HttpUriRequest request;
    private CloseableHttpResponse response;
    private final RequestBuilder requestBuilder;

    public BaseClientObject(BaseClientObjectBuilder builder) {
        this.client = builder.client;
        this.request = builder.request;
        this.requestBuilder = builder.requestBuilder;
    }

    public CloseableHttpClient getClient() {
        return client;
    }

    public int getResponseCode() {
        return response.getStatusLine().getStatusCode();
    }

    public HttpUriRequest getRequest() {
        return request;
    }

    public CloseableHttpResponse getResponse(){
        return response;
    }

    @SneakyThrows
    public void executeRequest() {
        response = client.execute(request);
    }

    @SneakyThrows
    public <G> G getResponseBody(Class<G> gClass) {
        String responseAsString = EntityUtils.toString(response.getEntity(), UTF_8);

        return handler.getObject(responseAsString, gClass);
    }

    @SneakyThrows
    public <T> T getRequestBody(Class<T> tClass) {
        HttpEntity entity = requestBuilder.getEntity();

        String entityString = EntityUtils.toString(entity, UTF_8);

        return handler.getObject(entityString, tClass);
    }

    @SneakyThrows
    public void closeResponse(){
        response.close();
    }

    public static class BaseClientObjectBuilder {
        private CloseableHttpClient client;
        private HttpUriRequest request;
        private RequestBuilder requestBuilder;

        public BaseClientObjectBuilder() {
        }

        public BaseClientObjectBuilder setClient(CloseableHttpClient client) {
            this.client = client;

            return this;
        }

        public BaseClientObjectBuilder setRequest(String endpoint, String method, Map<String, String> headersMap, HttpEntity... entity) {
            requestBuilder = RequestBuilder.create(method)
                    .setUri(endpoint);

            setHeaders(headersMap);

            if (entity.length > 0) requestBuilder.setEntity(entity[0]);

            request = requestBuilder.build();

            return this;
        }

        private void setHeaders(Map<String, String> headersMap) {
            for (String key : headersMap.keySet()) {
                requestBuilder.setHeader(key, headersMap.get(key));
            }
        }

        public BaseClientObject build() {
            return new BaseClientObject(this);
        }
    }
}

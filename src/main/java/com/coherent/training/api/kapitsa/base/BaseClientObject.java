package com.coherent.training.api.kapitsa.base;

import com.coherent.training.api.kapitsa.util.DataHandler;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.Map;

import static com.coherent.training.api.kapitsa.util.interceptors.ResponseInterceptor.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class BaseClientObject {
    private final DataHandler handler = new DataHandler();
    private final CloseableHttpClient client;
    private HttpUriRequest request;
    private RequestBuilder requestBuilder;
    private static final String POST = "POST";
    private static final String GET = "GET";

    public BaseClientObject(CloseableHttpClient client) {
        this.client = client;
    }

    public CloseableHttpClient getClient() {
        return client;
    }

    public int getResponseCode(CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    public HttpUriRequest getRequest() {
        return request;
    }

    @SneakyThrows
    public <T extends Object> T getSuccessResponseBody(Class<T> gClass, CloseableHttpResponse response) {
        String responseCode = String.valueOf(getResponseCode(response));
        String responseAsString = getEntity();

        if (responseCode.startsWith("2")) return handler.getObject(responseAsString, gClass);
        else throw new RuntimeException("Response code " + responseCode + " is not success");
    }

    public HttpEntity getBadResponseBody(CloseableHttpResponse response) {
        return response.getEntity();
    }

    @SneakyThrows
    public <T> T getRequestBody(Class<T> tClass) {
        HttpEntity entity = requestBuilder.getEntity();

        String entityString = EntityUtils.toString(entity, UTF_8);

        return handler.getObject(entityString, tClass);
    }

    @SneakyThrows
    public void closeResponse(CloseableHttpResponse response) {
        response.close();
    }

    private HttpUriRequest setRequest(String endpoint, String method, Map<String, String> headersMap, HttpEntity... entity) {
        requestBuilder = RequestBuilder.create(method)
                .setUri(endpoint);

        setHeaders(headersMap);

        if (entity.length > 0) requestBuilder.setEntity(entity[0]);

        return requestBuilder.build();
    }

    private HttpUriRequest setRequestWithParams(String endpoint, String method, Map<String, String> headersMap, HttpEntity entity, NameValuePair nvp) {
        requestBuilder = RequestBuilder.create(method)
                .setUri(endpoint)
                .addParameter(nvp)
                .setEntity(entity);

        setHeaders(headersMap);

        return requestBuilder.build();
    }

    private void setHeaders(Map<String, String> headersMap) {
        for (String key : headersMap.keySet()) {
            requestBuilder.setHeader(key, headersMap.get(key));
        }
    }

    @SneakyThrows
    public CloseableHttpResponse get(String url, Map<String, String> headers){
        request = setRequest(url, GET, headers);

        return client.execute(request);
    }

    @SneakyThrows
    public CloseableHttpResponse get(String url, Map<String, String> headers, HttpEntity entity){
        request = setRequest(url, GET, headers, entity);

        return client.execute(request);
    }

    @SneakyThrows
    public <T extends StringEntity> CloseableHttpResponse post(String url, Map<String, String> headers, T body){
        request = setRequest(url, POST, headers, body);

        return client.execute(request);
    }

    @SneakyThrows
    public <T> CloseableHttpResponse post(String url, Map<String, String> headers, T bodyClass){
        String json = handler.convertToJson(bodyClass);
        HttpEntity entity = new StringEntity(json);

        request = setRequest(url, POST, headers, entity);

        return client.execute(request);
    }
}

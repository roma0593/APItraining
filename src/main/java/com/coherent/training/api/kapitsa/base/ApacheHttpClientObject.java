package com.coherent.training.api.kapitsa.base;

import com.coherent.training.api.kapitsa.util.DataHandler;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.Map;

import static com.coherent.training.api.kapitsa.util.apache_interceptors.ResponseInterceptor.getEntity;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ApacheHttpClientObject {
    private final DataHandler handler = new DataHandler();
    private final CloseableHttpClient client;
    private HttpUriRequest request;
    private RequestBuilder requestBuilder;
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String PUT = "PUT";
    private static final String PATCH = "PATCH";
    private static final String DELETE = "DELETE";

    public ApacheHttpClientObject(CloseableHttpClient client) {
        this.client = client;
    }

    public CloseableHttpClient getClient() {
        return client;
    }

    public int getResponseCode(CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    @SneakyThrows
    public <T> T getResponseBody(Class<T> gClass, CloseableHttpResponse response) {
        String responseAsString = getEntity();

        return handler.getObject(responseAsString, gClass);
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


    private HttpUriRequest setRequestWithParams(String endpoint, String method, Map<String, String> headersMap, Map<String, String> nvp, HttpEntity... entity) {
        requestBuilder = RequestBuilder.create(method)
                .setUri(endpoint);

        for (String name : nvp.keySet()) {
            requestBuilder.addParameter(name, nvp.get(name));
        }

        if (entity.length > 0) {
            requestBuilder.setEntity(entity[0]);
        }

        setHeaders(headersMap);

        return requestBuilder.build();
    }

    private void setHeaders(Map<String, String> headersMap) {
        for (String key : headersMap.keySet()) {
            requestBuilder.setHeader(key, headersMap.get(key));
        }
    }

    @SneakyThrows
    public CloseableHttpResponse get(String url, Map<String, String> headers, Map<String, String> param) {
        request = setRequestWithParams(url, GET, headers, param);

        return client.execute(request);
    }

    @SneakyThrows
    public CloseableHttpResponse get(String url, Map<String, String> headers) {
        request = setRequest(url, GET, headers);

        return client.execute(request);
    }

    @SneakyThrows
    public CloseableHttpResponse get(String url, Map<String, String> headers, HttpEntity entity) {
        request = setRequest(url, GET, headers, entity);

        return client.execute(request);
    }

    @SneakyThrows
    public <T extends StringEntity> CloseableHttpResponse post(String url, Map<String, String> headers, T body) {
        request = setRequest(url, POST, headers, body);

        return client.execute(request);
    }

    @SneakyThrows
    public <T> CloseableHttpResponse post(String url, Map<String, String> headers, T bodyClass) {
        request = setRequest(url, POST, headers, toHttpEntity(bodyClass));

        return client.execute(request);
    }

    @SneakyThrows
    public CloseableHttpResponse post(String url, Map<String, String> headers, File file) {
        request = setRequest(url, POST, headers, getMultipartEntity(file));

        return client.execute(request);
    }

    @SneakyThrows
    public <T> CloseableHttpResponse put(String url, Map<String, String> headers, T bodyClass) {
        request = setRequest(url, PUT, headers, toHttpEntity(bodyClass));

        return client.execute(request);
    }

    @SneakyThrows
    public <T> CloseableHttpResponse patch(String url, Map<String, String> headers, T bodyClass) {
        request = setRequest(url, PATCH, headers, toHttpEntity(bodyClass));

        return client.execute(request);
    }

    @SneakyThrows
    public <T> CloseableHttpResponse delete(String url, Map<String, String> headers, T bodyClass) {
        request = setRequest(url, DELETE, headers, toHttpEntity(bodyClass));

        return client.execute(request);
    }

    @SneakyThrows
    private <T> HttpEntity toHttpEntity(T bodyClass) {
        return new StringEntity(handler.convertToJson(bodyClass));
    }

    private HttpEntity getMultipartEntity(File file) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        return builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName())
                .build();
    }
}

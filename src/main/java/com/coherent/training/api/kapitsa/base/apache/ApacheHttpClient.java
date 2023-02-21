package com.coherent.training.api.kapitsa.base.apache;

import com.coherent.training.api.kapitsa.base.Client;
import com.coherent.training.api.kapitsa.base.ResponseWrapper;
import com.coherent.training.api.kapitsa.util.DataHandler;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.coherent.training.api.kapitsa.util.apache_interceptors.ResponseInterceptor.getEntity;

public class ApacheHttpClient implements Client {
    private final DataHandler handler = new DataHandler();
    private final CloseableHttpClient client;
    private HttpUriRequest request;
    private RequestBuilder requestBuilder;
    private CloseableHttpResponse apacheResponse;
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String PUT = "PUT";
    private static final String PATCH = "PATCH";
    private static final String DELETE = "DELETE";

    public ApacheHttpClient(CloseableHttpClient client) {
        this.client = client;
    }

    @SneakyThrows
    public ResponseWrapper get(String url, Map<String, String> headers, Map<String, String> param) {
        request = setRequestWithParams(url, GET, headers, param);
        apacheResponse = client.execute(request);

        return new ResponseWrapper(apacheResponse);
    }

    @SneakyThrows
    @Override
    public ResponseWrapper get(String url, Map<String, String> headers) {
        request = setRequest(url, GET, headers);
        apacheResponse = client.execute(request);

        return new ResponseWrapper(apacheResponse);
    }

    @SneakyThrows
    @Override
    public ResponseWrapper post(String url, Map<String, String> headers, Map<String, String> formMap) {
        request = setRequest(url, POST, headers, convertToFormEntity(formMap));
        apacheResponse = client.execute(request);

        return new ResponseWrapper(apacheResponse);
    }

    @SneakyThrows
    @Override
    public <T> ResponseWrapper post(String url, Map<String, String> headerMap, T bodyClass) {
        request = setRequest(url, POST, headerMap, toHttpEntity(bodyClass));
        apacheResponse = client.execute(request);

        return new ResponseWrapper(apacheResponse);
    }

    @SneakyThrows
    @Override
    public ResponseWrapper post(String url, Map<String, String> headers, File file) {
        request = setRequest(url, POST, headers, getMultipartEntity(file));
        apacheResponse = client.execute(request);

        return new ResponseWrapper(apacheResponse);
    }

    @SneakyThrows
    @Override
    public <T> ResponseWrapper put(String url, Map<String, String> headers, T bodyClass) {
        request = setRequest(url, PUT, headers, toHttpEntity(bodyClass));
        apacheResponse = client.execute(request);

        return new ResponseWrapper(apacheResponse);
    }

    @SneakyThrows
    @Override
    public <T> ResponseWrapper patch(String url, Map<String, String> headers, T bodyClass) {
        request = setRequest(url, PATCH, headers, toHttpEntity(bodyClass));
        apacheResponse = client.execute(request);

        return new ResponseWrapper(apacheResponse);
    }

    @SneakyThrows
    @Override
    public <T> ResponseWrapper delete(String url, Map<String, String> headers, T bodyClass) {
        request = setRequest(url, DELETE, headers, toHttpEntity(bodyClass));
        apacheResponse = client.execute(request);

        return new ResponseWrapper(apacheResponse);
    }

    public CloseableHttpClient getClient() {
        return client;
    }

    @Override
    public int getResponseCode() {
        return apacheResponse.getStatusLine().getStatusCode();
    }

    @SneakyThrows
    @Override
    public <T> T getResponseBody(Class<T> gClass) {
        String responseAsString = getEntity();

        return handler.getObject(responseAsString, gClass);
    }

    @Override
    public String getResponseBodyAsString() {
        return getEntity();
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
    private <T> HttpEntity toHttpEntity(T bodyClass) {
        return new StringEntity(handler.convertToJson(bodyClass));
    }

    private HttpEntity getMultipartEntity(File file) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        return builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName())
                .build();
    }

    @SneakyThrows
    private StringEntity convertToFormEntity(Map<String, String> formMap) {
        List<NameValuePair> params = new ArrayList<>();

        for (String key : formMap.keySet()) {
            params.add(new BasicNameValuePair(key, formMap.get(key)));
        }

        return new UrlEncodedFormEntity(params, "utf-8");
    }
}

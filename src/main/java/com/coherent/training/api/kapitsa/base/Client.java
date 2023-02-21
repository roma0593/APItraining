package com.coherent.training.api.kapitsa.base;

import java.io.File;
import java.util.Map;

public interface Client {
    ResponseWrapper get(String url, Map<String, String> headerMap);
    ResponseWrapper get(String url, Map<String, String> headerMap, Map<String, String> param);
    ResponseWrapper post(String url, Map<String, String> headerMap, Map<String, String> formMap);
    <T> ResponseWrapper post(String url, Map<String, String> headerMap, T bodyClass);
    ResponseWrapper post(String url, Map<String, String> headerMap, File file);
    <T> ResponseWrapper patch(String url, Map<String, String> headerMap, T bodyClass);
    <T> ResponseWrapper put(String url, Map<String, String> headerMap, T bodyClass);
    <T> ResponseWrapper delete(String url, Map<String, String> headerMap, T bodyClass);
    <T> T getResponseBody(Class<T> bodyClass);
    String getResponseBodyAsString();
    int getResponseCode();
}

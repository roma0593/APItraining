package com.coherent.training.api.kapitsa.base.rest_assured;

import com.coherent.training.api.kapitsa.base.Client;
import com.coherent.training.api.kapitsa.base.ResponseWrapper;
import io.restassured.response.Response;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestAssuredClient implements Client {
    private Response restAssuredResponse;

    @Override
    public ResponseWrapper get(String url, Map<String, String> headerMap) {
        restAssuredResponse = given().headers(headerMap)
                .when().get(url);

        return new ResponseWrapper(restAssuredResponse);
    }

    @Override
    public ResponseWrapper get(String url, Map<String, String> headerMap, Map<String, String> param) {
        restAssuredResponse = given().headers(headerMap)
                .queryParams(param)
                .when().get(url);

        return new ResponseWrapper(restAssuredResponse);
    }

    @Override
    public ResponseWrapper post(String url, Map<String, String> headerMap, Map<String, String> formMap) {
        restAssuredResponse = given().headers(headerMap)
                .formParams(formMap)
                .when().post(url);

        return new ResponseWrapper(restAssuredResponse);
    }

    @Override
    public <T> ResponseWrapper post(String url, Map<String, String> headerMap, T bodyClass) {
        restAssuredResponse = given().headers(headerMap)
                .body(bodyClass)
                .when().post(url);

        return new ResponseWrapper(restAssuredResponse);
    }

    @Override
    public ResponseWrapper post(String url, Map<String, String> headerMap, File file) {
        restAssuredResponse = given().headers(headerMap)
                .multiPart(file)
                .when().post(url);

        return new ResponseWrapper(restAssuredResponse);
    }

    @Override
    public <T> ResponseWrapper patch(String url, Map<String, String> headerMap, T bodyClass) {
        restAssuredResponse = given().headers(headerMap)
                .body(bodyClass)
                .when().patch(url);

        return new ResponseWrapper(restAssuredResponse);
    }

    @Override
    public <T> ResponseWrapper put(String url, Map<String, String> headerMap, T bodyClass) {
        restAssuredResponse = given().headers(headerMap)
                .body(bodyClass)
                .when().patch(url);

        return new ResponseWrapper(restAssuredResponse);
    }

    @Override
    public <T> ResponseWrapper delete(String url, Map<String, String> headerMap, T bodyClass) {
        restAssuredResponse = given().headers(headerMap)
                .body(bodyClass)
                .when().delete(url);

        return new ResponseWrapper(restAssuredResponse);
    }

    @Override
    public <T> T getResponseBody(Class<T> bodyClass) {
        return restAssuredResponse.body().as(bodyClass);
    }

    @Override
    public String getResponseBodyAsString() {
        return restAssuredResponse.body().asString();
    }

    @Override
    public int getResponseCode() {
        return restAssuredResponse.getStatusCode();
    }
}

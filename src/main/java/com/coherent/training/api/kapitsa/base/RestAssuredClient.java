package com.coherent.training.api.kapitsa.base;

import io.restassured.response.Response;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestAssuredClient {
    public Response get(String url, Map<String, String> headerMap){
        return given().headers(headerMap)
                .when().get(url);
    }

    public Response get(String url, Map<String, String> headerMap, Map<String, String> param){
        return given().headers(headerMap)
                .queryParams(param)
                .when().get(url);
    }

    public Response post(String url, Map<String, String> headerMap, Map<String, String> formMap){
        return given().headers(headerMap)
                .formParams(formMap)
                .when().post(url);
    }

    public <T> Response post(String url, Map<String, String> headerMap, T bodyClass){
        return given().headers(headerMap)
                .body(bodyClass)
                .when().post(url);
    }

    public Response post(String url, Map<String, String> headerMap, File file){
        return given().headers(headerMap)
                .multiPart(file)
                .when().post(url);
    }

    public <T> Response patch(String url, Map<String, String> headerMap, T bodyClass){
        return given().headers(headerMap)
                .body(bodyClass)
                .when().patch(url);
    }

    public <T> Response delete(String url, Map<String, String> headerMap, T bodyClass){
        return given().headers(headerMap)
                .body(bodyClass)
                .when().delete(url);
    }

    public <T> T getResponseBody(Response response, Class<T> bodyClass){
        return response.body().as(bodyClass);
    }
}

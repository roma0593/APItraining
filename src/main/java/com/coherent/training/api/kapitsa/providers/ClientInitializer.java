package com.coherent.training.api.kapitsa.providers;

import com.coherent.training.api.kapitsa.base.Client;
import com.coherent.training.api.kapitsa.base.apache.ApacheHttpClient;
import com.coherent.training.api.kapitsa.base.rest_assured.RestAssuredClient;
import com.coherent.training.api.kapitsa.util.apache_interceptors.RequestInterceptor;
import com.coherent.training.api.kapitsa.util.apache_interceptors.ResponseInterceptor;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ClientInitializer {
    private static CloseableHttpClient closeableHttpClient;

    public static Client initClient() {
        String clientPath = ConfigFileReader.getInstance().getClientPath();

        if (clientPath.equals(ApacheHttpClient.class.getPackageName())) {
            closeableHttpClient = HttpClients.custom()
                    .addInterceptorFirst(new RequestInterceptor())
                    .addInterceptorLast(new ResponseInterceptor()).build();
            return new ApacheHttpClient(closeableHttpClient);
        } else if (clientPath.equals(RestAssuredClient.class.getPackageName())) {
            RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
            return new RestAssuredClient();
        } else throw new RuntimeException("Specified client can not be initialed");
    }

    public static CloseableHttpClient getCloseableHttpClient() {
        return closeableHttpClient;
    }
}

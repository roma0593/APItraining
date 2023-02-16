package com.coherent.training.api.kapitsa.apache_client_tests;

import com.coherent.training.api.kapitsa.util.apache_interceptors.RequestInterceptor;
import com.coherent.training.api.kapitsa.util.apache_interceptors.ResponseInterceptor;
import lombok.SneakyThrows;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseTest {
    protected int responseCode;
    protected CloseableHttpClient client;

    @BeforeTest
    public void setUp(){
        client = HttpClients.custom()
                .addInterceptorFirst(new RequestInterceptor())
                .addInterceptorLast(new ResponseInterceptor()).build();
    }

    @SneakyThrows
    @AfterTest
    public void tearDown(){
        client.close();
    }
}

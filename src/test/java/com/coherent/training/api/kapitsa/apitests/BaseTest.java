package com.coherent.training.api.kapitsa.apitests;

import com.coherent.training.api.kapitsa.util.interceptors.RequestInterceptor;
import com.coherent.training.api.kapitsa.util.interceptors.ResponseInterceptor;
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

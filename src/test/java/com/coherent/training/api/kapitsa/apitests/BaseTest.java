package com.coherent.training.api.kapitsa.apitests;

import lombok.SneakyThrows;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseTest {
    protected int responseCode;
    protected CloseableHttpClient client;

    @BeforeTest
    public void setUp(){
        int timeout = 2;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();;
    }

    @SneakyThrows
    @AfterTest
    public void tearDown(){
        client.close();
    }
}

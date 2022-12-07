package com.coherent.training.api.kapitsa.base;

import lombok.SneakyThrows;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseTest {
    public static CloseableHttpClient client;

    @BeforeTest
    public void setUp(){
        client = HttpClients.createDefault();
    }

    @SneakyThrows
    @AfterTest
    public void tearDown(){
        client.close();
    }
}

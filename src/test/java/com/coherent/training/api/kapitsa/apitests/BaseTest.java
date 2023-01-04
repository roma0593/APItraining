package com.coherent.training.api.kapitsa.apitests;

import com.coherent.training.api.kapitsa.clients.ZipCode;
import lombok.SneakyThrows;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseTest {
    protected CloseableHttpClient client;
    protected ZipCode zipCodeClient;
    protected String[] zipCodesArray;

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

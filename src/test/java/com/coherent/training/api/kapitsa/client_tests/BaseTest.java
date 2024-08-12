package com.coherent.training.api.kapitsa.client_tests;

import com.coherent.training.api.kapitsa.base.Client;
import com.coherent.training.api.kapitsa.clients.Users;
import com.coherent.training.api.kapitsa.clients.ZipCode;
import lombok.SneakyThrows;
import org.apache.http.impl.client.CloseableHttpClient;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.IOException;

import static com.coherent.training.api.kapitsa.providers.ClientInitializer.getCloseableHttpClient;
import static com.coherent.training.api.kapitsa.providers.ClientInitializer.initClient;

public class BaseTest {
    protected Client client;
    protected int responseCode;
    protected ZipCode zipCodeClient;
    protected Users usersClient;

    @SneakyThrows
    @BeforeClass
    public void setUp() {
        client = initClient();
        zipCodeClient = new ZipCode(client);
        usersClient = new Users(client);
    }

    @SneakyThrows
    @AfterClass
    public void tearDown() throws IOException {
        CloseableHttpClient closeableClient = getCloseableHttpClient();

        if (closeableClient != null) closeableClient.close();
        System.out.println("Hello my friend!");
    }
}

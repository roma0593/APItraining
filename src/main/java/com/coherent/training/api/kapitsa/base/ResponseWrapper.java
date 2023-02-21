package com.coherent.training.api.kapitsa.base;

import io.restassured.response.Response;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

public class ResponseWrapper implements AutoCloseable {
    private Response restAssuredResponse;
    private CloseableHttpResponse apacheResponse;

    public ResponseWrapper(Response restAssuredResponse) {
        this.restAssuredResponse = restAssuredResponse;
    }

    public ResponseWrapper(CloseableHttpResponse apacheResponse) {
        this.apacheResponse = apacheResponse;
    }

    public Response getRestAssuredResponse() {
        return restAssuredResponse;
    }

    public CloseableHttpResponse getApacheResponse() {
        return apacheResponse;
    }

    @Override
    public void close() throws IOException {
        if(apacheResponse != null) apacheResponse.close();
    }
}

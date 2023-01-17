package com.coherent.training.api.kapitsa.util.interceptors;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RequestInterceptor implements HttpRequestInterceptor {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ResponseInterceptor.class.getSimpleName());

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        logger.info("Request: {}", request);
    }
}

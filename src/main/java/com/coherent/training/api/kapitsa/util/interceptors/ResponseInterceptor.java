package com.coherent.training.api.kapitsa.util.interceptors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ResponseInterceptor implements HttpResponseInterceptor {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ResponseInterceptor.class.getSimpleName());
    private static String stringEntity;

    public static String getEntity() {
        return stringEntity;
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        HttpEntity entity = response.getEntity();

        stringEntity = (entity != null) ? EntityUtils.toString(entity, UTF_8) : "{}";

        logger.info("Response: {}", response.getStatusLine() + "\n"
                + stringEntity);
    }
}

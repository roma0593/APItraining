package com.coherent.training.api.kapitsa.util.interceptors;

import org.apache.http.*;
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
    public void process(HttpResponse response, HttpContext context) throws IOException {
        HttpEntity entity = response.getEntity();
        stringEntity = (entity != null) ? EntityUtils.toString(entity, UTF_8) : "{}";

        logger.info("Response: {}", response.getStatusLine());

        for(Header header : response.getAllHeaders()){
            logger.info("{}: {}", header.getName(), header.getValue());
        }

        logger.info("{}", stringEntity);
    }
}

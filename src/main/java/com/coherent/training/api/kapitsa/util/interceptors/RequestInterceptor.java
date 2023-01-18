package com.coherent.training.api.kapitsa.util.interceptors;

import lombok.SneakyThrows;
import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RequestInterceptor implements HttpRequestInterceptor {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RequestInterceptor.class.getSimpleName());

    @SneakyThrows
    @Override
    public void process(HttpRequest request, HttpContext context) {
        HttpEntity entity = (request instanceof HttpEntityEnclosingRequest) ?
                ((HttpEntityEnclosingRequest) request).getEntity() : null;

        String stringEntity = (entity != null) ? EntityUtils.toString(entity, UTF_8) : "{}";

        logger.info("Request: {}", request.getRequestLine());

        for (Header header : request.getAllHeaders()) {
            logger.info("{}: {}", header.getName(), header.getValue());
        }

        logger.info("{}", stringEntity);
    }
}

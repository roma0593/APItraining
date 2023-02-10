package com.coherent.training.api.kapitsa.util.interceptors;

import io.qameta.allure.Attachment;
import lombok.SneakyThrows;
import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RequestInterceptor implements HttpRequestInterceptor {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RequestInterceptor.class.getSimpleName());
    private String stringEntity;
    private RequestLine requestLine;
    private Header[] headers;

    @SneakyThrows
    @Override
    public void process(HttpRequest request, HttpContext context) {
        requestLine = request.getRequestLine();
        headers = request.getAllHeaders();

        HttpEntity entity = (request instanceof HttpEntityEnclosingRequest) ?
                ((HttpEntityEnclosingRequest) request).getEntity() : null;

        stringEntity = (entity != null) ? EntityUtils.toString(entity, UTF_8) : "[]";

        writeRequestToReport();
        logRequest();
    }

    @Attachment(value = "Request")
    private String writeRequestToReport() {
        StringBuilder requestHeaders = new StringBuilder();

        for (Header header : headers) {
            requestHeaders.append(header.getName() + ": " + header.getValue() + "\n");
        }

        return requestLine + "\n"
                + requestHeaders + "\n"
                + stringEntity;
    }

    private void logRequest() {
        logger.info("Request: {}", requestLine);

        for (Header header : headers) {
            logger.info("{}: {}", header.getName(), header.getValue());
        }

        logger.info("{}", stringEntity);
    }
}

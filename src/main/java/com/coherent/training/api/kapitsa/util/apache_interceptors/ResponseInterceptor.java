package com.coherent.training.api.kapitsa.util.apache_interceptors;

import io.qameta.allure.Attachment;
import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ResponseInterceptor implements HttpResponseInterceptor {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ResponseInterceptor.class.getSimpleName());
    private static String stringEntity;
    private StatusLine responseLine;
    private Header[] headers;

    public static String getEntity() {
        return stringEntity;
    }


    @Override
    public void process(HttpResponse response, HttpContext context) throws IOException {
        responseLine = response.getStatusLine();
        headers = response.getAllHeaders();

        HttpEntity entity = response.getEntity();
        stringEntity = (entity != null) ? EntityUtils.toString(entity, UTF_8) : "[]";

        writeResponseToReport();
        logResponse();
    }

    @Attachment(value = "Response")
    private String writeResponseToReport() {
        StringBuilder requestHeaders = new StringBuilder();

        for (Header header : headers) {
            requestHeaders.append(header.getName() + ": " + header.getValue() + "\n");
        }

        return responseLine + "\n"
                + requestHeaders + "\n"
                + stringEntity;
    }

    private void logResponse() {
        logger.info("Response: {}", responseLine);

        for (Header header : headers) {
            logger.info("{}: {}", header.getName(), header.getValue());
        }

        logger.info("{}", stringEntity);
    }
}

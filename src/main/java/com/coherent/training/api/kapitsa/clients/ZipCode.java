package com.coherent.training.api.kapitsa.clients;

import lombok.SneakyThrows;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static com.coherent.training.api.kapitsa.providers.UrlProvider.EXPAND_ZIP_CODES;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.GET_ZIP_CODES;

public class ZipCode extends BaseClient {
    private static final String GET_ZIP_CODES_URL = GET_ZIP_CODES.getEndpoint();
    private static final String EXPAND_ZIP_CODES_URL = EXPAND_ZIP_CODES.getEndpoint();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ZipCode.class.getSimpleName());

    public ZipCode(CloseableHttpClient client) {
        super(client);
    }

    @SneakyThrows
    public List<String> getAllZipCodes() {
        baseClient = builder.setRequest(GET_ZIP_CODES_URL, GET, setHeadersMap("read")).build();

        logger.info("Request: {}", baseClient.getRequest());

        baseClient.executeRequest();

        List<String> responseBody = getListFromResponse();

        baseClient.closeResponse();

        logger.info("Response: {}", baseClient.getResponse() + "\n" + responseBody);

        return responseBody;
    }

    @SneakyThrows
    public List<String> addNewZipCodes(String... zipCodes) {
        StringEntity entity = new StringEntity(Arrays.toString(zipCodes));

        baseClient = builder.setRequest(EXPAND_ZIP_CODES_URL, POST, setHeadersMap("write"), entity)
                .build();

        logger.info("Request: {}", baseClient.getRequest() + "\n" + baseClient.getRequestBody(List.class));

        baseClient.executeRequest();

        List<String> listOfCodes = getListFromResponse();

        baseClient.closeResponse();

        logger.info("Response: {}", baseClient.getResponse() + "\n" + listOfCodes);

        return listOfCodes;
    }

    private List<String> getListFromResponse() {
        return baseClient.getResponseBody(List.class);
    }

    public boolean zipCodesAreSaved(List<String> responseList, String... zipCodes) {
        for (String zipCode : zipCodes) {
            if (!responseList.contains(zipCode))
                return false;
            break;
        }

        return true;
    }

    private boolean isZipCodeUnique(List<String> responseList, String zipCode) {
        for (String element : responseList) {
            if (!element.equals(zipCode))
                return false;
            break;
        }

        return true;
    }

    public boolean isZipCodesUnique(List<String> responseList, String... zipCodes) {
        boolean isUnique = true;

        for (String zipCode : zipCodes) {
            isUnique = isZipCodeUnique(responseList, zipCode);
        }

        return isUnique;
    }
}

package com.coherent.training.api.kapitsa.apache_http_clients;

import lombok.SneakyThrows;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.List;

import static com.coherent.training.api.kapitsa.providers.UrlProvider.EXPAND_ZIP_CODES;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.GET_ZIP_CODES;
import static com.coherent.training.api.kapitsa.util.plainobjects.Scope.READ;
import static com.coherent.training.api.kapitsa.util.plainobjects.Scope.WRITE;

public class ZipCode extends BaseClient {
    private static final String GET_ZIP_CODES_URL = GET_ZIP_CODES.getEndpoint();
    private static final String EXPAND_ZIP_CODES_URL = EXPAND_ZIP_CODES.getEndpoint();

    public ZipCode(CloseableHttpClient client) {
        super(client);
    }

    @SneakyThrows
    public List<String> getAllZipCodes() {
        try {
            response = baseClient.get(GET_ZIP_CODES_URL, setHeadersMap(READ));

            return getListFromResponse();
        } finally {
            baseClient.closeResponse(response);
        }
    }

    @SneakyThrows
    public List<String> addNewZipCodes(String... zipCodes) {
        try {
            response = baseClient.post(EXPAND_ZIP_CODES_URL, setHeadersMap(WRITE), zipCodes);

            return getListFromResponse();
        } finally {
            baseClient.closeResponse(response);
        }
    }

    private List<String> getListFromResponse() {
        return baseClient.getResponseBody(List.class, response);
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

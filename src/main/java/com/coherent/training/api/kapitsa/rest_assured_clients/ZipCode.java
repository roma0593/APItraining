package com.coherent.training.api.kapitsa.rest_assured_clients;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.coherent.training.api.kapitsa.providers.UrlProvider.EXPAND_ZIP_CODES;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.GET_ZIP_CODES;
import static com.coherent.training.api.kapitsa.util.plainobjects.Scope.READ;
import static com.coherent.training.api.kapitsa.util.plainobjects.Scope.WRITE;

public class ZipCode extends BaseClient{
    private static final String GET_ZIP_CODES_URL = GET_ZIP_CODES.getEndpoint();
    private static final String EXPAND_ZIP_CODES_URL = EXPAND_ZIP_CODES.getEndpoint();

    public List<String> getAllZipCodes() {
        response = client.get(GET_ZIP_CODES_URL, setHeadersMap(READ));

        String[] zipCodeArray = client.getResponseBody(response, String[].class);

        return Arrays.asList(zipCodeArray);
    }

    public List<String> addNewZipCodes(String... zipCodes) {
        response = client.post(EXPAND_ZIP_CODES_URL, setHeadersMap(WRITE), zipCodes);

        String[] zipCodeArray = client.getResponseBody(response, String[].class);

        return Arrays.asList(zipCodeArray);
    }

    public boolean zipCodesAreSaved(List<String> responseList, String... zipCodes) {
        boolean isZipCodeSaved = true;

        for (String zipCode : zipCodes) {
            if (!responseList.contains(zipCode)) {
                isZipCodeSaved = false;
                break;
            }
        }

        return isZipCodeSaved;
    }

    public boolean isZipCodesUnique(List<String> responseList, String... zipCodes) {
        boolean isDuplicated = true;

        List<String> duplicatedValues = responseList.stream()
                .filter(e -> Collections.frequency(responseList, e) > 1)
                .distinct()
                .collect(Collectors.toList());

        for (String zipCode : zipCodes) {
            if(duplicatedValues.contains(zipCode)) {
                isDuplicated = false;
                break;
            }
        }

        return isDuplicated;
    }
}

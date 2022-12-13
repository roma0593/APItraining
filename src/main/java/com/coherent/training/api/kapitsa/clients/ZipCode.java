package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.base.BaseClientObject;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.coherent.training.api.kapitsa.providers.UrlProvider.EXPAND_ZIP_CODES;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.GET_ZIP_CODES;
import static java.util.logging.Logger.getLogger;
import static org.apache.commons.lang3.ArrayUtils.contains;

public class ZipCode extends BaseClientObject {
    private static final String GET_ZIP_CODES_URL = GET_ZIP_CODES.getUrl();
    private static final String EXPAND_ZIP_CODES_URL = EXPAND_ZIP_CODES.getUrl();
    private static final Logger logger = getLogger(Authenticator.class.getName());

    @SneakyThrows
    public String getAllZipCodes(){
        setupGetRequest(GET_ZIP_CODES_URL);

        logger.log(Level.INFO, "Request: " + getRequest);

        executeGetRequest();

        String responseCode = String.valueOf(getResponseCode());

        String responseBody = getResponseBody();

        logger.log(Level.INFO, "Response: " + response + "\n" + responseBody);

        if (responseCode.startsWith("2")) return responseBody;

        else throw new RuntimeException("Zip codes are not returned because response code is not in 2xx");
    }

    public String addNewZipCodes(String... zipCodes){
        setupPostRequest(EXPAND_ZIP_CODES_URL, Arrays.toString(zipCodes));

        logger.log(Level.INFO, "Request: " + postRequest + "\n" + getRequestBody());

        executePostRequest();

        String responseBody = getResponseBody();

        String responseCode = String.valueOf(getResponseCode());

        logger.log(Level.INFO, "Response: " + response + "\n" + responseBody);

        if (responseCode.startsWith("2")) return responseBody;

        else throw new RuntimeException("Zip codes are not returned because response code is not in 2xx");
    }

    public int getStatusCodeOfResponse(){
        assert response != null;

        return getResponseCode();
    }

    public boolean zipCodesAreSaved(String[] responseArray, String... zipCodes){
        for(String zipCode : zipCodes){
            if(!contains(responseArray, zipCode))
                return false;
                break;
        }

        return true;
    }

    private boolean isZipCodeUnique(String[] responseArray, String zipCode){
        for(String arrayElement : responseArray){
            if (!arrayElement.equals(zipCode))
                return false;
                break;
        }

        return true;
    }

    public boolean isZipCodesUnique(String[] responseArray, String ... zipCodes){
        boolean isUnique = true;

        for(String zipCode : zipCodes){
            isUnique = isZipCodeUnique(responseArray, zipCode);
        }

        return isUnique;
    }

    @SneakyThrows
    public void closeResponse(){
        response.close();
    }
}

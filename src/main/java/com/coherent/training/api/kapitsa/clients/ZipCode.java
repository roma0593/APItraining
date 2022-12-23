package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.base.BaseClientObject;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.coherent.training.api.kapitsa.clients.Authenticator.getInstance;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.EXPAND_ZIP_CODES;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.GET_ZIP_CODES;
import static org.apache.commons.lang3.ArrayUtils.contains;

public class ZipCode extends BaseClientObject {
    private static final String GET_ZIP_CODES_URL = GET_ZIP_CODES.getEndpoint();
    private static final String EXPAND_ZIP_CODES_URL = EXPAND_ZIP_CODES.getEndpoint();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ZipCode.class.getSimpleName());
    private static final Authenticator authenticator = getInstance();

    @SneakyThrows
    public String getAllZipCodes(){
        setupGetRequest(GET_ZIP_CODES_URL, authenticator.getBearerTokenForReadScope());

        logger.info("Request: {}", getRequest);

        executeGetRequest();

        String responseBody = getResponseBody();

        logger.info("Response: {}", response + "\n" + responseBody);

        return responseBody;
    }

    public String addNewZipCodes(String... zipCodes){
        setupPostRequest(EXPAND_ZIP_CODES_URL, Arrays.toString(zipCodes), authenticator.getBearerTokenForWriteScope());

        logger.info("Request: {}", postRequest + "\n" + getRequestBody());

        executePostRequest();

        String responseBody = getResponseBody();

        logger.info("Response: {}", response + "\n" + responseBody);

        return responseBody;
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

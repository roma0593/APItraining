package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.base.BaseClientObject;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.coherent.training.api.kapitsa.base.BaseClientObject.*;
import static com.coherent.training.api.kapitsa.clients.Authenticator.getInstance;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.EXPAND_ZIP_CODES;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.GET_ZIP_CODES;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.ArrayUtils.contains;

public class ZipCode {
    private static final String GET_ZIP_CODES_URL = GET_ZIP_CODES.getEndpoint();
    private static final String EXPAND_ZIP_CODES_URL = EXPAND_ZIP_CODES.getEndpoint();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ZipCode.class.getSimpleName());
    private static final Authenticator authenticator = getInstance();
    private static final BaseClientObjectBuilder builder = new BaseClientObjectBuilder();
    private BaseClientObject baseClient;
    private CloseableHttpResponse response;

    public ZipCode(CloseableHttpClient client) {
        builder.setClient(client);
    }

    @SneakyThrows
    public String getAllZipCodes(){
        String bearerToken = authenticator.getBearerTokenForReadScope(builder);

        baseClient = builder.setGetRequest(GET_ZIP_CODES_URL, bearerToken).build();

        logger.info("Request: {}", baseClient.getGETRequest());

        response = baseClient.executeGetRequest();

        String responseBody = baseClient.getResponseBody();

        logger.info("Response: {}", response + "\n" + responseBody);

        return responseBody;
    }

    public String addNewZipCodes(String... zipCodes){
        String bearerToken = authenticator.getBearerTokenForWriteScope(builder);

        baseClient = builder.setPostRequest(EXPAND_ZIP_CODES_URL, Arrays.toString(zipCodes), bearerToken)
                .build();

        logger.info("Request: {}", baseClient.getPOSTRequest() + "\n" + baseClient.getRequestBody());

        response = baseClient.executePostRequest();

        String responseBody = baseClient.getResponseBody();

        logger.info("Response: {}", response + "\n" + responseBody);

        return responseBody;
    }

    public String[] getArrayFromResponse(String response){
        Gson gson = new Gson();

        String responseCode = valueOf(getStatusCodeOfResponse());

        if (responseCode.startsWith("2")) return gson.fromJson(response, String[].class);
        else throw new RuntimeException("Zip codes are not returned because response code is not in 2xx");
    }

    public int getStatusCodeOfResponse(){
        assert response != null;

        return baseClient.getResponseCode();
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

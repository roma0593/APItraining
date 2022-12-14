package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.base.BaseClientObject;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.coherent.training.api.kapitsa.base.BaseClientObject.BaseClientObjectBuilder;
import static com.coherent.training.api.kapitsa.clients.Authenticator.getInstance;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.EXPAND_ZIP_CODES;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.GET_ZIP_CODES;
import static com.coherent.training.api.kapitsa.util.DataHandler.getListOfObj;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class ZipCode {
    private static final String GET_ZIP_CODES_URL = GET_ZIP_CODES.getEndpoint();
    private static final String EXPAND_ZIP_CODES_URL = EXPAND_ZIP_CODES.getEndpoint();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ZipCode.class.getSimpleName());
    private static final Authenticator authenticator = getInstance();
    private static final BaseClientObjectBuilder builder = new BaseClientObjectBuilder();
    private static final String POST = "POST";
    private static final String GET = "GET";
    private BaseClientObject baseClient;
    private CloseableHttpResponse response;

    public ZipCode(CloseableHttpClient client) {
        builder.setClient(client);
    }

    @SneakyThrows
    public List<String> getAllZipCodes(){
        baseClient = builder.setRequest(GET_ZIP_CODES_URL, GET, setHeadersMap("read")).build();

        logger.info("Request: {}", baseClient.getRequest());

        response = baseClient.executeRequest();

        List<String> responseBody = getListFromResponse(response);

        logger.info("Response: {}", response + "\n" + responseBody);

        return responseBody;
    }

    @SneakyThrows
    public List<String> addNewZipCodes(String... zipCodes){
        StringEntity entity = new StringEntity(Arrays.toString(zipCodes));

        baseClient = builder.setRequest(EXPAND_ZIP_CODES_URL, POST, setHeadersMap("write"), entity)
                .build();

        logger.info("Request: {}", baseClient.getRequest() + "\n" + baseClient.getRequestBody());

        response = baseClient.executeRequest();

        List<String> listOfCodes = getListFromResponse(response);

        logger.info("Response: {}", response + "\n" + listOfCodes);

        return listOfCodes;
    }

    private List<String> getListFromResponse(CloseableHttpResponse response){
        String responseBody = baseClient.getResponseBody(response);

        return getListOfObj(responseBody);
    }

    public int getStatusCodeOfResponse(){
        assert response != null;

        return baseClient.getResponseCode();
    }

    public boolean zipCodesAreSaved(List<String> responseList, String... zipCodes){
        for(String zipCode : zipCodes){
            if(!responseList.contains(zipCode))
                return false;
                break;
        }

        return true;
    }

    private boolean isZipCodeUnique(List<String> responseList, String zipCode){
        for(String element : responseList){
            if (!element.equals(zipCode))
                return false;
                break;
        }

        return true;
    }

    public boolean isZipCodesUnique(List<String> responseList, String ... zipCodes){
        boolean isUnique = true;

        for(String zipCode : zipCodes){
            isUnique = isZipCodeUnique(responseList, zipCode);
        }

        return isUnique;
    }

    private Map<String, String> setHeadersMap(String scope){
        String bearerToken = (scope.equals("read")) ? authenticator.getBearerTokenForReadScope(builder)
                : authenticator.getBearerTokenForWriteScope(builder);

        Map<String, String> headersMap = new HashMap<>();

        headersMap.put(AUTHORIZATION, "Bearer " + bearerToken);
        headersMap.put(CONTENT_TYPE, "application/json");

        return headersMap;
    }

    @SneakyThrows
    public void closeResponse(){
        response.close();
    }
}

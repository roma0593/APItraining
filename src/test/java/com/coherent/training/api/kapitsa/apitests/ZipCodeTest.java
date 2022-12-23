package com.coherent.training.api.kapitsa.apitests;

import com.coherent.training.api.kapitsa.base.BaseTest;
import com.coherent.training.api.kapitsa.clients.ZipCode;
import com.coherent.training.api.kapitsa.utils.JsonParser;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static java.lang.String.valueOf;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ZipCodeTest extends BaseTest {
    @Test
    public void getAllZipCodesTest(){
        zipCodeClient = new ZipCode();

        String zipCodes = zipCodeClient.getAllZipCodes();

        int responseCode = zipCodeClient.getStatusCodeOfResponse();

        if (valueOf(responseCode).startsWith("2")) zipCodesArray = new JsonParser().getArray(zipCodes);
        else throw new RuntimeException("Zip codes are not returned because response code is not in 2xx");

        zipCodeClient.closeResponse();

        assertTrue(zipCodesArray.length > 0, "There are no saved zip codes");

        assertEquals(responseCode, SC_OK, "Expected and actual response code mismatch");
    }

    @Parameters({"zipCode1", "zipCode2", "zipCode3"})
    @Test
    public void addZipCodesTest(String zipCode1, String zipCode2, String zipCode3){
        zipCodeClient = new ZipCode();

        String returnedZipCodes = zipCodeClient.addNewZipCodes(zipCode1, zipCode2, zipCode3);

        int responseCode = zipCodeClient.getStatusCodeOfResponse();

        if (valueOf(responseCode).startsWith("2")) zipCodesArray = new JsonParser().getArray(returnedZipCodes);
        else throw new RuntimeException("Zip codes are not returned because response code is not in 2xx");

        boolean areSaved = zipCodeClient.zipCodesAreSaved(zipCodesArray, zipCode1, zipCode2, zipCode3);

        zipCodeClient.closeResponse();

        assertTrue(areSaved, "Not all zip codes are saved");
        assertEquals(responseCode, SC_CREATED, "Expected and actual response code mismatch");
    }

    @Parameters({"zipCode2", "zipCode4"})
    @Test
    public void addDuplicatedZipCodesTest(String zipCode2, String zipCode4){
        zipCodeClient = new ZipCode();

        String returnedZipCodes = zipCodeClient.addNewZipCodes(zipCode4, zipCode2, zipCode2);

        int responseCode = zipCodeClient.getStatusCodeOfResponse();

        if (valueOf(responseCode).startsWith("2")) zipCodesArray = new JsonParser().getArray(returnedZipCodes);
        else throw new RuntimeException("Zip codes are not returned because response code is not in 2xx");

        boolean areSaved = zipCodeClient.zipCodesAreSaved(zipCodesArray, zipCode4, zipCode2, zipCode2);
        boolean areUnique = zipCodeClient.isZipCodesUnique(zipCodesArray, zipCode4, zipCode2, zipCode2);

        zipCodeClient.closeResponse();

        assertTrue(areSaved, "Not all zip codes are saved");
        assertTrue(areUnique, "Duplicated zip codes present in response");
        assertEquals(responseCode, SC_CREATED, "Expected and actual response code mismatch");
    }

    @Parameters({"zipCode1", "zipCode2"})
    @Test
    public void addZipCodesAsAlreadySaved(String zipCode1, String zipCode5){
        zipCodeClient = new ZipCode();

        String returnedZipCodes = zipCodeClient.addNewZipCodes(zipCode1, zipCode5);

        int responseCode = zipCodeClient.getStatusCodeOfResponse();

        if (valueOf(responseCode).startsWith("2")) zipCodesArray = new JsonParser().getArray(returnedZipCodes);
        else throw new RuntimeException("Zip codes are not returned because response code is not in 2xx");

        boolean areSaved = zipCodeClient.zipCodesAreSaved(zipCodesArray, zipCode1, zipCode5);
        boolean areUnique = zipCodeClient.isZipCodesUnique(zipCodesArray, zipCode1, zipCode5);

        assertTrue(areSaved, "Not all zip codes are saved");
        assertTrue(areUnique, "Duplicated zip codes present in response");
        assertEquals(responseCode, SC_CREATED, "Expected and actual response code mismatch");
    }
}

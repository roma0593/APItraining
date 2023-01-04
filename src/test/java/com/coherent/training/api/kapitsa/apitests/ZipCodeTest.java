package com.coherent.training.api.kapitsa.apitests;

import com.coherent.training.api.kapitsa.clients.ZipCode;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ZipCodeTest extends BaseTest {
    @Test
    public void getAllZipCodesTest(){
        zipCodeClient = new ZipCode(client);

        List<String> zipCodes = zipCodeClient.getAllZipCodes();

        int responseCode = zipCodeClient.getStatusCodeOfResponse();

        zipCodeClient.closeResponse();

        assertTrue(zipCodes.size() > 0, "There are no saved zip codes");

        assertEquals(responseCode, SC_OK, "Expected and actual response code mismatch");
    }

    @Parameters({"zipCode1", "zipCode2", "zipCode3"})
    @Test
    public void addZipCodesTest(String zipCode1, String zipCode2, String zipCode3){
        zipCodeClient = new ZipCode(client);

        List<String> returnedZipCodes = zipCodeClient.addNewZipCodes(zipCode1, zipCode2, zipCode3);

        int responseCode = zipCodeClient.getStatusCodeOfResponse();

        boolean areSaved = zipCodeClient.zipCodesAreSaved(returnedZipCodes, zipCode1, zipCode2, zipCode3);

        zipCodeClient.closeResponse();

        assertTrue(areSaved, "Not all zip codes are saved");
        assertEquals(responseCode, SC_CREATED, "Expected and actual response code mismatch");
    }

    @Parameters({"zipCode2", "zipCode4"})
    @Test
    public void addDuplicatedZipCodesTest(String zipCode2, String zipCode4){
        zipCodeClient = new ZipCode(client);

        List<String> zipCodesList = zipCodeClient.addNewZipCodes(zipCode4, zipCode2, zipCode2);

        int responseCode = zipCodeClient.getStatusCodeOfResponse();

        boolean areSaved = zipCodeClient.zipCodesAreSaved(zipCodesList, zipCode4, zipCode2, zipCode2);
        boolean areUnique = zipCodeClient.isZipCodesUnique(zipCodesList, zipCode4, zipCode2, zipCode2);

        zipCodeClient.closeResponse();

        assertTrue(areSaved, "Not all zip codes are saved");
        assertTrue(areUnique, "Duplicated zip codes present in response");
        assertEquals(responseCode, SC_CREATED, "Expected and actual response code mismatch");
    }

    @Parameters({"zipCode1", "zipCode2"})
    @Test
    public void addZipCodesAsAlreadySaved(String zipCode1, String zipCode5){
        zipCodeClient = new ZipCode(client);

        List<String> returnedZipCodes = zipCodeClient.addNewZipCodes(zipCode1, zipCode5);

        int responseCode = zipCodeClient.getStatusCodeOfResponse();

        boolean areSaved = zipCodeClient.zipCodesAreSaved(returnedZipCodes, zipCode1, zipCode5);
        boolean areUnique = zipCodeClient.isZipCodesUnique(returnedZipCodes, zipCode1, zipCode5);

        assertTrue(areSaved, "Not all zip codes are saved");
        assertTrue(areUnique, "Duplicated zip codes present in response");
        assertEquals(responseCode, SC_CREATED, "Expected and actual response code mismatch");
    }
}

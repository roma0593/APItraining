package com.coherent.training.api.kapitsa.client_tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ZipCodeTest extends BaseTest {

    @SneakyThrows
    @Severity(SeverityLevel.NORMAL)
    @Description("Getting all zipcodes")
    @Step("Make GET request to /zip-codes API")
    @Test
    public void getAllZipCodesTest() {
        List<String> zipCodes = zipCodeClient.getAllZipCodes();
        responseCode = zipCodeClient.getStatusCodeOfResponse();

        assertTrue(zipCodes.size() > 0, "There are no saved zip codes");
        assertEquals(responseCode, SC_OK, "Expected and actual response code mismatch");
    }

    @SneakyThrows
    @Severity(SeverityLevel.CRITICAL)
    @Description("Adding zipcodes")
    @Step("Make POST request to /zip-codes/expand with body containing list of zipcodes")
    @Parameters({"zipCode1", "zipCode2", "zipCode3"})
    @Test
    public void addZipCodesTest(String zipCode1, String zipCode2, String zipCode3) {
        List<String> returnedZipCodes = zipCodeClient.addNewZipCodes(zipCode1, zipCode2, zipCode3);
        responseCode = zipCodeClient.getStatusCodeOfResponse();
        boolean areSaved = zipCodeClient.zipCodesAreSaved(returnedZipCodes, zipCode1, zipCode2, zipCode3);

        assertTrue(areSaved, "Not all zip codes are saved");
        assertEquals(responseCode, SC_CREATED, "Expected and actual response code mismatch");
    }

    @SneakyThrows
    @Severity(SeverityLevel.CRITICAL)
    @Description("Adding duplicated zipcodes")
    @Step("Make POST request to /zip-codes/expand with body containing duplicated zipcodes")
    @Parameters({"zipCode2", "zipCode4"})
    @Test
    public void addDuplicatedZipCodesTest(String zipCode2, String zipCode4) {
        List<String> zipCodesList = zipCodeClient.addNewZipCodes(zipCode4, zipCode2, zipCode2);
        responseCode = zipCodeClient.getStatusCodeOfResponse();
        boolean areSaved = zipCodeClient.zipCodesAreSaved(zipCodesList, zipCode4, zipCode2, zipCode2);
        boolean areUnique = zipCodeClient.isZipCodesUnique(zipCodesList, zipCode4, zipCode2, zipCode2);

        assertTrue(areSaved, "Not all zip codes are saved");
        assertTrue(areUnique, "Duplicated zip codes present in response");
        assertEquals(responseCode, SC_CREATED, "Expected and actual response code mismatch");
    }

    @SneakyThrows
    @Severity(SeverityLevel.CRITICAL)
    @Description("Adding zipcodes with already existing codes")
    @Step("Make POST request to /zip-codes/expand with body containing list of already existing zipcodes")
    @Parameters({"zipCode1", "zipCode2"})
    @Test
    public void addZipCodesAsAlreadySaved(String zipCode1, String zipCode5) {
        List<String> returnedZipCodes = zipCodeClient.addNewZipCodes(zipCode1, zipCode5);
        responseCode = zipCodeClient.getStatusCodeOfResponse();
        boolean areSaved = zipCodeClient.zipCodesAreSaved(returnedZipCodes, zipCode1, zipCode5);
        boolean areUnique = zipCodeClient.isZipCodesUnique(returnedZipCodes, zipCode1, zipCode5);

        assertTrue(areSaved, "Not all zip codes are saved");
        assertTrue(areUnique, "Duplicated zip codes present in response");
        assertEquals(responseCode, SC_CREATED, "Expected and actual response code mismatch");
    }
}

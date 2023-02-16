package com.coherent.training.api.kapitsa.rest_assured_client_tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
    protected int responseCode;

    @BeforeSuite
    public void applyFilters(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
    }
}

package com.coherent.training.api.kapitsa.apitests;

import com.coherent.training.api.kapitsa.clients.Users;
import com.coherent.training.api.kapitsa.clients.ZipCode;
import com.coherent.training.api.kapitsa.util.plainobjects.User;
import com.coherent.training.api.kapitsa.utils.DataUtilization;
import org.testng.annotations.Test;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.*;

public class UsersTest extends BaseTest {
    @Test(dataProviderClass = DataUtilization.class, dataProvider = "allFieldsUserProvider")
    public void addUserWithAllFields(String usersJson) {
        usersClient = new Users(client);

        User user = usersClient.addUser(usersJson);

        responseCode = usersClient.getStatusCodeOfResponse();

        boolean isUserAdded = usersClient.isUserAdded(user);

        zipCodeClient = new ZipCode(client);

        List<String> availableZipCodes = zipCodeClient.getAllZipCodes();

        boolean isZipCodeAvailable = zipCodeClient.zipCodesAreSaved(availableZipCodes, user.getZipCode());

        assertEquals(responseCode, SC_CREATED, "Expected and actual response code mismatch");
        assertTrue(isUserAdded, "User was not added");
        assertFalse(isZipCodeAvailable, "Zip code is available");
    }

    @Test(dataProviderClass = DataUtilization.class, dataProvider = "invalidNameSexUserProvider")
    public void addUserWithExistingNameSexPair(String usersJson) {
        usersClient = new Users(client);

        User user = usersClient.addUser(usersJson);

        responseCode = usersClient.getStatusCodeOfResponse();

        boolean isUserAdded = usersClient.isUserAdded(user);

        assertEquals(responseCode, SC_BAD_REQUEST, "Expected and actual response code mismatch");
        assertFalse(isUserAdded, "User was added");
    }

    @Test(dataProviderClass = DataUtilization.class, dataProvider = "requiredFieldsUserProvider")
    public void addUserWithRequiredFields(String usersJson) {
        usersClient = new Users(client);

        User user = usersClient.addUser(usersJson);

        responseCode = usersClient.getStatusCodeOfResponse();

        boolean isUserAdded = usersClient.isUserAdded(user);

        assertEquals(responseCode, SC_CREATED, "Expected and actual response code mismatch");
        assertTrue(isUserAdded, "User was not added");
    }

    @Test(dataProviderClass = DataUtilization.class, dataProvider = "invalidZipcodeUserProvider")
    public void addUserWithUnavailableZipCode(String usersJson) {
        usersClient = new Users(client);

        User user = usersClient.addUser(usersJson);

        responseCode = usersClient.getStatusCodeOfResponse();

        boolean isUserAdded = usersClient.isUserAdded(user);

        assertEquals(responseCode, SC_FAILED_DEPENDENCY, "Expected and actual response code mismatch");
        assertFalse(isUserAdded, "User was added");
    }
}

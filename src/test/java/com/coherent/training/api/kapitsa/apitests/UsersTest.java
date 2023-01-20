package com.coherent.training.api.kapitsa.apitests;

import com.coherent.training.api.kapitsa.clients.Users;
import com.coherent.training.api.kapitsa.clients.ZipCode;
import com.coherent.training.api.kapitsa.util.plainobjects.User;
import com.coherent.training.api.kapitsa.utils.DataUtilization;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.*;

public class UsersTest extends BaseTest {
    private Users usersClient;
    private ZipCode zipCodeClient;

    @Test(dataProviderClass = DataUtilization.class, dataProvider = "allFieldsUserProvider")
    public void addUserWithAllFields(String age, String name, String sex, String zipCode) {
        User userToAdd = new User(parseInt(age), name, sex, zipCode);

        usersClient = new Users(client);
        User receivedUser = usersClient.addUser(userToAdd);

        responseCode = usersClient.getStatusCodeOfResponse();

        boolean isUserAdded = usersClient.isUserAdded(receivedUser);

        zipCodeClient = new ZipCode(client);
        List<String> availableZipCodes = zipCodeClient.getAllZipCodes();

        boolean isZipCodeAvailable = zipCodeClient.zipCodesAreSaved(availableZipCodes, receivedUser.getZipCode());

        assertEquals(responseCode, SC_CREATED, "Expected and actual response code mismatch");
        assertTrue(isUserAdded, "User was not added");
        assertFalse(isZipCodeAvailable, "Zip code is available");
    }

    @Test(dataProviderClass = DataUtilization.class, dataProvider = "invalidNameSexUserProvider")
    public void addUserWithExistingNameSexPair(String age, String name, String sex, String zipCode) {
        User userToAdd = new User(parseInt(age), name, sex, zipCode);

        usersClient = new Users(client);
        User receivedUser = usersClient.addUser(userToAdd);

        responseCode = usersClient.getStatusCodeOfResponse();

        boolean isUserAdded = usersClient.isUserAdded(receivedUser);

        assertEquals(responseCode, SC_BAD_REQUEST, "Expected and actual response code mismatch");
        assertFalse(isUserAdded, "User was added");
    }

    @Test(dataProviderClass = DataUtilization.class, dataProvider = "requiredFieldsUserProvider")
    public void addUserWithRequiredFields(String name, String sex) {
        User userToAdd = new User(name, sex);

        usersClient = new Users(client);
        User receivedUser = usersClient.addUser(userToAdd);

        responseCode = usersClient.getStatusCodeOfResponse();

        boolean isUserAdded = usersClient.isUserAdded(receivedUser);

        assertEquals(responseCode, SC_CREATED, "Expected and actual response code mismatch");
        assertTrue(isUserAdded, "User was not added");
    }

    @Test(dataProviderClass = DataUtilization.class, dataProvider = "invalidZipcodeUserProvider")
    public void addUserWithUnavailableZipCode(String age, String name, String sex, String zipCode) {
        User userToAdd = new User(parseInt(age), name, sex, zipCode);

        usersClient = new Users(client);
        User receivedUser = usersClient.addUser(userToAdd);

        responseCode = usersClient.getStatusCodeOfResponse();

        boolean isUserAdded = usersClient.isUserAdded(receivedUser);

        assertEquals(responseCode, SC_FAILED_DEPENDENCY, "Expected and actual response code mismatch");
        assertFalse(isUserAdded, "User was added");
    }

    @Test
    public void getAllUsers(){
        usersClient = new Users(client);
        List<User> users = usersClient.getAllUsers();

        responseCode = usersClient.getStatusCodeOfResponse();

        assertEquals(responseCode, SC_OK, "Expected and actual response code mismatch");
        assertTrue(users.size() > 0, "Users are not retrieved");
    }

    @Parameters({"age"})
    @Test
    public void getUsersOlderThan(int age){
        Map<String, String> nameValueMap = new HashMap<>();
        nameValueMap.put("olderThan", String.valueOf(age));

        usersClient = new Users(client);
        List<User> users = usersClient.getAllUsersWithParam(nameValueMap);

        responseCode = usersClient.getStatusCodeOfResponse();

        boolean areUsersOlder = usersClient.areUsers(users, String.valueOf(age), "olderThan");

        assertEquals(responseCode, SC_OK, "Expected and actual response code mismatch");
        assertTrue(areUsersOlder, "Not all users are older than " + age);
    }

    @Parameters({"age"})
    @Test
    public void getUsersYoungerThan(int age){
        Map<String, String> nameValueMap = new HashMap<>();
        nameValueMap.put("youngerThan", String.valueOf(age));

        usersClient = new Users(client);
        List<User> users = usersClient.getAllUsersWithParam(nameValueMap);

        responseCode = usersClient.getStatusCodeOfResponse();

        boolean areUsersYounger = usersClient.areUsers(users, String.valueOf(age), "youngerThan");

        assertEquals(responseCode, SC_OK, "Expected and actual response code mismatch");
        assertTrue(areUsersYounger, "Not all users are younger than " + age);
    }

    @Parameters({"sex"})
    @Test
    public void getUsersWithSex(String sex){
        Map<String, String> nameValueMap = new HashMap<>();
        nameValueMap.put("sex", sex.toUpperCase());

        usersClient = new Users(client);
        List<User> users = usersClient.getAllUsersWithParam(nameValueMap);

        responseCode = usersClient.getStatusCodeOfResponse();

        boolean areUsersWithSex = usersClient.areUsers(users, sex);

        assertEquals(responseCode, SC_OK, "Expected and actual response code mismatch");
        assertTrue(areUsersWithSex, "Not all users are " + sex);
    }
}

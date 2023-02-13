package com.coherent.training.api.kapitsa.apitests;

import com.coherent.training.api.kapitsa.clients.Users;
import com.coherent.training.api.kapitsa.clients.ZipCode;
import com.coherent.training.api.kapitsa.util.DataHandler;
import com.coherent.training.api.kapitsa.util.plainobjects.User;
import com.coherent.training.api.kapitsa.utils.DataUtilization;
import io.qameta.allure.*;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.coherent.training.api.kapitsa.util.plainobjects.Conditions.OLDER_THAN;
import static com.coherent.training.api.kapitsa.util.plainobjects.Conditions.YOUNGER_THAN;
import static java.lang.Integer.parseInt;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.*;

public class UsersTest extends BaseTest {
    private Users usersClient;
    private ZipCode zipCodeClient;
    private final DataHandler dataHandler = new DataHandler();

    @Severity(SeverityLevel.CRITICAL)
    @Description("Adding user with all fields")
    @Step("Make POST request to /users API with body containing all user's fields")
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

    @Severity(SeverityLevel.CRITICAL)
    @Description("Adding user with invalid name/sex pair fields")
    @Step("Make POST request to /users API with body containing invalid name/sex pair")
    @Link(name = "US-1")
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

    @Severity(SeverityLevel.CRITICAL)
    @Description("Adding user with required fields")
    @Step("Make POST request to /users API with body containing required user's fields")
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

    @Severity(SeverityLevel.CRITICAL)
    @Description("Adding user with unavailable zipcodes")
    @Step("Make POST request to /users API with body containing user with unavailable zipcodes")
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

    @Severity(SeverityLevel.NORMAL)
    @Description("Getting all users")
    @Step("Make GET request to /users API")
    @Test
    public void getAllUsers() {
        usersClient = new Users(client);
        List<User> users = usersClient.getAllUsers();
        responseCode = usersClient.getStatusCodeOfResponse();

        assertEquals(responseCode, SC_OK, "Expected and actual response code mismatch");
        assertTrue(users.size() > 0, "Users are not retrieved");
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Getting users by olderThan parameter")
    @Step("Make GET request to /users API with olderThan parameter")
    @Parameters({"age", "olderParam"})
    @Test
    public void getUsersOlderThan(int age, String olderParam) {
        Map<String, String> nameValueMap = new HashMap<>();
        nameValueMap.put(olderParam, String.valueOf(age));

        usersClient = new Users(client);
        List<User> users = usersClient.getAllUsersWithParam(nameValueMap);
        responseCode = usersClient.getStatusCodeOfResponse();
        boolean areUsersOlder = usersClient.areUsers(users, String.valueOf(age), OLDER_THAN);

        assertEquals(responseCode, SC_OK, "Expected and actual response code mismatch");
        assertTrue(areUsersOlder, "Not all users are older than " + age);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Getting users by youngerThan parameter")
    @Step("Make GET request to /users API with youngerThan parameter")
    @Parameters({"age", "youngerParam"})
    @Test
    public void getUsersYoungerThan(int age, String youngerParam) {
        Map<String, String> nameValueMap = new HashMap<>();
        nameValueMap.put(youngerParam, String.valueOf(age));

        usersClient = new Users(client);
        List<User> users = usersClient.getAllUsersWithParam(nameValueMap);
        responseCode = usersClient.getStatusCodeOfResponse();
        boolean areUsersYounger = usersClient.areUsers(users, String.valueOf(age), YOUNGER_THAN);

        assertEquals(responseCode, SC_OK, "Expected and actual response code mismatch");
        assertTrue(areUsersYounger, "Not all users are younger than " + age);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Getting users by sex parameter")
    @Step("Make GET request to /users API with sex parameter")
    @Parameters({"sex", "sexParam"})
    @Test
    public void getUsersWithSex(String sex, String sexParam) {
        Map<String, String> nameValueMap = new HashMap<>();
        nameValueMap.put(sexParam, sex.toUpperCase());

        usersClient = new Users(client);
        List<User> users = usersClient.getAllUsersWithParam(nameValueMap);
        responseCode = usersClient.getStatusCodeOfResponse();
        boolean areUsersWithSex = usersClient.areUsers(users, sex);

        assertEquals(responseCode, SC_OK, "Expected and actual response code mismatch");
        assertTrue(areUsersWithSex, "Not all users are " + sex);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Update users with all valid fields")
    @Step("Make PUT/PATCH request to /users API with existing user and user to update in the body")
    @Link(name = "US-2")
    @Test(dataProviderClass = DataUtilization.class, dataProvider = "updateUserReqFieldsProvider")
    public void updateUser(String newAge, String newName, String age, String name, String sex, String zipCode) {
        usersClient = new Users(client);
        User updatedUser = usersClient.updateUser(new User(parseInt(newAge), newName, sex, zipCode), new User(parseInt(age), name, sex, zipCode));
        responseCode = usersClient.getStatusCodeOfResponse();
        boolean isUserUpdated = usersClient.isUserUpdated(updatedUser);

        assertEquals(responseCode, SC_OK, "Expected and actual response code mismatch");
        assertTrue(isUserUpdated, "User is not updated");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Update users with invalid zipcode field")
    @Step("Make PUT/PATCH request to /users API with existing user and user to update with invalid zipcode in body")
    @Link(name = "US-2")
    @Test(dataProviderClass = DataUtilization.class, dataProvider = "updateUserWithInvZipProvider")
    public void updateUserWithInvalidZip(String newAge, String newName, String newSex, String newZipCode, String age, String name, String sex, String zipCode) {
        usersClient = new Users(client);
        User updatedUser = usersClient.updateUser(new User(parseInt(newAge), newName, newSex, newZipCode), new User(parseInt(age), name, sex, zipCode));
        responseCode = usersClient.getStatusCodeOfResponse();
        boolean isUserUpdated = usersClient.isUserUpdated(updatedUser);

        assertEquals(responseCode, SC_FAILED_DEPENDENCY, "Expected and actual response code mismatch");
        assertFalse(isUserUpdated, "User is updated");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Update users without required fields")
    @Step("Make PUT/PATCH request to /users API with existing user and user to update without required fields in the body")
    @Link(name = "US-3")
    @Test(dataProviderClass = DataUtilization.class, dataProvider = "updateUseOptFieldsProvider")
    public void updateUserWithoutReqFields(String newAge, String newZipCode, String age, String name, String sex, String zipCode) {
        usersClient = new Users(client);
        User updatedUser = usersClient.updateUser(new User(parseInt(newAge), newZipCode), new User(parseInt(age), name, sex, zipCode));
        responseCode = usersClient.getStatusCodeOfResponse();
        boolean isUserUpdated = usersClient.isUserUpdated(updatedUser);

        assertEquals(responseCode, SC_CONFLICT, "Expected and actual response code mismatch");
        assertFalse(isUserUpdated, "User is updated");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Delete user with all fields")
    @Step("Make DELETE request to /users API with body containing user with all fields")
    @Link(name = "US-4")
    @Test(dataProviderClass = DataUtilization.class, dataProvider = "allFieldsUserProvider")
    public void deleteUserWithAllFields(String age, String name, String sex, String zipCode) {
        User userToAdd = new User(parseInt(age), name, sex, zipCode);
        User userToDelete = new User(name, sex);

        usersClient = new Users(client);
        usersClient.addUser(userToAdd);
        usersClient.deleteUser(userToDelete);
        responseCode = usersClient.getStatusCodeOfResponse();
        boolean isUserExisted = usersClient.isUserAdded(userToDelete);

        zipCodeClient = new ZipCode(client);
        List<String> listOfZipCodes = zipCodeClient.getAllZipCodes();
        boolean isZipCodeAvailable = zipCodeClient.zipCodesAreSaved(listOfZipCodes, zipCode);

        assertEquals(responseCode, SC_NO_CONTENT, "Expected and actual response code mismatch");
        assertFalse(isUserExisted, "User is not removed");
        assertTrue(isZipCodeAvailable, "Zip code is not available");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Delete user with required fields")
    @Step("Make DELETE request to /users API with body containing user with required fields")
    @Link(name = "US-5")
    @Test(dataProviderClass = DataUtilization.class, dataProvider = "allFieldsUserProvider")
    public void deleteUserWithRequiredFields(String age, String name, String sex, String zipCode) {
        User userToAdd = new User(parseInt(age), name, sex, zipCode);
        User userToDelete = new User(name, sex);

        usersClient = new Users(client);
        usersClient.addUser(userToAdd);
        usersClient.deleteUser(userToDelete);
        responseCode = usersClient.getStatusCodeOfResponse();
        boolean isUserExisted = usersClient.isUserAdded(userToDelete);

        zipCodeClient = new ZipCode(client);
        List<String> listOfZipCodes = zipCodeClient.getAllZipCodes();
        boolean isZipCodeAvailable = zipCodeClient.zipCodesAreSaved(listOfZipCodes, zipCode);

        assertEquals(responseCode, SC_NO_CONTENT, "Expected and actual response code mismatch");
        assertFalse(isUserExisted, "User is not removed");
        assertTrue(isZipCodeAvailable, "Zip code is not available");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Delete user with missed required fields")
    @Step("Make DELETE request to /users API with body containing user with missed required fields")
    @Link(name = "US-6")
    @Test(dataProviderClass = DataUtilization.class, dataProvider = "allFieldsUserProvider")
    public void deleteUserWithMissedRequiredFields(String age, String name, String sex, String zipCode) {
        User userToAdd = new User(parseInt(age), name, sex, zipCode);
        User userToDelete = new User(name, zipCode);

        usersClient = new Users(client);
        usersClient.addUser(userToAdd);
        usersClient.deleteUser(userToDelete);
        responseCode = usersClient.getStatusCodeOfResponse();
        boolean isUserExisted = usersClient.isUserAdded(userToDelete);

        assertEquals(responseCode, SC_CONFLICT, "Expected and actual response code mismatch");
        assertTrue(isUserExisted, "User is removed");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Upload users from the file")
    @Step("Make POST request to /users/upload API with multiform body from json file")
    @Parameters({"allFieldsPath"})
    @Link(name = "US-7")
    @Test
    public void uploadUsersWithAllFields(String allFieldsPath) {
        File file = new File(allFieldsPath);
        List<User> listOfUploadedUsers = dataHandler.getObjectFromFile(file, List.class);
        usersClient = new Users(client);

        int numberOfUploadedUserResp = usersClient.uploadUsers(file);
        responseCode = usersClient.getStatusCodeOfResponse();
        boolean areUsersUploaded = usersClient.areUsersUploaded(listOfUploadedUsers);

        assertEquals(responseCode, SC_CREATED, "Expected and actual response code mismatch");
        assertTrue(areUsersUploaded, "Users are not uploaded");
        assertEquals(numberOfUploadedUserResp, listOfUploadedUsers.size(), "Expected and actual number of uploaded users mismatch");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Upload users from the file with invalid zipcodes")
    @Step("Make POST request to /users/upload API with multiform body from json file containing users with invalid zipcodes")
    @Parameters({"invZipcodePath"})
    @Link(name = "US-8")
    @Test
    public void uploadUsersWithInvalidZipCode(String invZipcodePath) {
        File file = new File(invZipcodePath);
        List<User> listOfUploadedUsers = dataHandler.getObjectFromFile(file, List.class);

        usersClient = new Users(client);
        usersClient.uploadUsers(file);

        responseCode = usersClient.getStatusCodeOfResponse();
        boolean areUsersUploaded = usersClient.areUsersUploaded(listOfUploadedUsers);

        assertEquals(responseCode, SC_FAILED_DEPENDENCY, "Expected and actual response code mismatch");
        assertFalse(areUsersUploaded, "Users are uploaded");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Upload users from the file with missed required fields")
    @Step("Make POST request to /users/upload API with multiform body from json file containing users with missed required fields")
    @Parameters({"missedFieldsPath"})
    @Link(name = "US-9")
    @Test
    public void uploadUsersWithMissedReqField(String missedFieldsPath) {
        File file = new File(missedFieldsPath);
        List<User> listOfUploadedUsers = dataHandler.getObjectFromFile(file, List.class);

        usersClient = new Users(client);
        usersClient.uploadUsers(file);

        responseCode = usersClient.getStatusCodeOfResponse();
        boolean areUsersUploaded = usersClient.areUsersUploaded(listOfUploadedUsers);

        assertEquals(responseCode, SC_CONFLICT, "Expected and actual response code mismatch");
        assertFalse(areUsersUploaded, "Users are uploaded");
    }
}

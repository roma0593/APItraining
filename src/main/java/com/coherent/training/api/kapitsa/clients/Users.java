package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.base.Client;
import com.coherent.training.api.kapitsa.base.ResponseWrapper;
import com.coherent.training.api.kapitsa.util.plainobjects.Conditions;
import com.coherent.training.api.kapitsa.util.plainobjects.UpdateUser;
import com.coherent.training.api.kapitsa.util.plainobjects.User;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.coherent.training.api.kapitsa.providers.UrlProvider.UPLOAD_USERS;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.USERS;
import static com.coherent.training.api.kapitsa.util.plainobjects.Conditions.OLDER_THAN;
import static com.coherent.training.api.kapitsa.util.plainobjects.Conditions.YOUNGER_THAN;
import static com.coherent.training.api.kapitsa.util.plainobjects.Scope.READ;
import static com.coherent.training.api.kapitsa.util.plainobjects.Scope.WRITE;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class Users extends BaseClient {
    private static final String USERS_ENDPOINT = USERS.getEndpoint();
    private static final String UPLOAD_ENDPOINT = UPLOAD_USERS.getEndpoint();
    private static final int POSITION_TO_CUT = 18;

    public Users(Client client) {
        super(client);
    }

    public void addUser(User user) throws IOException {
        try (ResponseWrapper response = client.post(USERS_ENDPOINT, setHeadersMap(WRITE), user)) {
        }
    }

    public List<User> getAllUsers() throws IOException {
        try (ResponseWrapper response = client.get(USERS_ENDPOINT, setHeadersMap(READ))) {

            User[] userArray = client.getResponseBody(User[].class);

            return Arrays.asList(userArray);
        }
    }

    public List<User> getAllUsersWithParam(Map<String, String> paramMap) throws IOException {
        try (ResponseWrapper response = client.get(USERS_ENDPOINT, setHeadersMap(READ), paramMap)) {

            User[] userArray = client.getResponseBody(User[].class);

            return Arrays.asList(userArray);
        }
    }

    public void updateUser(User userChanger, User userToChange) throws IOException {
        UpdateUser updateUser = new UpdateUser(userChanger, userToChange);

        try (ResponseWrapper response = client.patch(USERS_ENDPOINT, setHeadersMap(WRITE), updateUser)) {
        }
    }

    public int uploadUsers(File file) throws IOException {
        Map<String, String> headersMap = setHeadersMap(WRITE);
        headersMap.remove(CONTENT_TYPE);

        try (ResponseWrapper response = client.post(UPLOAD_ENDPOINT, headersMap, file)) {

            int responseCode = getStatusCodeOfResponse();

            if (responseCode == 201) {
                String responseBody = client.getResponseBodyAsString();
                return getNumberOfUploadedUsers(responseBody);
            } else {
                return 0;
            }
        }
    }

    public void deleteUser(User userToDelete) throws IOException {
        try (ResponseWrapper response = client.delete(USERS_ENDPOINT, setHeadersMap(WRITE), userToDelete)) {
        }
    }

    private int getNumberOfUploadedUsers(String uploadResponse) {
        return Integer.parseInt(uploadResponse.substring(POSITION_TO_CUT));
    }

    @SneakyThrows
    public boolean areUsersUploaded(List<User> uploadedUsers) {
        List<User> savedUsers = getAllUsers();

        return Objects.equals(uploadedUsers, savedUsers);
    }

    @SneakyThrows
    public boolean isUserAdded(User userFromJson) {
        List<User> userList = getAllUsers();

        return userList.contains(userFromJson);
    }

    public boolean areUsers(List<User> userList, String value, Conditions... condition) {
        boolean areUsers = true;

        for (User user : userList) {
            if (condition.length > 0 && condition[0].equals(YOUNGER_THAN)) {
                areUsers = user.getAge() < Integer.parseInt(value);
            } else if (condition.length > 0 && condition[0].equals(OLDER_THAN)) {
                areUsers = user.getAge() > Integer.parseInt(value);
            } else {
                areUsers = user.getSex().equalsIgnoreCase(value);
            }

            if (!areUsers) break;
        }

        return areUsers;
    }
}

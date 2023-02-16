package com.coherent.training.api.kapitsa.rest_assured_clients;

import com.coherent.training.api.kapitsa.util.plainobjects.Conditions;
import com.coherent.training.api.kapitsa.util.plainobjects.UpdateUser;
import com.coherent.training.api.kapitsa.util.plainobjects.User;

import java.io.File;
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

    public void addUser(User user) {
        response = client.post(USERS_ENDPOINT, setHeadersMap(WRITE), user);
    }

    public List<User> getAllUsers() {
        response = client.get(USERS_ENDPOINT, setHeadersMap(READ));

        User[] userArray = client.getResponseBody(response, User[].class);

        return Arrays.asList(userArray);
    }

    public List<User> getAllUsersWithParam(Map<String, String> paramMap) {
        response = client.get(USERS_ENDPOINT, setHeadersMap(READ), paramMap);

        User[] userArray = client.getResponseBody(response, User[].class);

        return Arrays.asList(userArray);
    }

    public void updateUser(User userChanger, User userToChange) {
        UpdateUser updateUser = new UpdateUser(userChanger, userToChange);

        response = client.patch(USERS_ENDPOINT, setHeadersMap(WRITE), updateUser);
    }

    public int uploadUsers(File file) {
        Map<String, String> headersMap = setHeadersMap(WRITE);
        headersMap.remove(CONTENT_TYPE);

        response = client.post(UPLOAD_ENDPOINT, headersMap, file);

        int responseCode = getStatusCodeOfResponse();

        if (responseCode == 201) {
            String responseBody = response.body().asString();
            return getNumberOfUploadedUsers(responseBody);
        } else {
            return 0;
        }
    }

    public void deleteUser(User userToDelete) {
        client.delete(USERS_ENDPOINT, setHeadersMap(WRITE), userToDelete);
    }

    private int getNumberOfUploadedUsers(String uploadResponse) {
        return Integer.parseInt(uploadResponse.substring(POSITION_TO_CUT));
    }

    public boolean areUsersUploaded(List<User> uploadedUsers) {
        List<User> savedUsers = getAllUsers();

        return Objects.equals(uploadedUsers, savedUsers);
    }

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

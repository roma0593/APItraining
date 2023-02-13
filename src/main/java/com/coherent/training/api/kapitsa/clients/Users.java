package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.util.plainobjects.Conditions;
import com.coherent.training.api.kapitsa.util.plainobjects.UpdateUser;
import com.coherent.training.api.kapitsa.util.plainobjects.User;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.coherent.training.api.kapitsa.providers.UrlProvider.UPLOAD_USERS;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.USERS;
import static com.coherent.training.api.kapitsa.util.interceptors.ResponseInterceptor.getEntity;
import static com.coherent.training.api.kapitsa.util.plainobjects.Conditions.OLDER_THAN;
import static com.coherent.training.api.kapitsa.util.plainobjects.Conditions.YOUNGER_THAN;
import static com.coherent.training.api.kapitsa.util.plainobjects.Scope.READ;
import static com.coherent.training.api.kapitsa.util.plainobjects.Scope.WRITE;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_CREATED;

public class Users extends BaseClient {
    private static final String USERS_ENDPOINT = USERS.getEndpoint();
    private static final String UPLOAD_ENDPOINT = UPLOAD_USERS.getEndpoint();
    private static final int POSITION_TO_CUT = 18;

    public Users(CloseableHttpClient client) {
        super(client);
    }

    @SneakyThrows
    public User addUser(User user) {
        try {
            response = baseClient.post(USERS_ENDPOINT, setHeadersMap(WRITE), user);

            return baseClient.getRequestBody(User.class);
        } finally {
            baseClient.closeResponse(response);
        }
    }

    public List<User> getAllUsers() {
        try {
            response = baseClient.get(USERS_ENDPOINT, setHeadersMap(READ));

            User[] userArray = baseClient.getResponseBody(User[].class, response);
            return Arrays.asList(userArray);
        } finally {
            baseClient.closeResponse(response);
        }
    }

    public List<User> getAllUsersWithParam(Map<String, String> nameValueMap) {
        try {
            response = baseClient.get(USERS_ENDPOINT, setHeadersMap(READ), nameValueMap);

            User[] userArray = baseClient.getResponseBody(User[].class, response);
            return Arrays.asList(userArray);
        } finally {
            baseClient.closeResponse(response);
        }
    }

    @SneakyThrows
    public User updateUser(User userChanger, User userToChange) {
        UpdateUser updateUser = new UpdateUser(userChanger, userToChange);

        try {
            response = baseClient.patch(USERS_ENDPOINT, setHeadersMap(WRITE), updateUser);
            return baseClient.getRequestBody(UpdateUser.class).getUserNewValues();
        } finally {
            baseClient.closeResponse(response);
        }
    }

    public int uploadUsers(File file) {
        try {
            Map<String, String> headersMap = setHeadersMap(WRITE);
            headersMap.remove(CONTENT_TYPE);

            response = baseClient.post(UPLOAD_ENDPOINT, headersMap, file);

            int responseCode = getStatusCodeOfResponse();

            if (responseCode == SC_CREATED) {
                return getNumberOfUploadedUsers(getEntity());
            } else {
                return 0;
            }

        } finally {
            baseClient.closeResponse(response);
        }
    }

    public void deleteUser(User userToDelete) {
        try {
            response = baseClient.delete(USERS_ENDPOINT, setHeadersMap(WRITE), userToDelete);
        } finally {
            baseClient.closeResponse(response);
        }
    }

    private int getNumberOfUploadedUsers(String uploadResponse) {
        return Integer.parseInt(uploadResponse.substring(POSITION_TO_CUT));
    }

    public boolean areUsersUploaded(List<User> uploadedUsers) {
        List<User> savedUsers = getAllUsers();

        return CollectionUtils.isEqualCollection(uploadedUsers, savedUsers);
    }

    public boolean isUserAdded(User userFromJson) {
        List<User> userList = getAllUsers();

        boolean isAdded = false;

        for (User user : userList) {
            if (userFromJson.equals(user)) {
                isAdded = true;
                break;
            }
        }

        return isAdded;
    }

    public boolean isUserUpdated(User updatedUser) {
        boolean isUserUpdated = false;

        for (User user : getAllUsers()) {
            if (user.equals(updatedUser)) {
                isUserUpdated = true;
                break;
            }
        }

        return isUserUpdated;
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

package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.util.plainobjects.User;
import lombok.SneakyThrows;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Arrays;
import java.util.List;

import static com.coherent.training.api.kapitsa.providers.UrlProvider.USERS;
import static com.coherent.training.api.kapitsa.util.plainobjects.Scope.READ;
import static com.coherent.training.api.kapitsa.util.plainobjects.Scope.WRITE;

public class Users extends BaseClient{
    private static final String USERS_ENDPOINT = USERS.getEndpoint();

    public Users(CloseableHttpClient client) {
        super(client);
    }

    @SneakyThrows
    public User addUser(User user){
        try {
            response = baseClient.post(USERS_ENDPOINT, setHeadersMap(WRITE), user);

            return baseClient.getRequestBody(User.class);
        } finally {
            baseClient.closeResponse(response);
        }
    }

    public List<User> getAllUsers(){
        try {
            response = baseClient.get(USERS_ENDPOINT, setHeadersMap(READ));

            User[] userArray = baseClient.getSuccessResponseBody(User[].class, response);
            return Arrays.asList(userArray);
        } finally {
            baseClient.closeResponse(response);
        }
    }

    public boolean isUserAdded(User userFromJson){
        List<User> userList = getAllUsers();

        boolean isAdded = false;

        for (User user : userList){
            if (userFromJson.equals(user)) {
                isAdded = true;
                break;
            }
        }

        return isAdded;
    }
}

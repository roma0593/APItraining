package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.util.plainobjects.User;
import lombok.SneakyThrows;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static com.coherent.training.api.kapitsa.providers.Scope.READ;
import static com.coherent.training.api.kapitsa.providers.Scope.WRITE;
import static com.coherent.training.api.kapitsa.providers.UrlProvider.USERS;

public class Users extends BaseClient{
    private static final String USERS_ENDPOINT = USERS.getEndpoint();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Users.class.getSimpleName());

    public Users(CloseableHttpClient client) {
        super(client);
    }

    @SneakyThrows
    public User addUser(String userJson){
        StringEntity entity = new StringEntity(userJson);

        baseClient = builder.setRequest(USERS_ENDPOINT, POST, setHeadersMap(WRITE), entity).build();

        User requestBody = baseClient.getRequestBody(User.class);

        logger.info("Request: {}", baseClient.getRequest() + "\n" + requestBody);

        response = baseClient.executeRequest();

        logger.info("Response: {}", response);

        baseClient.closeResponse(response);

        return requestBody;
    }

    public List<User> getAllUsers(){
        baseClient = builder.setRequest(USERS_ENDPOINT, GET, setHeadersMap(READ)).build();

        logger.info("Request: {}", baseClient.getRequest());

        response = baseClient.executeRequest();

        User[] userArray = baseClient.getSuccessResponseBody(User[].class, response);

        logger.info("Response: {}, {}", response, Arrays.toString(userArray));

        baseClient.closeResponse(response);

        return Arrays.asList(userArray);
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

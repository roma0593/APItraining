package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.util.plainobjects.User;
import lombok.SneakyThrows;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

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
        baseClient = builder.setRequest(USERS_ENDPOINT, POST, setHeadersMap("write"), entity).build();

        User requestBody = baseClient.getRequestBody(User.class);

        logger.info("Request: {}", baseClient.getRequest() + "\n" + requestBody);

        baseClient.executeRequest();

        logger.info("Response: {}", baseClient.getResponse());

        baseClient.closeResponse();

        return requestBody;
    }

    public List<User> getAllUsers(){
        baseClient = builder.setRequest(USERS_ENDPOINT, GET, setHeadersMap("read")).build();

        logger.info("Request: {}", baseClient.getRequest());

        baseClient.executeRequest();

        User[] userArray = baseClient.getResponseBody(User[].class);

        logger.info("Response: {}, {}", baseClient.getResponse(), Arrays.toString(userArray));

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

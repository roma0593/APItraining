package com.coherent.training.api.kapitsa.clients;

import com.coherent.training.api.kapitsa.util.plainobjects.User;
import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

            User[] userArray = baseClient.getResponseBody(User[].class, response);
            return Arrays.asList(userArray);
        } finally {
            baseClient.closeResponse(response);
        }
    }

    public List<User> getAllUsersWithParam(Map<String, String> nameValueMap){
        List<NameValuePair> nvp = getNvp(nameValueMap);

        try {
            response = baseClient.get(USERS_ENDPOINT, setHeadersMap(READ), nvp);

            User[] userArray = baseClient.getResponseBody(User[].class, response);
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

    public boolean areUsers(List<User> userList, String value, String... condition){
        boolean areUsers = true;

        for (User user : userList){
            if(condition.length > 0 && condition[0].equals("youngerThan")){
                areUsers = user.getAge() < Integer.parseInt(value);
            } else if (condition.length > 0 && condition[0].equals("olderThan")){
                areUsers = user.getAge() > Integer.parseInt(value);
            } else {
                areUsers = user.getSex().equalsIgnoreCase(value);
            }

            if (!areUsers) break;
        }

        return areUsers;
    }

    private List<NameValuePair> getNvp(Map<String, String> nameValueMap){
        List<NameValuePair> nvp = new ArrayList<>();

        for(String key : nameValueMap.keySet()){
            nvp.add(new BasicNameValuePair(key, nameValueMap.get(key)));
        }

        return nvp;
    }
}

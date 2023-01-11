package com.coherent.training.api.kapitsa.utils;

import com.coherent.training.api.kapitsa.util.plainobjects.User;
import org.testng.annotations.DataProvider;

import java.io.File;

public class DataUtilization {
    private static final String ALL_FIELDS_USER = "src/test/resources/test_data/users/all_fields_user.json";
    private static final String REQUIRED_FIELDS_USER = "src/test/resources/test_data/users/required_fields_user.json";
    private static final String INVALID_ZIPCODE_USER = "src/test/resources/test_data/users/user_with_invalid_zip_code.json";
    private static final String INVALID_NAME_SEX_USER = "src/test/resources/test_data/users/invalid_namesex_pair_user.json";
    private static final JsonParser jsonParser = new JsonParser();

    @DataProvider(name = "allFieldsUserProvider")
    public static Object[][] allFieldsUserProvider(){
        return userProvider(ALL_FIELDS_USER);
    }

    @DataProvider(name = "requiredFieldsUserProvider")
    public static Object[][] requiredFieldsUserProvider(){
        return userProvider(REQUIRED_FIELDS_USER);
    }

    @DataProvider(name = "invalidZipcodeUserProvider")
    public static Object[][] invalidZipcodeUserProvider(){
        return userProvider(INVALID_ZIPCODE_USER);
    }

    @DataProvider(name = "invalidNameSexUserProvider")
    public static Object[][] invalidNameSexUserProvider(){
        return userProvider(INVALID_NAME_SEX_USER);
    }

    private static Object[][] userProvider(String path){
        String[] usersJson = jsonParser.getDataFromJson(new File(path), User[].class);

        String[][] dataArray = new String[usersJson.length][1];

        int arrayIndex = 0;

        for (String userJson : usersJson){
            dataArray[arrayIndex++][0] = userJson;
        }

        return dataArray;
    }
}

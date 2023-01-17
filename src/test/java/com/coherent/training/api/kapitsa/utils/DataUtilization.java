package com.coherent.training.api.kapitsa.utils;

import com.coherent.training.api.kapitsa.util.plainobjects.User;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        User[] users = jsonParser.getDataFromJson(new File(path), User[].class);

        String[][] dataArray = new String[users.length][4];

        String age, name, sex, zipCode;

        int arrayIndex = 0;

        for (User user : users){
            name = user.getName();
            age = String.valueOf(user.getAge());
            sex = user.getSex();
            zipCode = user.getZipCode();

            List<String> userFields = Arrays.asList(age, name, sex, zipCode);

            int userId = 0;

            for(String value : userFields){
                if (StringUtils.isNotBlank(value)) {
                    dataArray[arrayIndex][userId++] = value;
                }
            }

            arrayIndex++;
        }

        return filterNullsInInnerArray(dataArray);
    }

    private static Object[] filterNulls(Object[] arr){
        return Arrays.stream(arr).filter(Objects::nonNull)
                .filter(obj -> !obj.equals("0"))
                .toArray();
    }

    private static Object[][] filterNullsInInnerArray(Object[][] arr){
        List<Object[]> outerList = new ArrayList<>(arr.length);

        for (Object[] inner : arr) {
            outerList.add(filterNulls(inner));
        }

        return outerList.toArray(new Object[outerList.size()][]);
    }
}

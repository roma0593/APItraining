package com.coherent.training.api.kapitsa.utils;

import com.coherent.training.api.kapitsa.util.plainobjects.User;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.DataProvider;
import org.testng.collections.Lists;

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
    private static final String USER_FOR_UPDATE_REQ_FIELDS = "src/test/resources/test_data/users/user_for_update_req_fields.json";
    private static final String USER_FOR_UPDATE_OPT_FIELDS = "src/test/resources/test_data/users/user_for_update_opt_fields.json";
    private static final JsonParser jsonParser = new JsonParser();

    @DataProvider(name = "allFieldsUserProvider")
    public static Object[][] allFieldsUserProvider() {
        return userProvider(ALL_FIELDS_USER);
    }

    @DataProvider(name = "requiredFieldsUserProvider")
    public static Object[][] requiredFieldsUserProvider() {
        return userProvider(REQUIRED_FIELDS_USER);
    }

    @DataProvider(name = "invalidZipcodeUserProvider")
    public static Object[][] invalidZipcodeUserProvider() {
        return userProvider(INVALID_ZIPCODE_USER);
    }

    @DataProvider(name = "invalidNameSexUserProvider")
    public static Object[][] invalidNameSexUserProvider() {
        return userProvider(INVALID_NAME_SEX_USER);
    }

    @DataProvider(name = "updateUserReqFieldsProvider")
    public static Object[][] updateUserReqFieldsProvider() {
        return mergeDataProviders(usersForUpdateProvider(USER_FOR_UPDATE_REQ_FIELDS), allFieldsUserProvider());
    }

    @DataProvider(name = "updateUserWithInvZipProvider")
    public static Object[][] updateUserWithInvZipProvider() {
        return mergeDataProviders(invalidZipcodeUserProvider(), allFieldsUserProvider());
    }

    @DataProvider(name = "updateUseOptFieldsProvider")
    public static Object[][] updateUseOptFieldsProvider() {
        return mergeDataProviders(usersForUpdateProvider(USER_FOR_UPDATE_OPT_FIELDS), allFieldsUserProvider());
    }

    public static Object[][] usersForUpdateProvider(String path) {
        return userProvider(path);
    }

    private static Object[][] userProvider(String path) {
        User[] users = jsonParser.getDataFromJson(new File(path), User[].class);

        String[][] dataArray = new String[users.length][4];

        String age, name, sex, zipCode;

        int arrayIndex = 0;

        for (User user : users) {
            name = user.getName();
            age = String.valueOf(user.getAge());
            sex = user.getSex();
            zipCode = user.getZipCode();

            List<String> userFields = Arrays.asList(age, name, sex, zipCode);

            int userId = 0;

            for (String value : userFields) {
                if (StringUtils.isNotBlank(value)) {
                    dataArray[arrayIndex][userId++] = value;
                }
            }

            arrayIndex++;
        }

        return filterNullsInInnerArray(dataArray);
    }

    private static Object[][] mergeDataProviders(Object[][]... dataProviders) {
        List<Object[]> providers = Lists.newArrayList();

        int size = 1;

        for (Object[][] dataProvider : dataProviders) {
            providers.addAll(Arrays.asList(dataProvider));
            size = dataProvider.length;
        }

        String[][] dataArray = new String[size][10];

        for (int i = 0; i < size; i++) {
            int index = 0;

            for (int j = i; j < providers.size() * size; j += size) {
                Object[] notNullProviders = filterNulls(providers.get(j));

                for (Object object : notNullProviders) {
                    dataArray[i][index++] = (String) object;
                }
            }
        }

        return filterNullsInInnerArray(dataArray);
    }

    private static Object[] filterNulls(Object[] arr) {
        return Arrays.stream(arr).filter(Objects::nonNull)
                .filter(obj -> !obj.equals("0"))
                .toArray();
    }

    private static Object[][] filterNullsInInnerArray(Object[][] arr) {
        List<Object[]> outerList = new ArrayList<>(arr.length);

        for (Object[] inner : arr) {
            outerList.add(filterNulls(inner));
        }

        return outerList.toArray(new Object[outerList.size()][]);
    }
}

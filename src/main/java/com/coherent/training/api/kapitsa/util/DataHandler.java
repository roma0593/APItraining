package com.coherent.training.api.kapitsa.util;

import com.coherent.training.api.kapitsa.util.plainobjects.Token;
import com.google.gson.Gson;
import org.openqa.selenium.json.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {
    private static final Gson gson = new Gson();

    public static Token getTokenObj(String json){
        return gson.fromJson(json, Token.class);
    }

    public static List<String> getListOfObj(String json){
        Type stringListType = new TypeToken<ArrayList<String>>(){}.getType();

        return gson.fromJson(json, stringListType);
    }
}

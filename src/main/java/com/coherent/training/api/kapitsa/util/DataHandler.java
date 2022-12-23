package com.coherent.training.api.kapitsa.util;

import com.coherent.training.api.kapitsa.util.plainobjects.Token;
import com.google.gson.Gson;

public class DataHandler {
    private static final Gson gson = new Gson();

    public static Token getTokenObj(String json){
        return gson.fromJson(json, Token.class);
    }
}

package com.coherent.training.api.kapitsa.utils;

import com.google.gson.Gson;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.stream.Collectors;

public class JsonParser {
    private final Gson gson;

    public JsonParser() {
        gson = new Gson();
    }

    public String[] getArray(String json){
        return gson.fromJson(json, String[].class);
    }

    public String[] getArrayFromFile(File file){
        String zipCodes = readFromJson(file);

        return gson.fromJson(zipCodes, String[].class);
    }

    @SneakyThrows
    private String readFromJson(File file){
        BufferedReader reader = new BufferedReader(new FileReader(file));

        return reader.lines().collect(Collectors.joining());
    }
}

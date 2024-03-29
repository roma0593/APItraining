package com.coherent.training.api.kapitsa.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.File;

public class DataHandler {
    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public <T> T getObject(String json, Class<T> tClass){
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        return mapper.readValue(json, tClass);
    }

    @SneakyThrows
    public <T> T getObjectFromFile(File file, Class<T> tClass){
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        return mapper.readValue(file, tClass);
    }

    @SneakyThrows
    public <T> String convertToJson(T tClass){
        return mapper.writeValueAsString(tClass);
    }
}

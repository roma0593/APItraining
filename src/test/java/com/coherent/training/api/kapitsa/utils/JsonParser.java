package com.coherent.training.api.kapitsa.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class JsonParser {
    private final ObjectMapper mapper = new ObjectMapper();


    @SneakyThrows
    public <T> T[] getDataFromJson(File file, Class<T[]> tClass) {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {

            mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

            return mapper.readValue(reader, tClass);
        }
    }
}

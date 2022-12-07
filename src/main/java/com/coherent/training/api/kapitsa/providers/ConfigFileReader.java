package com.coherent.training.api.kapitsa.providers;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.Properties;

import static com.coherent.training.api.kapitsa.providers.ConfigProvider.CLIENT_ID;
import static com.coherent.training.api.kapitsa.providers.ConfigProvider.CLIENT_SECRET;

public class ConfigFileReader {
    private Properties properties;
    private static Map<String, Properties> propsCache;
    private static final String CONFIG_PATH = "configs//config.properties";

    @SneakyThrows
    private ConfigFileReader() {}

    @SneakyThrows
    private void setProperty() {
        BufferedReader reader;

        reader = new BufferedReader(new FileReader(CONFIG_PATH));

        properties = new Properties();
        properties.load(reader);
    }

    private static class SingletonConfigFileReader {
        private static final ConfigFileReader INSTANCE = new ConfigFileReader();
    }

    public static ConfigFileReader getInstance() {
        return SingletonConfigFileReader.INSTANCE;
    }

    public String getClientId() {
        setProperty();

        String clientId = properties.getProperty(CLIENT_ID.getPropertyKey());

        if (clientId != null) return clientId;
        else throw new RuntimeException("Client_id is not specified");
    }

    public String getClientSecret() {
        setProperty();

        String clientSecret = properties.getProperty(CLIENT_SECRET.getPropertyKey());

        if (clientSecret != null) return clientSecret;
        else throw new RuntimeException("Client secret is not specified");
    }
}

package com.coherent.training.api.kapitsa.providers;

import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.coherent.training.api.kapitsa.providers.ConfigProvider.*;

public class ConfigFileReader {
    private Properties properties;
    private static Map<String, Properties> propsCache;
    private static final String CONFIG_PATH = "configs//config.properties";
    private static final String PROFILE_PATH = "configs//profile.properties";

    @SneakyThrows
    private ConfigFileReader() {setPropsCache();}

    @SneakyThrows
    private void setPropsCache(){
        List<String> propPathList = List.of(CONFIG_PATH, PROFILE_PATH);
        propsCache = new HashMap<>();
        BufferedReader reader;

        for(String propPath : propPathList){
            reader = new BufferedReader(new FileReader(propPath));
            Properties property = new Properties();
            property.load(reader);

            String propertyName = FilenameUtils.getName(propPath);

            propsCache.put(propertyName, property);
        }
    }

    private static Properties getPropertyFromCache(String path){
        String propertyName = FilenameUtils.getName(path);

        return propsCache.get(propertyName);
    }

    private static class SingletonConfigFileReader {
        private static final ConfigFileReader INSTANCE = new ConfigFileReader();
    }

    public static ConfigFileReader getInstance() {
        return SingletonConfigFileReader.INSTANCE;
    }

    public String getClientId() {
        properties = getPropertyFromCache(CONFIG_PATH);

        String clientId = properties.getProperty(CLIENT_ID.getPropertyKey());

        if (clientId != null) return clientId;
        else throw new RuntimeException("Client_id is not specified");
    }

    public String getClientSecret() {
        properties = getPropertyFromCache(CONFIG_PATH);

        String clientSecret = properties.getProperty(CLIENT_SECRET.getPropertyKey());

        if (clientSecret != null) return clientSecret;
        else throw new RuntimeException("Client secret is not specified");
    }

    public String getHostUrl(){
        properties = getPropertyFromCache(PROFILE_PATH);

        String profile = properties.getProperty(PROFILE.getPropertyKey());

        String hostUrlKey = String.format(HOST_KEY.getPropertyKey(), profile);

        String hostUrl = properties.getProperty(hostUrlKey);

        if (hostUrl != null) return hostUrl;
        else throw new RuntimeException("hostURL is not specified");
    }
}

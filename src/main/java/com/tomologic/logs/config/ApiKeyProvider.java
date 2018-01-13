package com.tomologic.logs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Responsible for getting api keys for the application
 * these keys are compared with the one coming in request
 * Practically they should be saved somewhere else like encrypted in database
 */

@Configuration
@ConfigurationProperties("key")
public class ApiKeyProvider {
    private static String normalUserApiKey;
    private static String rootUserApiKey;

    public static String getRootUserApiKey() {
        return rootUserApiKey;
    }

    public static void setRootUserApiKey(String rootKey) {
        rootUserApiKey = rootKey;
    }

    public static String getNormalUserApiKey() {
        return normalUserApiKey;
    }

    public static void setNormalUserApiKey(String normalKey) {
        normalUserApiKey = normalKey;
    }
}

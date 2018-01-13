package com.tomologic.logs.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class contains configuration information for the application to use
 * SUDO password should not be saved here but it should under some safe place
 * For simplicity we are keek it here
 */

@Configuration
@ConfigurationProperties("config")
public class Config {
    public static String LOG_PATH;
    public static int OLD_FILE_TIME;
    public static String USER_ROLE;
    public static String SUDO_PASS;

    public static String getLogPath() {
        return LOG_PATH;
    }

    public static void setLogPath(String logPath) {
        LOG_PATH = logPath;
    }

    public static int getOldFileTime() {
        return OLD_FILE_TIME;
    }

    public static void setOldFileTime(int oldFileTime) {
        OLD_FILE_TIME = oldFileTime;
    }

    public static String getUserRole() {
        return USER_ROLE;
    }

    public static void setUserRole(String userRole) {
        USER_ROLE = userRole;
    }

    public static String getSudoPass() {
        return SUDO_PASS;
    }

    public static void setSudoPass(String sudoPass) {
        SUDO_PASS = sudoPass;
    }


}

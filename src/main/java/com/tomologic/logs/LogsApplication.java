package com.tomologic.logs;


import com.tomologic.logs.config.Config;
import com.tomologic.logs.service.PublishLog;
import com.tomologic.logs.service.impl.PublishLogImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * The main class which starts the application
 */

@SpringBootApplication
public class LogsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogsApplication.class, args);
    }

    @Bean
    public PublishLog publishLog() {
        return new PublishLogImpl(Config.USER_ROLE);
    }
}

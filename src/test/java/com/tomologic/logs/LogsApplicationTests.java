package com.tomologic.logs;

import com.tomologic.logs.config.ApiKeyProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LogsApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ApiKeyProvider apikeyPro;

    private Logger log = LoggerFactory.getLogger(LogsApplicationTests.class);

    @Before
    public void setUp() throws Exception {
        copyFile("src/test/test.log", "/var/log/test.log");
    }

    @After
    public void tearDown() throws Exception {
        deleteFile("/var/log/test.log");
    }

    @Test
    public void shouldRetunOkayResponseForLogList() throws Exception {

        mockMvc.perform(
                get("/logs/list")
                        .header("api-key", apikeyPro.getRootUserApiKey())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()
                );
    }

    @Test
    public void shouldRetunOkayResponseForLogFile() throws Exception {
        mockMvc.perform(
                get("/logs/content?path=test.log")
                        .header("api-key", apikeyPro.getRootUserApiKey())
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(content().json("{\"name\":\"test.log\",\"contents\":\"This is a test log file\"}"));
    }


    private void copyFile(String sourceFile, String destFile)
            throws IOException {

        FileChannel source = new FileInputStream(sourceFile).getChannel();
        FileChannel destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }

    }

    private void deleteFile(String fileName) {
        try {

            File file = new File(fileName);

            if (file.delete()) {
                log.debug(file.getName() + " is deleted!");
            } else {
                log.debug("Delete operation is failed.");
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

}

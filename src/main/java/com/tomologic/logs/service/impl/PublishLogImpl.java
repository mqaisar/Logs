package com.tomologic.logs.service.impl;

import com.tomologic.logs.exception.CustomIOException;
import com.tomologic.logs.exception.ResourceNotFoundException;
import com.tomologic.logs.model.FileData;
import com.tomologic.logs.model.LogFile;
import com.tomologic.logs.service.PublishLog;
import com.tomologic.logs.config.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class PublishLogImpl implements PublishLog {
    private final static Logger logger = Logger.getLogger(PublishLogImpl.class);
    private String role = "user";

    public PublishLogImpl(String role) {
        this.role = role;
    }

    /**
     * @param  inPath is the path
     * @param  filter on which files will be filtered
     * @returns list of files
     */
    public List<LogFile> listFilteredLogFiles(String inPath, String filter) {
        final List<LogFile> logFiles = new ArrayList<LogFile>();
        if (StringUtils.isEmpty(inPath)) {
            inPath = Config.LOG_PATH;

        } else {
            inPath = Config.LOG_PATH + File.separator + inPath;
        }

        logger.debug("Path to look at " + inPath + " with filter " + filter);
        Path start = Paths.get(inPath);

        try {
            LogFileVisiter logFileVisiter = new LogFileVisiter(logFiles);
            logFileVisiter.setFileFilter(filter);
            Files.walkFileTree(start, logFileVisiter);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new CustomIOException(ex.getMessage());
        }

        return logFiles;
    }
    /** Gets list of files in the path
     * @param  inPath is the path
     * @returns list of files
     */

    public List<LogFile> listLogFiles(String inPath) {
        final List<LogFile> logFiles = new ArrayList<LogFile>();

        if (StringUtils.isEmpty(inPath)) {
            inPath = Config.LOG_PATH;

        } else {
            inPath = Config.LOG_PATH + File.separator + inPath;
        }

        logger.debug("Path to look at " + inPath);

        Path start = Paths.get(inPath);

        try {
            LogFileVisiter logFileVisiter = new LogFileVisiter(logFiles);
            Files.walkFileTree(start, logFileVisiter);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new CustomIOException(ex.getMessage());
        }

        return logFiles;
    }

    /** Reads and returns content of the requested file
     * @param  filePath path to the file
     * @returns content of the requested file
     */
    public FileData getContent(String filePath) throws ResourceNotFoundException{
        logger.debug("getContent for " + filePath);
        filePath = Config.LOG_PATH + File.separator + filePath;
        Path path = Paths.get(filePath);
        try {
            if (!path.toFile().exists() ||
                    (!this.role.equals("admin") && !path.toFile().canRead())) {
                throw new ResourceNotFoundException("Could not find requested resource");
            }
            String contents = "";
            if (this.role.equals("admin")) {
                //read using sudo cat command
                return this.getRestrictedContent(filePath);
            } else {
                contents = new String(Files.readAllBytes(path));
            }

            FileData fd = new FileData();
            fd.setName(path.getFileName().toString());
            fd.setContents(contents);
            return fd;

        } catch (IOException ex) {
            logger.error("IO failure", ex);
            throw new ResourceNotFoundException("IO error occurred");
        }
    }
    /** Reads and returns content of the requested file under sudo permission
     * @param  path path to the file
     * @returns content of the requested file
     */
    private FileData getRestrictedContent(String path) {

        logger.debug("getRestrictedContent for " + path + " with role: " + role);
        if (role == null || !role.equals("admin")) {
            throw new ResourceNotFoundException("Could not find requested resource");
        }
        String[] cmd = {"/bin/bash", "-c", "echo " + Config.SUDO_PASS + "| sudo -S cat " + path};

        try {
            Process process = Runtime.getRuntime().exec(cmd);
            StringBuilder wholeBuffer = new StringBuilder();

            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = input.readLine()) != null) {
                wholeBuffer.append(line + "\n");
            }
            input.close();

            FileData fd = new FileData();
            fd.setName(path);
            fd.setContents(wholeBuffer.toString());
            return fd;

        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new ResourceNotFoundException("IO error occurred");
        }

    }
}

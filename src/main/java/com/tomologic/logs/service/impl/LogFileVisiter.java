package com.tomologic.logs.service.impl;

import com.tomologic.logs.model.LogFile;
import com.tomologic.logs.config.Config;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.List;

public class LogFileVisiter extends SimpleFileVisitor<Path> {

    private List<LogFile> logFiles;
    private PathMatcher matcher;

    public LogFileVisiter(List<LogFile> logFilesCollection) {
        this.logFiles = logFilesCollection;
    }

    public void setFileFilter(String filter) {
        matcher = FileSystems.getDefault()
                .getPathMatcher("glob:" + filter);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) throws IOException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, Config.OLD_FILE_TIME);

        if (attributes.lastModifiedTime().toMillis() >= cal.getTimeInMillis()) {
            if (matcher != null) {
                if (dir.getFileName() != null && matcher.matches(dir.getFileName())) {
                    this.logFiles.add(getLogFile(dir, attributes));
                }
            } else {
                this.logFiles.add(getLogFile(dir, attributes));
            }
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, Config.OLD_FILE_TIME);

        if (attributes.lastModifiedTime().toMillis() >= cal.getTimeInMillis()) {
            //also apply the filter if set by user

            if (matcher != null) {
                if (file.getFileName() != null && matcher.matches(file.getFileName())) {
                    //filtered name is found.
                    //populate collection
                    logFiles.add(getLogFile(file, attributes));
                }
            } else {
                //populate collection
                logFiles.add(getLogFile(file, attributes));
            }

        }

        //continue file walk
        return FileVisitResult.CONTINUE;
    }
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, Config.OLD_FILE_TIME);

        BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
        if (attributes.lastModifiedTime().toMillis() >= cal.getTimeInMillis()) {
            //set LogFile object
            LogFile lf = new LogFile();
            lf.setName(file.getFileName().toString());
            lf.setPath(file.toAbsolutePath().toString());
            lf.setSize(-1);
            lf.setType('n');
            lf.setCanRead("no");
            lf.setLastModifiedTime(attributes.lastModifiedTime().toString());
            lf.setCreationTime(attributes.creationTime().toString());
            //populate collection
            logFiles.add(lf);
        }

        //continue file walk
        return FileVisitResult.CONTINUE;
    }

    private LogFile getLogFile(Path path, BasicFileAttributes attributes) {
        LogFile lf = new LogFile();
        lf.setName(path.getFileName().toString());
        lf.setPath(path.toAbsolutePath().toString());
        lf.setSize(attributes.size());
        lf.setType(attributes.isDirectory() ? 'd' : 'f');
        lf.setLastModifiedTime(attributes.lastModifiedTime().toString());
        lf.setCreationTime(attributes.creationTime().toString());
        return lf;
    }
}

package com.tomologic.logs.service;

import com.tomologic.logs.model.FileData;
import com.tomologic.logs.model.LogFile;

import java.util.List;

public interface PublishLog {

    public List<LogFile> listLogFiles(String path);
    public FileData getContent(String path);
    public List<LogFile> listFilteredLogFiles(String path, String filter);
}

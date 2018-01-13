package com.tomologic.logs.controller;

import com.tomologic.logs.config.Config;
import com.tomologic.logs.model.FileData;
import com.tomologic.logs.model.LogFile;
import com.tomologic.logs.service.PublishLog;
import com.tomologic.logs.service.impl.PublishLogImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *Defines URL mapping to the controllers
 */

@RestController
@RequestMapping("/logs")
public class LogController {
    private Logger log = LoggerFactory.getLogger(LogController.class);

    private final PublishLog publishLog;

    @Autowired
    public LogController(PublishLog publishLog) {
        this.publishLog = publishLog;
    }

    @GetMapping(value = "/content", produces = MediaType.APPLICATION_JSON_VALUE)
    public FileData getFileContent(@RequestParam("path") String path) {
        log.debug("Trying to get content for file '" + path + "'");
        log.debug("Content of file '" + path + "' retrieved");
        return publishLog.getContent(path);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public List<LogFile> listFiles(@RequestParam(value = "path", required = false) String path) {
        log.debug("Trying to get list of log files");
        PublishLog p = new PublishLogImpl(Config.USER_ROLE);
        log.debug("List of log files under '" + path + "' retrieved");
        return p.listLogFiles(path);
    }

    @GetMapping(value = "/list/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public  List<LogFile> listFilteredFiles( @RequestParam("path") String path, @RequestParam("filter") String filter ) {
        PublishLog p = new PublishLogImpl(Config.USER_ROLE);
        log.debug("List of log files under '" + path + "' retrieved");
        return p.listFilteredLogFiles(path, filter);
    }
}

package com.tomologic.logs.model;

/**
 * This is model for content for the given file, it contains file name and it's content
 */
public class FileData {

    String name;
    String contents;

    public FileData() {
    }

    public FileData(String name, String contents) {
        this.name = name;
        this.contents = contents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

}
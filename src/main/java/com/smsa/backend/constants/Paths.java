package com.smsa.backend.constants;

public enum Paths {
    SRC_FILE_PATH("src/main/resources/static/sample.xlsx"),
    DEST_FILE_PATH("src/main/resources/static/invoice.xlsx"); // Note the comma here

    private final String path;

    Paths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

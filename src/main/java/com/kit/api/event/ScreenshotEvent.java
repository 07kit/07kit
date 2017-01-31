package com.kit.api.event;

import java.io.File;

/**
 */
public class ScreenshotEvent {
    private final File file;
    private final String name;
    private final String description;

    public ScreenshotEvent(File file, String name, String description) {
        this.file = file;
        this.name = name;
        this.description = description;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

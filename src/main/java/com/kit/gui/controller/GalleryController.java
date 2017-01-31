package com.kit.gui.controller;

import com.google.common.reflect.TypeToken;
import com.kit.gui.Controller;
import com.kit.gui.view.GalleryView;
import com.kit.api.Screen;
import com.kit.core.model.Container;
import com.kit.gui.Controller;
import com.kit.gui.ControllerManager;
import com.kit.gui.view.GalleryView;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class GalleryController extends Controller<GalleryView> {
    private final GalleryView view = new GalleryView(this);
    private JFrame frame;

    public GalleryController() {
        ControllerManager.add(GalleryController.class, this);
    }

    @Override
    public GalleryView getComponent() {
        return view;
    }

    public void show() {
        view.reload();
        frame = new JFrame("Gallery");
        frame.add(getComponent());
        frame.pack();
        frame.setVisible(true);
    }

    public List<Screenshot> getScreenshots() {
        Path screenshotDir = Paths.get(System.getProperty("user.home"))
                .resolve("07kit")
                .resolve("Screenshots");
        List<Screenshot> screenshots = extractScreenshots(screenshotDir);
        Collections.reverse(screenshots);
        return screenshots;
    }

    private List<Screenshot> extractScreenshots(Path dir) {
        List<Screenshot> screenshots = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir)) {
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) {
                    screenshots.addAll(extractScreenshots(path));
                } else if (path.getFileName().toString().endsWith(".png")) {
                    Screenshot screenshot = new Screenshot();
                    screenshot.setName(getName(path.getFileName().toString()));
                    screenshot.setDescription(getDescription(path.getFileName().toString()));

                    String characterName = dir.getFileName().toString();
                    if (characterName.equalsIgnoreCase("screenshots")) {
                        characterName = "-";
                    }
                    screenshot.setCharacterName(characterName);
                    screenshot.setFile(path.toFile());
                    screenshots.add(screenshot);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return screenshots;
    }

    private String getName(String fileName) {
        if (fileName.contains("level_up")) {
            return "Level up";
        }
        return "Screenshot";
    }

    private String getDescription(String fileName) {
        if (fileName.contains("level_up")) {
            return fileName.replaceAll("_", " ");
        }
        return "Screenshot";
    }

    public static class Screenshot {
        private String name;
        private String description;
        private String characterName;
        private File file;

        public Screenshot(String name, String description, String characterName, File file) {
            this.name = name;
            this.description = description;
            this.characterName = characterName;
            this.file = file;
        }

        public Screenshot() {

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCharacterName() {
            return characterName;
        }

        public void setCharacterName(String characterName) {
            this.characterName = characterName;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }
    }
}

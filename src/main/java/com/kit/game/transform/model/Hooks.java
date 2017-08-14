package com.kit.game.transform.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.kit.api.util.GsonFactory;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

public class Hooks {
    public static final String DEFAULT_URL = "https://github.com/07kit/07kit/blob/master/hooks.json";
    public static final String DEFAULT_PATH = "./hooks.json";
    private static Hooks hooks;

    @SerializedName("revision")
    public int revision;
    @SerializedName("classDefinitions")
    public List<ClassDefinition> classDefinitions;

    public static boolean loadFromFile(Path hookPath) {
        final Logger logger = Logger.getLogger(Hooks.class);
        try {
            logger.info(String.format("Loading hooks from file [%s]", hookPath.toAbsolutePath()));
            Gson gson = new GsonFactory().newInstance();
            hooks = gson.fromJson(new FileReader(hookPath.toFile()), Hooks.class);
            return hooks != null;
        } catch (Throwable t) {
            logger.error(String.format("Unable to load hooks from file [%s]", hookPath.toAbsolutePath()), t);
            return false;
        }
    }

    public static boolean loadFromUrl(String url) {
        final Logger logger = Logger.getLogger(Hooks.class);
        try {
            try (InputStream in = new URL(url).openStream()) {
                Gson gson = new GsonFactory().newInstance();
                hooks = gson.fromJson(new InputStreamReader(in), Hooks.class);
                return hooks != null;
            }
        } catch (Throwable t) {
            logger.error(String.format("Unable to load hooks from url [%s]", url), t);
            return false;
        }
    }

    public static Hooks getHooks() {
        return hooks;
    }
}

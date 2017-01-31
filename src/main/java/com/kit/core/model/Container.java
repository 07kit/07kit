package com.kit.core.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 */
public abstract class Container<T> {
    public static final Gson GSON = new Gson();
    private static Logger logger = Logger.getLogger(Container.class);
    private List<T> elements = new ArrayList<>();
    private final String fileName;

    public Container(String fileName) {
        this.fileName = System.getProperty("user.home") + "/07Kit/Settings/" + fileName + ".json";
        load();
    }

    public abstract Type getElementsType();

    public void load() {
        try {
            File file = new File(fileName);

            logger.debug("Loading container: " + file.getAbsolutePath());
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new RuntimeException("Couldn't create container file.");
                }
                save();
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String json = "";
            String buf = "";
            while ((buf = reader.readLine()) != null) {
                json += buf + "\n";
            }
            Type listType = getElementsType();
            elements = GSON.fromJson(json, listType);
            reader.close();
            logger.debug("Loaded " + elements.size() + " entries.");
        } catch (Exception e) {
            throw new RuntimeException("Couldn't open container!", e);
        }
    }

    public void save() {
        try {
            File file = new File(fileName);

            logger.info("Loading container: " + file.getAbsolutePath());
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new RuntimeException("Couldn't create container file.");
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            PrintStream out = new PrintStream(fileOutputStream);
            out.print(GSON.toJson(elements));
            out.flush();
            out.close();
            fileOutputStream.close();
            logger.info("Loaded " + elements.size() + " entries.");
        } catch (Exception e) {
            throw new RuntimeException("Couldn't open container!", e);
        }
    }

    public void add(T element) {
        elements.add(element);
    }

    public void remove(T element) {
        elements.remove(element);
    }

    public boolean contains(T element) {
        return elements.contains(element);
    }

    public List<T> getAll() {
        return elements;
    }

    static {
        File file = new File(System.getProperty("user.home") + "/07Kit/Settings/");
        file.mkdirs();
    }

    public interface Serializer<T> {

        List<T> deserialize(Gson gson, String json);

    }
}
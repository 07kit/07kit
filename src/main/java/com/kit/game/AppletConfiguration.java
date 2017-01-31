package com.kit.game;

import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for applet configuration data.
 * This data is the same as the data used by
 * the official RuneScape desktop client.
 *
 */
public final class AppletConfiguration {
    private static final String CONFIG_URL = "http://oldschool.runescape.com/jav_config.ws";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_" +
            "9_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36";

    private final Map<String, String> parameters;
    private String documentBase;
    private String archiveName;
    private String mainClass;

    public AppletConfiguration() {
        parameters = new HashMap<>();
    }

    public String getDocumentBase() {
        return documentBase;
    }

    public void setDocumentBase(String documentBase) {
        this.documentBase = documentBase;
    }

    public String getArchiveName() {
        return archiveName;
    }

    public void setArchiveName(String archiveName) {
        this.archiveName = archiveName;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * Loads the configuration from the RuneScape website.
     */
    public void load() throws IOException {
        parameters.clear();
        String text = normalize(Request.Get(CONFIG_URL)
                .version(HttpVersion.HTTP_1_1)
                .userAgent(USER_AGENT)
                .useExpectContinue()
                .execute()
                .returnContent().toString());
        String[] lines = text.split("\n");
        for (String line : lines) {
            extractKVPairInto(parameters, line);
        }

        String mainClass = parameters.get("initial_class");
        setMainClass(mainClass.replace(".class", ""));
        setDocumentBase(parameters.get("codebase"));
        setArchiveName(parameters.get("initial_jar"));
    }

    /**
     * Removes some param type identifiers from a string.
     *
     * @param string string to normalize
     * @return normalized string.
     */
    private static String normalize(String string) {
        return string.replaceAll("param=", "")
                .replaceAll("msg=", "");
    }

    /**
     * Extracts a Key-Value pair from a string
     * and puts it into the given map.
     *
     * @param into  Map to put the pair into
     * @param input String to extract the pair from
     */
    private static void extractKVPairInto(Map<String, String> into, String input) {
        int len = input.length();
        int idx = input.indexOf('=');
        into.put(input.substring(0, idx),
                input.substring(idx + 1, len));
    }
}

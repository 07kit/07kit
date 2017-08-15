package com.kit.api.service;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.kit.api.wrappers.Entity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import com.kit.api.util.GsonFactory;
import com.kit.core.Session;
import com.kit.util.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 */
public final class SocialService {
    private static final String HOST = "https://api.07kit.com";
    private static final String CHARACTERS_ENDPOINT = HOST + "/characters";
    private static final String CHARACTER_ENDPOINT = HOST + "/characters/%d";
    private static final String CHARACTER_EVENT_ENDPOINT = HOST + "/characters/%d/events";
    private static final String CHARACTER_SCREENSHOT_ENDPOINT = HOST + "/characters/%d/screenshots";
    private final ThreadGroup socialThreadGroup = new ThreadGroup("SocialService");
    private final Gson gson = new GsonFactory().newInstance();


    public SocialService() {
    }

    public UserCharacter getCharacter(String hash) throws IOException {
        HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(Request.Get(CHARACTERS_ENDPOINT + "?hash=" + hash)
                .addHeader("Authorization", "Bearer " + Session.get().getApiToken())
        ).returnResponse();
        if (response.getStatusLine().getStatusCode() == 200) {
            return gson.fromJson(EntityUtils.toString(response.getEntity()), UserCharacter.class);
        } else if (response.getStatusLine().getStatusCode() == 404) {
            return null;
        } else {
            throw new IllegalStateException(String.format("Couldn't retrieve character for hash %s: %d", hash, response.getStatusLine().getStatusCode()));
        }
    }

    public UserCharacter createCharacter(String name, String hash) throws IOException {
        HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(Request.Post(CHARACTERS_ENDPOINT)
                .addHeader("Authorization", "Bearer " + Session.get().getApiToken())
                .bodyString(gson.toJson(new UserCharacter(name, hash)), ContentType.APPLICATION_JSON)
        ).returnResponse();
        if (response.getStatusLine().getStatusCode() == 200) {
            return gson.fromJson(EntityUtils.toString(response.getEntity()), UserCharacter.class);
        } else {
            throw new IllegalStateException(String.format("Couldn't create character for hash %s: %d", hash, response.getStatusLine().getStatusCode()));
        }
    }

    public UserCharacter updateCharacter(UserCharacter character) throws IOException {
        HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(Request.Put(String.format(CHARACTER_ENDPOINT, character.id))
                .addHeader("Authorization", "Bearer " + Session.get().getApiToken())
                .bodyString(gson.toJson(character), ContentType.APPLICATION_JSON)
        ).returnResponse();
        if (response.getStatusLine().getStatusCode() == 200) {
            return gson.fromJson(EntityUtils.toString(response.getEntity()), UserCharacter.class);
        } else {
            throw new IllegalStateException(String.format("Couldn't update character for id %d: %d", character.id, response.getStatusLine().getStatusCode()));
        }
    }

    public void uploadScreenshot(ScreenshotForm form, Consumer<Boolean> callback) {
        uploadScreenshotEvent(new EventForm(EventForm.EventType.POST, 0, "Screenshot uploaded", "-"), form, callback);
    }

    public void uploadScreenshotEvent(EventForm eventForm, ScreenshotForm form, Consumer<Boolean> callback) {
        HttpEntity entity = MultipartEntityBuilder
                .create()
                .addTextBody("name", form.name)
                .addTextBody("description", form.description)
                .addTextBody("locationX", String.valueOf(form.locationX))
                .addTextBody("locationY", String.valueOf(form.locationY))
                .addTextBody("created", String.valueOf(form.created.getTime()))
                .addBinaryBody("file", form.file, ContentType.create("image/png"), form.file.getName())
                .build();


        new Thread(socialThreadGroup, () -> {
            try {
                HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(Request.Post(String.format(CHARACTER_SCREENSHOT_ENDPOINT, Session.get().getCharacter().id))
                        .addHeader("Authorization", "Bearer " + Session.get().getApiToken())
                        .body(entity)).returnResponse();
                if (response.getStatusLine().getStatusCode() == 200) {
                    Map responseBody = gson.fromJson(EntityUtils.toString(response.getEntity()), Map.class);
                    eventForm.screenshotId = ((Number) responseBody.get("id")).longValue();
                    createEvent(eventForm, callback);
                } else {
                    callback.accept(false);
                }
            } catch (Exception e) {
                callback.accept(false);
                e.printStackTrace();
            }
        }, String.format("screenshot-upload-%s", form.file.getName())).start();
    }

    public void createEvent(EventForm form, Consumer<Boolean> callback) {
        new Thread(socialThreadGroup, () -> {
            try {
                HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(Request.Post(String.format(CHARACTER_EVENT_ENDPOINT, Session.get().getCharacter().id))
                        .addHeader("Authorization", "Bearer " + Session.get().getApiToken())
                        .bodyString(gson.toJson(form), ContentType.APPLICATION_JSON)).returnResponse();
                if (response.getStatusLine().getStatusCode() == 200) {
                    callback.accept(true);
                } else {
                    callback.accept(false);
                    throw new IllegalStateException(String.format("Couldn't log event. %d", response.getStatusLine().getStatusCode()));
                }
            } catch (IOException e) {
                e.printStackTrace();
                callback.accept(false);
            }
        }, "event-upload-" + form.caption).start();
    }

    public static class UserCharacter {
        @SerializedName("id")
        public long id;
        @SerializedName("name")
        public String name;
        @SerializedName("hash")
        public String hash;

        public UserCharacter(String name, String hash) {
            this.name = name;
            this.hash = hash;
        }

        public UserCharacter() {
        }
    }

    public static class EventForm {
        public enum EventType {
            POST, LEVEL_UP, ACHIEVEMENT
        }

        @SerializedName("type")
        public EventType type;
        @SerializedName("screenshotId")
        public long screenshotId;
        @SerializedName("caption")
        public String caption;
        @SerializedName("description")
        public String description;

        public EventForm(EventType type, long screenshotId, String caption, String description) {
            this.type = type;
            this.screenshotId = screenshotId;
            this.caption = caption;
            this.description = description;
        }

        public EventForm() {
        }
    }

    public static class ScreenshotForm {
        @SerializedName("file")
        private final File file;
        @SerializedName("name")
        private final String name;
        @SerializedName("description")
        private final String description;
        @SerializedName("locationX")
        private final int locationX;
        @SerializedName("locationY")
        private final int locationY;
        @SerializedName("created")
        private final Timestamp created;

        public ScreenshotForm(File file, String name, String description, int locationX, int locationY, Timestamp created) {
            this.file = file;
            this.name = name;
            this.description = description;
            this.locationX = locationX;
            this.locationY = locationY;
            this.created = created;
        }
    }

}

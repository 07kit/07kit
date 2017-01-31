package com.kit.plugins.twitch;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PropertyChangedEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.control.PluginManager;
import com.kit.util.HttpUtil;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TwitchChatPlugin extends Plugin {

    public static final Gson GSON = new Gson();

    public static final int MAX_CONNECTION_ATTEMPTS = 10;

    public static final String OAUTH_USER_INFO_URL = "https://api.twitch.tv/kraken?oauth_token=";
    public static final String IRC_HOST = "irc.chat.twitch.tv";

    @Option(label = "<html>Twitch OAuth token, get yours <a href=\"https://twitchapps.com/tmi/\">here</a><html>", value = "", type = Option.Type.TEXT)
    private String twitchOAuthToken;

    private TwitchUserInfo userInfo;

    private String lastOAuthToken;

    //TODO
//	Be able to add to stream list in social stream (maybe a right click menu?)

    private PircBotX bot;

    public TwitchChatPlugin(PluginManager manager) {
        super(manager);
    }

    private PircBotX createBot(int attempt) {
        try {
            lastOAuthToken = twitchOAuthToken;
            Request request = Request.Get(OAUTH_USER_INFO_URL + twitchOAuthToken.substring(twitchOAuthToken.contains(":") ?
                    twitchOAuthToken.indexOf(':') + 1 : 0));

            HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(request).returnResponse();
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    byte[] bytes = EntityUtils.toByteArray(response.getEntity());
                    userInfo = GSON.fromJson(new String(bytes), TwitchUserInfo.class);
                }
            }

            if (userInfo != null && userInfo.isIdentified()) {
                String channelToJoin = "#" + userInfo.getToken().getUsername();
                Configuration configuration = new Configuration.Builder()
                        .setName(userInfo.getToken().getUsername())
                        .setServerPassword(twitchOAuthToken)
                        .addServer(IRC_HOST)
                        .addAutoJoinChannel(channelToJoin)
                        .addListener(new TwitchChatMessageHandler())
                        .buildConfiguration();

                PircBotX bot = new PircBotX(configuration);
                bot.startBot();
                logger.info("Connecting to Twitch Chat for " + userInfo.getToken().getUsername());
            } else {
                NotificationsUtil.showNotification("Error", "Unable to start Twitch Chat Client, OAuth Token is invalid.");
            }

        } catch (IOException | IrcException e) {//TODO report failure back to user better
            logger.error("Error starting up Twitch Chat Client", e);
            if (attempt < MAX_CONNECTION_ATTEMPTS) {
                NotificationsUtil.showNotification("Error", "Unable to start Twitch Chat Client, retrying...");
                int next = attempt + 1;
                return createBot(next);
            } else {
                NotificationsUtil.showNotification("Error", "Unable to start Twitch Chat Client, giving up.");
            }
        }

        return bot;
    }

    @Override
    public String getName() {
        return "Twitch Chat";
    }

    @Override
    public void start() {
        if (twitchOAuthToken != null &&
                twitchOAuthToken.length() > 0) {
            bot = createBot(0);
        }
    }

    @EventHandler
    public void onPropertyChange(PropertyChangedEvent event) {
        if (twitchOAuthToken.equals(lastOAuthToken)) {
            return;
        }

        if (bot != null) {
            bot.close();
        }
        if (twitchOAuthToken != null &&
                twitchOAuthToken.length() > 0) {
            bot = createBot(0);
        }
    }

    @Override
    public void stop() {
        if (bot != null) {
            bot.close();
        }
    }
}

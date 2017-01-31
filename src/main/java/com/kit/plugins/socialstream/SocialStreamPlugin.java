package com.kit.plugins.socialstream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Schedule;
import com.kit.core.Session;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.jdesktop.swingworker.SwingWorker;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NotificationsUtil;
import com.kit.api.wrappers.PriceInfo;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.http.TwitchStream;
import com.kit.util.HttpUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SocialStreamPlugin extends Plugin {

    public static final Gson GSON = new Gson();
    public static final Type LIST_TYPE = new com.google.common.reflect.TypeToken<ArrayList<TwitchStream>>() {
    }.getType();

    public static final String API_URL = "https://api.07kit.com/socialstream/twitch/";

    public static final String AUTH_HEADER_KEY = "Authorization";

    private final SocialStreamSidebarWidget widget;
    private boolean loading = false;

    @Option(label = "Max number of entries to show in SocialStream", value = "50", type = Option.Type.NUMBER)
    private int entriesToShow;

    public SocialStreamPlugin(PluginManager manager) {
        super(manager);
        widget = new SocialStreamSidebarWidget();
    }

    @Schedule(120000)
    public void redrawSidebar() {
        if (loading) {
            return;
        }
        SocialStreamPlugin self = this;
        try {
            (new SwingWorker<List<StreamWidget>, Void>() {
                @Override
                protected void done() {
                    try {
                        List<StreamWidget> widgets = get();
                        if (widgets == null) {
                            return;
                        }
                        widget.createContainer(widgets);
                    } catch (InterruptedException | ExecutionException e1) {
                        logger.error("Error loading social stream", e1);
                        NotificationsUtil.showNotification("Social", "Error loading social stream");
                        loading = false;
                    }
                }

                @Override
                protected List<StreamWidget> doInBackground() throws Exception {
                    loading = true;
                    logger.info("Updating social stream");
                    Request request = Request.Get(API_URL + entriesToShow);
                    request.addHeader(AUTH_HEADER_KEY, "Bearer " + Session.get().getApiToken());

                    HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(request).returnResponse();
                    if (response != null) {
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            byte[] bytes = EntityUtils.toByteArray(response.getEntity());
                            List<TwitchStream> streams = GSON.fromJson(new String(bytes), LIST_TYPE);
                            return streams.stream().map(t -> new StreamWidget(t, self))
                                    .collect(Collectors.toList());
                        }
                    }
                    return null;
                }
            }).execute();
        } catch (Exception e) {
            logger.error("Error occurred getting social stream data.", e);
        } finally {
            loading = false;
        }
    }

    @Override
    public String getName() {
        return "Social";
    }

    @Override
    public void start() {
        redrawSidebar();
        ui.registerSidebarWidget(widget);
    }

    @Override
    public void stop() {
        ui.deregisterSidebarWidget(widget);
    }

    @Override
    public boolean hasOptions() {
        return true;
    }

    public SocialStreamSidebarWidget getWidget() {
        return widget;
    }
}

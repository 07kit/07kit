package com.kit.core;

import com.google.common.hash.Hashing;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.kit.Application;
import com.kit.api.debug.AbstractDebug;
import com.kit.api.debug.AutoEnable;
import com.kit.api.event.*;
import com.kit.api.service.CacheService;
import com.kit.api.service.SocialService;
import com.kit.api.util.Utilities;
import com.kit.core.control.*;
import com.kit.core.event.EnvironmentChangedEvent;
import com.kit.core.model.Property;
import com.kit.game.engine.IClient;
import com.kit.api.MethodContext;
import com.kit.game.AppletLoader;
import com.kit.gui.view.AppletView;
import com.kit.http.UserAccount;
import com.kit.jfx.Frame;
import org.apache.log4j.Logger;
import com.kit.api.event.Events;
import com.kit.core.control.DebugManager;
import com.kit.socket.ClientService;

import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static com.google.common.collect.Maps.newHashMap;

/**
 * State mechanism for a botting session.
 */
public final class Session extends MethodContext {

    private static Logger logger = Logger.getLogger(Session.class);
    private static Session session;
    private final Events eventBus = new Events();
    private final ServiceManager serviceManager;
    private final CommunityManager communityManager;
    private final OverlayManager overlayManager;
    private final DebugManager debugManager;
    private final PluginManager pluginManager;
    private final AppletLoader loader;
    private Frame frame = null;
    private final AppletView appletView;
    private State state;
    private long startTime;
    private boolean render = true;
    private boolean rememberUsername;
    private Property rememberedUsername;
    private Property email;
    private Property apiKey;
    private UserAccount userAccount;
    private ClientService socketClient;
    //private IClient client;

    public static final String USERNAME_PROPERTY_KEY = "rememberedUsername";
    public static final String EMAIL_PROPERTY_KEY = "email";
    public static final String API_KEY_PROPERTY_KEY = "apiKey";
    private SocialService.UserCharacter character;
    private String apiToken;

    static {
        session = new Session();
    }

    /**
     * Constructor
     */
    private Session() {
        eventBus.register(this);
        startTime = System.currentTimeMillis();
        this.communityManager = new CommunityManager(this);
        this.overlayManager = new OverlayManager(this);
        this.debugManager = new DebugManager(this);
        this.pluginManager = new PluginManager(this);
        this.loader = new AppletLoader(this);
        this.appletView = new AppletView();
        this.rememberedUsername = Property.get(USERNAME_PROPERTY_KEY);
        this.email = Property.get(EMAIL_PROPERTY_KEY);
        this.apiKey = Property.get(API_KEY_PROPERTY_KEY);
        this.rememberUsername = rememberedUsername != null;
        if (apiKey != null) {
            apiToken = apiKey.getValue();
        }

        this.socketClient = new ClientService();
        this.serviceManager = new ServiceManager(
                Arrays.asList(socketClient));
    }

    public static Session get() {
        return session;
    }

    public boolean isRememberUsername() {
        return rememberUsername;
    }

    public void setRememberUsername(boolean rememberUsername) {
        this.rememberUsername = rememberUsername;
    }

    public Property getRememberedUsername() {
        return rememberedUsername;
    }

    public void setRememberedUsername(Property rememberedUsername) {
        this.rememberedUsername = rememberedUsername;
    }

    public int getAliveTime() {
        return (int) (System.currentTimeMillis() - startTime);
    }

    public Property getApiKey() {
        return apiKey;
    }

    public Property getEmail() {
        return email;
    }

    public ClientService getSocketClient() {
        return socketClient;
    }

    @EventHandler
    public void onEnvironmentChanged(EnvironmentChangedEvent event) {

    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        SocialService socialService = new SocialService();
        String loginHash = Hashing.md5().hashString(client().getUsername(), Charset.defaultCharset()).toString();
        try {
            character = socialService.getCharacter(loginHash);
            if (character == null) { // Doesn't exist :-(
                character = socialService.createCharacter(player.getName(), loginHash);
            }

            Session.get().client().setRights(2);
            character.name = player.getName();
            socialService.updateCharacter(character);
        } catch (Throwable e) {
            logger.error(String.format("Failed to retrieve character for hash %s", loginHash), e);
        }
    }

    public void onAppletLoaded() {
        setState(State.ACTIVE);

        new Thread(() -> {
            Utilities.sleepUntil(() -> client().getLoginIndex() >= 10, 300000);
            try {
                CacheService.get().init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        /**
         * A bit of a hack, but yeah..
         */
        for (AbstractDebug debug : debugManager.getDebugs()) {
            if (debug.getClass().getAnnotation(AutoEnable.class) != null) {
                debug.setEnabled(true);
            }
        }
    }

    /**
     * Gets the overlay manager which is responsible for rendering plugin overlays onto the RS canvas.
     *
     * @return overlay manager
     */
    public OverlayManager getOverlayManager() {
        return overlayManager;
    }

    /**
     * Gets the debug manager, used for enabling and disabling
     * various debugging features of the 07Kit client.
     *
     * @return debug manager
     */
    public DebugManager getDebugManager() {
        return debugManager;
    }

    /**
     * Gets the script manager, used for starting and stopping scripts
     * and managing their general state as well as their spawned threads.
     *
     * @return script manager
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    /**
     * Gets the applet loader
     *
     * @return applet loader
     */
    public AppletLoader getAppletLoader() {
        return loader;
    }

    /**
     * Gets the game client
     *
     * @return client
     */
    public IClient getClient() {
        return (IClient) loader.getApplet();
    }

    /**
     * Short-hand for getting the event bus from the environment
     *
     * @return event bus.
     */
    public Events getEventBus() {
        return eventBus;
    }

    /**
     * Gets the current session state
     *
     * @return state
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the session state
     *
     * @param state new state.
     */
    public void setState(State state) {
        this.state = state;
        if (state == State.ACTIVE) {
            logger.info("Applet ready - starting plugin manager.");
            pluginManager.start();
        }
        eventBus.submit(new StateChangeEvent(state));
    }

    /**
     * Destroys the session
     */
    public void destroy() {
        pluginManager.stop();
        getAppletLoader().getApplet().stop();
        getAppletLoader().getApplet().destroy();
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getApiToken() {
        return apiToken;
    }

    public SocialService.UserCharacter getCharacter() {
        return character;
    }

    public void onAuthenticated() {
        serviceManager.addListener(new ServiceManager.Listener() {
            @Override
            public void healthy() {
                logger.info("Services started.");
            }

            @Override
            public void stopped() {
                logger.info("Services stopped.");
            }

            @Override
            public void failure(Service service) {
                logger.error(String.format("Service [%s] failed.", service.getClass().getName()),
                        service.failureCause());

            }
        });
        serviceManager.startAsync();
    }

    /**
     * Enum representing possible session states
     */
    public enum State {
        ACTIVE, PAUSED, STOPPED
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Frame getFrame() {
        if (frame == null)
            frame = new Frame();
        return frame;
    }

    public AppletView getAppletView() {
        return appletView;
    }
}

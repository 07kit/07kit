package com.kit.core.control;

import com.kit.Application;
import com.kit.api.event.ClientLoopEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.event.Events;
import com.kit.api.plugin.Plugin;
import com.kit.core.Session;
import com.kit.http.UserAccount;
import com.kit.plugins.*;
import com.kit.plugins.afk.AFKMentionNotifierPlugin;
import com.kit.plugins.afk.AFKWatcherPlugin;
import com.kit.plugins.clan.ClanPlugin;
import com.kit.plugins.cluescrolls.ClueScrollPlugin;
import com.kit.plugins.combat.*;
import com.kit.plugins.debugger.DebugPlugin;
import com.kit.plugins.grandexchange.BankValuatorPlugin;
import com.kit.plugins.grandexchange.GrandExchangePlugin;
import com.kit.plugins.grandexchange.ItemExaminePricePlugin;
import com.kit.plugins.map.WorldMapPlugin;
import com.kit.plugins.milestone.LevelUpCapturerPlugin;
import com.kit.plugins.notes.NotesPlugin;
import com.kit.plugins.quickchat.QuickChatPlugin;
import com.kit.plugins.quickhop.QuickHopPlugin;
import com.kit.plugins.skills.agility.AgilityOverlayPlugin;
import com.kit.plugins.skills.fishing.FishingPlugin;
import com.kit.plugins.skills.mining.MiningOverlayPlugin;
import com.kit.plugins.skills.runecrafting.RunecraftingOverlayPlugin;
import com.kit.plugins.skills.woodcutting.WoodcuttingOverlayPlugin;
import com.kit.plugins.socialstream.SocialStreamPlugin;
import com.kit.plugins.stats.PlayerStatsPlugin;
import com.kit.plugins.streamhelper.LootProfitAndDropRecorder;
import com.kit.plugins.twitch.TwitchChatPlugin;
import com.kit.plugins.wintertodt.WintertodtPlugin;
import com.kit.plugins.xptracker.XPTrackerPlugin;
import com.kit.api.event.ClientLoopEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.event.Events;
import com.kit.api.plugin.Plugin;
import com.kit.core.Session;
import com.kit.http.UserAccount;
import com.kit.plugins.afk.AFKMentionNotifierPlugin;
import com.kit.plugins.afk.AFKWatcherPlugin;
import com.kit.plugins.clan.ClanPlugin;
import com.kit.plugins.cluescrolls.ClueScrollPlugin;
import com.kit.plugins.grandexchange.BankValuatorPlugin;
import com.kit.plugins.grandexchange.ItemExaminePricePlugin;
import com.kit.plugins.notes.NotesPlugin;
import com.kit.plugins.skills.agility.AgilityOverlayPlugin;
import com.kit.plugins.debugger.DebugPlugin;
import com.kit.plugins.skills.fishing.FishingPlugin;
import com.kit.plugins.grandexchange.GrandExchangePlugin;
import com.kit.plugins.hiscore.HiscorePlugin;
import com.kit.plugins.map.WorldMapPlugin;
import com.kit.plugins.milestone.LevelUpCapturerPlugin;
import com.kit.plugins.skills.mining.MiningOverlayPlugin;
import com.kit.plugins.quickchat.QuickChatPlugin;
import com.kit.plugins.quickhop.QuickHopPlugin;
import com.kit.plugins.skills.runecrafting.RunecraftingOverlayPlugin;
import com.kit.plugins.socialstream.SocialStreamPlugin;
import com.kit.plugins.stats.PlayerStatsPlugin;
import com.kit.plugins.streamhelper.LootProfitAndDropRecorder;
import com.kit.plugins.twitch.TwitchChatPlugin;
import com.kit.plugins.skills.woodcutting.WoodcuttingOverlayPlugin;
import com.kit.plugins.wintertodt.WintertodtPlugin;
import com.kit.plugins.xptracker.XPTrackerPlugin;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * A script management class.
 *
 */
public final class PluginManager {
    private final CopyOnWriteArrayList<Plugin.SchedulableRunnable> scheduledRunnables = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Plugin> enabledPlugins = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService lifecycleManager = Executors.newSingleThreadScheduledExecutor();
    private final Logger logger = Logger.getLogger(PluginManager.class);

    private final List<Plugin> plugins = new ArrayList<>();
    private final Events eventBus;
    private final Session session;
    private boolean pluginsStarted;

    public PluginManager(Session session) {
        this.eventBus = session.getEventBus();
        this.session = session;
        eventBus.register(this);
    }

    public void startPlugin(Plugin plugin) {
        eventBus.register(plugin);
        plugin.start();
        scheduledRunnables.addAll(plugin.getRunnables());
        enabledPlugins.add(plugin);
    }

    public void stopPlugin(Plugin plugin) {
        eventBus.deregister(plugin);
        scheduledRunnables.removeAll(plugin.getRunnables());
        plugin.stop();
        enabledPlugins.remove(plugin);
    }

    public void start() {
        try {
            plugins.add(new SocialStreamPlugin(this));
//            plugins.add(new QuickHopPlugin(this));
            plugins.add(new HiscorePlugin(this));
            plugins.add(new GrandExchangePlugin(this));
            plugins.add(new XPTrackerPlugin(this));
            plugins.add(new NotesPlugin(this));

            if (Session.get().getUserAccount() != null &&
                    (Session.get().getUserAccount().getType() == UserAccount.Type.DEVELOPER ||
                            Application.devMode)) {
                plugins.add(new DebugPlugin(this));
            }

            plugins.add(new InventoryMarkerPlugin(this));

            plugins.add(new ClanPlugin(this));

            plugins.add(new LootOverlayPlugin(this));
            plugins.add(new CombatPlugin(this));
            plugins.add(new FishingPlugin(this));
            plugins.add(new RememberMeOverlayPlugin(this));
            plugins.add(new WoodcuttingOverlayPlugin(this));
            plugins.add(new MiningOverlayPlugin(this));
            plugins.add(new BoostsOverlayPlugin(this));
            plugins.add(new RunecraftingOverlayPlugin(this));
            plugins.add(new BankValuatorPlugin(this));
            plugins.add(new FirstActionOverlayPlugin(this));
            plugins.add(new FoodOverlayPlugin(this));
            plugins.add(new NpcMarkerOverlayPlugin(this));
            plugins.add(new AgilityOverlayPlugin(this));
            //plugins.add(new CannonOverlayPlugin(this));
            plugins.add(new NpcKillCounterOverlayPlugin(this));
            plugins.add(new KillCounterOverlayPlugin(this));
            plugins.add(new LootProfitAndDropRecorder(this));
            plugins.add(new TradeOverlayPlugin(this));
            plugins.add(new WintertodtPlugin(this));

            plugins.add(new LevelUpCapturerPlugin(this));
            plugins.add(new QuickChatPlugin(this));
            plugins.add(new ItemExaminePricePlugin(this));
            plugins.add(new FriendClanChatMinimapMarkerPlugin(this));
            plugins.add(new ClueScrollPlugin(this));

            plugins.add(new IdleNotifierPlugin(this));
            plugins.add(new LogoutNotifierPlugin(this));
            plugins.add(new AFKMentionNotifierPlugin(this));
            plugins.add(new TradeNotifierPlugin(this));

            plugins.add(new WorldMapPlugin(this));
            plugins.add(new DeathMarkerPlugin(this));

            plugins.add(new PlayerStatsPlugin(this));

            plugins.add(new TwitchChatPlugin(this));

            plugins.add(new AFKWatcherPlugin(this));

            lifecycleManager.scheduleAtFixedRate(this::updateLifecycle, 0, 500, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("Error creating plugins", e);
        }
    }

    public void stop() {
        plugins.forEach(this::stopPlugin);
    }

    @EventHandler
    public void onClientLoop(ClientLoopEvent event) {
        for (Plugin.SchedulableRunnable runnable : scheduledRunnables) {
            long timePast = System.currentTimeMillis() - runnable.getLastRan();
            if (timePast > runnable.getMillis()) {
                try {
                    runnable.run();
                } catch (Exception e) {
                    logger.error("Error executing runnable", e);
                }
            }
        }
    }

    private void updateLifecycle() {
        plugins.forEach(plugin -> {
            if (plugin.isEnabled() && !enabledPlugins.contains(plugin)) {
                try {
                    startPlugin(plugin);
                } catch (Exception e) {
                    logger.error("Error starting plugin", e);
                }
            } else if (!plugin.isEnabled() && enabledPlugins.contains(plugin)) {
                try {
                    stopPlugin(plugin);
                } catch (Exception e) {
                    logger.error("Error stopping plugin", e);
                }
            }
        });
        pluginsStarted = true;
    }

    public boolean isPluginsStarted() {
        return pluginsStarted;
    }

    public boolean isReady() {
        return session.getState() == Session.State.ACTIVE;
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }
}

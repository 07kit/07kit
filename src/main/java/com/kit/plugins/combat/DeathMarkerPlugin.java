package com.kit.plugins.combat;

import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Tile;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.plugins.map.WorldMapPlugin;
import com.kit.plugins.map.marker.DeathMarker;
import com.kit.plugins.quickchat.QuickChatPlugin;

import java.awt.*;

public class DeathMarkerPlugin extends Plugin {

    public static final int DEATH_ANIM_ID = 836;

    @Option(label = "Time to mark last death for (seconds)", value = "240", type = Option.Type.NUMBER)
    private int timeToMarkDeath;

    private WorldMapPlugin worldMap;
    private Tile lastDeathLocation;
    private long lastDeathTime;
    private DeathMarker lastDeathMarker;

    public DeathMarkerPlugin(PluginManager manager) {
        super(manager);
    }

    @EventHandler
    public void onDeath(MessageEvent event) {
        if (event.getMessage() != null && event.getMessage().contains("you are dead") &&
                event.getType() == MessageEvent.Type.MESSAGE_SERVER && lastDeathLocation != null) {
            if (lastDeathMarker != null) {
                worldMap.removeMarker(lastDeathMarker);
            }
            lastDeathMarker = new DeathMarker(worldMap, lastDeathLocation);
            worldMap.addMarker(lastDeathMarker);
            Session.get().game.sendChatboxMessage("<col=ff0000>Left a death marker where you died, check your map!</col>",
                    "", "", MessageEvent.Type.MESSAGE_SERVER_FILTERED);
        }
    }

    @Schedule(5000)
    public void cleanup() {
        if (lastDeathLocation != null && timeSinceDeath() > timeToMarkDeath * 1000) {
            if (lastDeathMarker != null) {
                worldMap.removeMarker(lastDeathMarker);
            }
            lastDeathMarker = null;
            lastDeathLocation = null;
            lastDeathTime = 0;
        }
    }

    @Schedule(100)
    public void checkIfDying() {
        if (!Session.get().isLoggedIn() || Session.get().player == null) {
            return;
        }

        if (Session.get().player.getAnimation() == DEATH_ANIM_ID && timeSinceDeath() > 5000) {
            lastDeathLocation = Session.get().player.getTile();
            lastDeathTime = System.currentTimeMillis();
        }
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        if (!Session.get().isLoggedIn() || lastDeathLocation == null) {
            return;
        }
        Point minimap = Session.get().minimap.convert(lastDeathLocation);
        if (minimap.x > -1 && minimap.y > -1) {
            Graphics2D g2d = (Graphics2D) event.getGraphics();
            g2d.drawImage(DeathMarker.DEATH_MARKER, minimap.x - (DeathMarker.DEATH_MARKER.getWidth() / 2),
                    minimap.y - (DeathMarker.DEATH_MARKER.getHeight() / 2), null);
            Font old = g2d.getFont();
            g2d.setFont(g2d.getFont().deriveFont(8f));
            PaintUtils.drawString(g2d, "You died here", minimap.x + DeathMarker.DEATH_MARKER.getWidth() / 2, minimap.y);
            g2d.setFont(old);
        }
    }

    private long timeSinceDeath() {
        return System.currentTimeMillis() - lastDeathTime;
    }

    @Override
    public String getName() {
        return "Death Marker";
    }

    @Override
    public void start() {
        worldMap = (WorldMapPlugin) getManager().getPlugins().stream().filter(p -> p.getClass().equals(WorldMapPlugin.class))
                .findFirst().get();
    }

    @Override
    public void stop() {

    }
}

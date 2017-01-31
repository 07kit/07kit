package com.kit.plugins.wintertodt;

import com.kit.api.event.MessageEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NotificationsUtil;
import com.kit.api.wrappers.GameObject;
import com.kit.api.wrappers.Tile;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Matt on 01/11/2016.
 */
public class WintertodtPlugin extends Plugin {

    private static final Color GREEN = new Color(0, 255, 0, 175);
    private static final Color RED = new Color(255, 0, 0, 175);
    private static final Color ORANGE = new Color(255, 128, 0, 175);
    private static final Color YELLOW = new Color(255, 255, 0, 175);

    private static final int BRAZIER_ID = 29314;
    private static final int UNLIT_BRAZIER_ID = 29312;
    private static final int BROKEN_BRAZIER_ID = 29313;

    private static final int INCAPACITATED_PYROMANCER_ID = 7372;

    private static final int SNOWFALL_ID = 26690;

    private static final int BRUMA_LOG_ID = 20695;

    private static final int FEEDING_BRAZIER_ID = 832;

    private List<Tile> snowfalls = new ArrayList<>();
    private List<Tile> unlitBraziers = new ArrayList<>();
    private List<Tile> brokenBraziers = new ArrayList<>();

    private boolean notifiedSnowfall = false;
    private boolean notifiedUnlit = false;
    private boolean notifiedBroken = false;
    private boolean feedingBrazier = false;

    public WintertodtPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Wintertodt";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Schedule(600)
    public void loop() {
        if (!Session.get().isLoggedIn())
            return;
        snowfalls = objects.find(SNOWFALL_ID).type(GameObject.GameObjectType.INTERACTABLE).onMinimap().asList().stream().map(GameObject::getTile).collect(Collectors.toList());
        unlitBraziers = objects.find(UNLIT_BRAZIER_ID).type(GameObject.GameObjectType.INTERACTABLE).onMinimap().asList().stream().map(GameObject::getTile).collect(Collectors.toList());
        brokenBraziers = objects.find(BROKEN_BRAZIER_ID).type(GameObject.GameObjectType.INTERACTABLE).onMinimap().asList().stream().map(GameObject::getTile).collect(Collectors.toList());

        if (player.getAnimation() == FEEDING_BRAZIER_ID)
            feedingBrazier = true;

        if (!inventory.contains(BRUMA_LOG_ID) && feedingBrazier)
            NotificationsUtil.showNotification("Wintertodt", "You've ran out of Bruma logs!");

        if (!inventory.contains(BRUMA_LOG_ID) || player.isMoving())
            feedingBrazier = false;



        if (!notifiedSnowfall) {
            for (Tile snowfall : snowfalls) {
                if (snowfall.equals(player.getTile())) {
                    NotificationsUtil.showNotification("Wintertodt", "You're about to be hit by snow!");
                    notifiedSnowfall = true;
                    break;
                }
            }
        } else if (snowfalls.size() == 0) {
            notifiedSnowfall = false;
        }


        if (unlitBraziers.size() > 0 && !notifiedUnlit) {
            NotificationsUtil.showNotification("Wintertodt", "A brazier needs lighting!");
            notifiedUnlit = true;
        } else if (unlitBraziers.size() == 0) {
            notifiedUnlit = false;
        }


        if (brokenBraziers.size() > 0 && !notifiedBroken) {
            NotificationsUtil.showNotification("Wintertodt", "A brazier has broken!");
            notifiedBroken = true;
        } else if (brokenBraziers.size() == 0) {
            notifiedBroken = false;
        }

    }

    @EventHandler
    public void onPaintEvent(PaintEvent event) {
        if (!Session.get().isLoggedIn())
            return;
        Graphics2D g = (Graphics2D) event.getGraphics().create();

        g.setColor(RED);
        for (Tile snowfall: snowfalls) {
            g.fill(snowfall.getPolygon());
        }

        g.setColor(ORANGE);
        for (Tile unlitBrazier: unlitBraziers) {
            g.fill(unlitBrazier.getPolygon());
        }

        g.setColor(YELLOW);
        for (Tile brokenBrazier: brokenBraziers) {
            g.fill(brokenBrazier.getPolygon());
        }
    }

    @EventHandler
    public void onMessage(MessageEvent event) {
        if (event.getMessage().contains("The cold of the Wintertodt seeps into your bones.") && feedingBrazier) {
            feedingBrazier = false;
            NotificationsUtil.showNotification("Wintertodt", "You've stopped feeding the brazier!");
        }
    }
}

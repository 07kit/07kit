package com.kit.plugins.skills.fishing;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.primitives.Ints;
import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Skill;
import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.event.PaintEvent;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.ColorUtils;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Npc;
import com.kit.api.wrappers.Skill;
import com.kit.core.control.PluginManager;
import com.kit.plugins.skills.mining.MiningOverlayPlugin;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class FishingPlugin extends Plugin {
    private final FishingBoxOverlay box = new FishingBoxOverlay(this);
    private Multimap<Spot, Npc> spots;
    private String[] actions;
    private int[] equipments;
    private long lastCatch;
    private long sampleStart;
    private int sampleFishCaught;
    private int totalFishCaught;
    private boolean active;

    @Option(label = "Floating overlay", value = "false", type = Option.Type.TOGGLE)
    private boolean floating;

    public FishingPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Fishing";
    }

    @Override
    public String getGroup() {
        return "Skills";
    }

    @Override
    public void start() {
        Set<String> actionSet = new HashSet<>();
        for (Spot spot : Spot.values()) {
            actionSet.addAll(Arrays.asList(spot.actions));
        }
        actions = actionSet.toArray(new String[actionSet.size()]);

        Set<Integer> equipmentSet = new HashSet<>();
        for (Equipment equipment : Equipment.values()) {
            equipmentSet.addAll(Ints.asList(equipment.ids));
        }
        equipments = Ints.toArray(equipmentSet);
        ui.registerBoxOverlay(box);
    }

    @Override
    public void stop() {
        ui.deregisterBoxOverlay(box);
    }

    @Schedule(600)
    public void getSpots() {
        if (System.currentTimeMillis() - lastCatch >= 300000) { // If we've caught nothing in the last 5 minutes reset the sample rate.
            sampleFishCaught = 0;
        }

        if (!isLoggedIn()) {
            logger.debug("Not logged in.");
            return;
        }


        List<Spot> validSpots = Arrays.asList(Spot.values()).stream().filter(key -> {
            if (skills.getBaseLevel(Skill.FISHING) < key.level) {
                return false;
            } else if (!inventory.contains(key.equipment.ids)) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());

        List<Npc> npcs = this.npcs.find()
                .hasAction(actions)
                .distance(20)
                .asList();
        spots = Multimaps.index(npcs, npc -> {
            for (Spot spot : validSpots) {
                Set<String> spotActions = new HashSet<>(Arrays.asList(spot.actions));
                Set<String> npcActions = new HashSet<>(Arrays.asList(npc.getComposite().getActions()));
                npcActions.remove(null);
                if (spotActions.equals(npcActions)) {
                    return spot;
                }
            }
            return Spot.UNKNOWN;
        });

        active = inventory.contains(equipments) || equipment.contains(equipments);
    }

    @EventHandler
    public void onMessage(MessageEvent event) {
        if (event.getMessage().contains("You catch")) {
            lastCatch = System.currentTimeMillis();
            totalFishCaught++;

            if (sampleFishCaught == 0) {
                sampleStart = System.currentTimeMillis();
            }
            sampleFishCaught++;
        }
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        if (spots == null || bank.isOpen()) {
            return;
        }

        Graphics2D g2d = (Graphics2D) event.getGraphics();
        for (Spot spot : spots.keySet()) {
            if (spot == Spot.UNKNOWN) continue;

            Collection<Npc> npcs = spots.get(spot);
            for (Npc npc : npcs) {
                Point basePoint = npc.getBasePoint();
                Point mapPoint = minimap.convert(npc.getTile());
                Polygon polygon = npc.getTile().getPolygon();

                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.fill(polygon);

                g2d.setColor(Application.COLOUR_SCHEME.getText());
                int idx = 0;
                for (String fish : spot.fish) {
                    PaintUtils.drawString(g2d, "\u2022 " + fish, basePoint.x, basePoint.y - (idx * 15));
                    idx++;
                }

                if (mapPoint.x != -1 && mapPoint.y != -1) {
                    g2d.setColor(Application.COLOUR_SCHEME.getText());
                    PaintUtils.drawString(g2d, "F", mapPoint.x, mapPoint.y);
                }
            }
        }
    }

    private class FishingBoxOverlay extends BoxOverlay {
        private final Color BACKGROUND_COLOR = ColorUtils.setOpacity(new Color(75, 67, 54, 255), 200);
        private final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 9);

        protected FishingBoxOverlay(Plugin owner) {
            super(owner);
            setFloating(floating);
        }

        @Override
        public void draw(Graphics2D gfx) {
            gfx.setColor(BACKGROUND_COLOR);
            gfx.fillRect(0, 0, getWidth(), getHeight());

            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.setFont(FONT);
            PaintUtils.drawString(gfx, "Fishing", 10, 15);
            gfx.drawLine(10, 20, getWidth() - 10, 20);

            gfx.setFont(FONT);
            int fishCaughtWidth = gfx.getFontMetrics().stringWidth("Fish caught:");
            PaintUtils.drawString(gfx, "Fish caught:", 10, 35);

            gfx.setFont(FONT.deriveFont(Font.PLAIN));
            PaintUtils.drawString(gfx, String.valueOf(totalFishCaught), fishCaughtWidth + 20, 35);

            gfx.setFont(FONT);
            int fishPerHourWidth = gfx.getFontMetrics().stringWidth("Fish/hr:");
            PaintUtils.drawString(gfx, "Fish/hr:", 10, 50);

            gfx.setFont(FONT.deriveFont(Font.PLAIN));
            if (sampleFishCaught > 0) {
                double timePerFish = (System.currentTimeMillis() - sampleStart) / sampleFishCaught;
                int fishPerHour = (int) (3600000D / timePerFish);
                PaintUtils.drawString(gfx, String.valueOf(fishPerHour), fishPerHourWidth + 20, 50);
            } else {
                PaintUtils.drawString(gfx, "N/A", fishPerHourWidth + 20, 50);
            }

            // Draw the box outline
            gfx.setColor(new Color(0, 0, 0));
            gfx.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }

        @Override
        public DockPosition getDockPosition() {
            return DockPosition.LEFT;
        }

        @Override
        public int getWidth() {
            return 120;
        }

        @Override
        public int getHeight() {
            return 70;
        }

        @Override
        public boolean isShowing() {
            return isLoggedIn() && !bank.isOpen() && active;
        }

        @Override
        public void setFloating(boolean floating) {
            FishingPlugin.this.floating = floating;
            FishingPlugin.this.persistOptions();
            super.setFloating(floating);
        }

    }
}

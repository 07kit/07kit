package com.kit.plugins.skills.woodcutting;

import com.google.common.primitives.Ints;
import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.event.SpawnGroundItemEvent;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NotificationsUtil;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Loot;
import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.event.PaintEvent;
import com.kit.api.event.SpawnGroundItemEvent;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.ColorUtils;
import com.kit.api.util.NotificationsUtil;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.GameObject;
import com.kit.api.wrappers.Loot;
import com.kit.core.control.PluginManager;
import com.kit.plugins.skills.mining.MiningOverlayPlugin;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
public class WoodcuttingOverlayPlugin extends Plugin {
    private static final int WOODCUTTING_ANIMATION = 2846;
    private static final int[] NEST_IDS = {
            5070, // red egg
            5071, // blue / green egg
            5072, // blue / green egg
            5073, // seed
            5074, // ring
            5075 // nothing
    };
    private static final int[] AXE_IDS = {
            1349, 1351, 1353, 1355, 1357, 1359, 1361, 6739
    };

    private final Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private final WoodcuttingBoxOverlay box = new WoodcuttingBoxOverlay(this);

    @Option(label = "Show normal trees", type = Option.Type.TOGGLE, value = "true")
    private boolean showTrees;
    @Option(label = "Show oak trees", type = Option.Type.TOGGLE, value = "true")
    private boolean showOaks;
    @Option(label = "Show willow trees", type = Option.Type.TOGGLE, value = "true")
    private boolean showWillows;
    @Option(label = "Show maple trees", type = Option.Type.TOGGLE, value = "true")
    private boolean showMaples;
    @Option(label = "Show yew trees", type = Option.Type.TOGGLE, value = "true")
    private boolean showYews;
    @Option(label = "Show magic trees", type = Option.Type.TOGGLE, value = "true")
    private boolean showMagics;
    @Option(label = "Draw trees on minimap", type = Option.Type.TOGGLE, value = "true")
    private boolean showOnMinimap;
    @Option(label = "Draw trees on screen", type = Option.Type.TOGGLE, value = "true")
    private boolean showOnScreen;
    @Option(label = "Draw nests on screen", type = Option.Type.TOGGLE, value = "true")
    private boolean showNestsOnScreen;
    @Option(label = "Notify when nest spawns", type = Option.Type.TOGGLE, value = "true")
    private boolean notifyOnNest;
    @Option(label = "Highlight nearest matching tree", type = Option.Type.TOGGLE, value = "true")
    private boolean highlightTree;
    @Option(label = "Floating overlay", value = "false", type = Option.Type.TOGGLE)
    private boolean floating;
    private List<GameObject> trees;
    private List<Loot> nests;
    private List<String> filters;
    private long lastYield;
    private long sampleStart;
    private int sampleLogsChopped;
    private int logsChopped = 0;

    private float opacityMultiplier = 0.01f;
    private boolean opacityUp = true;
    private int opacityRate = 0;

    private boolean active;

    public WoodcuttingOverlayPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Woodcutting";
    }

    @Override
    public String getGroup() {
        return "Skills";
    }

    @Override
    public void start() {
        ui.registerBoxOverlay(box);
    }

    @Override
    public void stop() {
        ui.deregisterBoxOverlay(box);
    }

    @Schedule(1000)
    public void checkState() {
        if (System.currentTimeMillis() - lastYield >= 300000) { // If we've chopped nothing in the last 5 minutes reset the sample rate.
            sampleLogsChopped = 0;
        }

        if (!isLoggedIn()) {
            return;
        }

        active = inventory.contains(AXE_IDS) || equipment.contains(AXE_IDS);
        if (active) {
            trees = objects.find()
                    .type(GameObject.GameObjectType.INTERACTABLE)
                    .named(filters.toArray(new String[0]))
                    .hasAction("Chop down")
                    .distance(17)
                    .asList()
                    .stream()
                    .sorted(Comparator.comparingInt(x -> x.distanceTo(player)))
                    .collect(Collectors.toList());
            nests = loot.find(NEST_IDS)
                    .distance(20)
                    .asList();
        }
    }

    @EventHandler
    public void onPaint(PaintEvent e) {
        if (!isLoggedIn() || trees == null || !active || bank.isOpen()) {
            return;
        }

        Graphics2D g = (Graphics2D) e.getGraphics().create();
        int idx = 0;
        for (GameObject tree : trees) {
            if (tree.unwrap() == null) {
                continue;
            }

            if (showOnScreen) {
                Point basePoint = tree.getBasePoint();
                if (highlightTree && idx == 0 && tree.getModel() != null && tree.getModel().isValid()) {
                    g.setColor(new Color(255, 255, 0, (int) (150 * opacityMultiplier)));
                    tree.getModel().fill(g);
                }

                if (basePoint.x != -1 && basePoint.y != -1) {
                    g.setColor(Application.COLOUR_SCHEME.getText());
                    g.setFont(FONT);
                    PaintUtils.drawString(g, tree.getName(), basePoint.x, basePoint.y);
                }
            }

            if (showOnMinimap) {
                Point minimapPoint = minimap.convert(tree.getTile());
                if (minimapPoint.x != -1 && minimapPoint.y != -1) {
                    g.setColor(Color.CYAN);
                    g.setFont(FONT.deriveFont(10f));
                    PaintUtils.drawString(g, tree.getName(), minimapPoint.x, minimapPoint.y);
                }
            }
            idx++;
        }

        if (nests != null) {
            for (Loot nest : nests) {
                if (nest.isOnScreen()) {
                    g.setColor(new Color(255, 0, 0, (int) (150 * opacityMultiplier)));
                    g.fill(nest.getTile().getPolygon());
                    g.setColor(Application.COLOUR_SCHEME.getText());

                    Point basePoint = nest.getBasePoint();
                    PaintUtils.drawString(g, "Nest", basePoint.x, basePoint.y);
                }
            }
        }

        opacityRate++;
        if (opacityRate == 100) {
            opacityUp = !opacityUp;
            opacityRate = 0;
        }

        if (opacityUp) {
            opacityMultiplier += 0.01f;
        } else {
            opacityMultiplier -= 0.01f;
        }
        g.dispose();
    }

    @EventHandler
    public void onMessageEvent(MessageEvent event) {
        if (event.getMessage().contains("You get some")) {
            lastYield = System.currentTimeMillis();
            logsChopped++;


            if (sampleLogsChopped == 0) {
                sampleStart = System.currentTimeMillis();
            }
            sampleLogsChopped++;
        }
    }

    @EventHandler
    public void onItemSpawned(SpawnGroundItemEvent event) {
        if (notifyOnNest) {
            List<Integer> nests = Ints.asList(NEST_IDS);
            event.getLoot().stream().filter(loot -> nests.contains(loot.getId())).forEach(loot -> {
                NotificationsUtil.showNotification("Woodcutting", "A birds nest has spawned.");
            });
        }
    }

    @Override
    public void onOptionsChanged() {
        Tree.TREE.show = showTrees;
        Tree.OAK.show = showOaks;
        Tree.WILLOW.show = showWillows;
        Tree.MAPLE.show = showMaples;
        Tree.YEW.show = showYews;

        filters = new ArrayList<>();
        for (Tree t : Tree.values()) {
            if (t.show) {
                filters.add(t.name);
            }
        }
    }

    private class WoodcuttingBoxOverlay extends BoxOverlay {
        private final Color BACKGROUND_COLOR = ColorUtils.setOpacity(new Color(75, 67, 54, 255), 200);
        private final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 9);

        protected WoodcuttingBoxOverlay(Plugin owner) {
            super(owner);
            setFloating(floating);
        }

        @Override
        public void draw(Graphics2D gfx) {
            gfx.setColor(BACKGROUND_COLOR);
            gfx.fillRect(0, 0, getWidth(), getHeight());

            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.setFont(FONT);
            PaintUtils.drawString(gfx, "Woodcutting", 10, 15);
            gfx.drawLine(10, 20, getWidth() - 10, 20);

            gfx.setFont(FONT);
            int logsChoppedWidth = gfx.getFontMetrics().stringWidth("Logs chopped:");
            PaintUtils.drawString(gfx, "Logs chopped:", 10, 35);

            gfx.setFont(FONT.deriveFont(Font.PLAIN));
            PaintUtils.drawString(gfx, String.valueOf(logsChopped), logsChoppedWidth + 20, 35);


            gfx.setFont(FONT);
            int logsPerHrWidth = gfx.getFontMetrics().stringWidth("Logs/hr:");
            PaintUtils.drawString(gfx, "Logs/hr:", 10, 50);

            gfx.setFont(FONT.deriveFont(Font.PLAIN));
            if (sampleLogsChopped > 0) {
                double timePerLog = (System.currentTimeMillis() - sampleStart) / sampleLogsChopped;
                int logsPerHour = (int) (3600000D / timePerLog);
                PaintUtils.drawString(gfx, String.valueOf(logsPerHour), logsPerHrWidth + 20, 50);
            } else {
                PaintUtils.drawString(gfx, "N/A", logsPerHrWidth + 20, 50);
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
            return isLoggedIn() && active && !bank.isOpen();
        }

        @Override
        public void setFloating(boolean floating) {
            WoodcuttingOverlayPlugin.this.floating = floating;
            WoodcuttingOverlayPlugin.this.persistOptions();
            super.setFloating(floating);
        }
    }

    private enum Tree {

        TREE("Tree", 1),
        OAK("Oak", 15),
        WILLOW("Willow", 30),
        MAPLE("Maple tree", 45),
        YEW("Yew", 60);

        String name;
        int level;
        boolean show;

        Tree(String name, int level) {
            this.name = name;
            this.level = level;
        }

    }

}

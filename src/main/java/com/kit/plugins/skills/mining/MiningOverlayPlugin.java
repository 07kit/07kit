package com.kit.plugins.skills.mining;

import com.google.common.primitives.Shorts;
import com.kit.Application;
import com.kit.api.event.MessageEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.PaintUtils;
import com.kit.Application;
import com.kit.api.collection.Filter;
import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.event.PaintEvent;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.ColorUtils;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.GameObject;
import com.kit.api.wrappers.GameObjectType;
import com.kit.core.control.PluginManager;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
public class MiningOverlayPlugin extends Plugin {
    private static final int WOODCUTTING_ANIMATION = 2846;
    private static final int[] PICKAXE_IDS = Arrays.stream(PickaxeType.values()).mapToInt(PickaxeType::getId).toArray();

    private final Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private final MiningBoxOverlay box = new MiningBoxOverlay(this);

    @Option(label = "Show clay rocks", type = Option.Type.TOGGLE, value = "true")
    private boolean showClay;
    @Option(label = "Show copper rocks", type = Option.Type.TOGGLE, value = "true")
    private boolean showCopper;
    @Option(label = "Show tin rocks", type = Option.Type.TOGGLE, value = "true")
    private boolean showTin;
    @Option(label = "Show iron rocks", type = Option.Type.TOGGLE, value = "true")
    private boolean showIron;
    @Option(label = "Show silver rocks", type = Option.Type.TOGGLE, value = "true")
    private boolean showSilver;
    @Option(label = "Show coal rocks", type = Option.Type.TOGGLE, value = "true")
    private boolean showCoal;
    @Option(label = "Show gold rocks", type = Option.Type.TOGGLE, value = "true")
    private boolean showGold;
    @Option(label = "Show mithril rocks", type = Option.Type.TOGGLE, value = "true")
    private boolean showMithril;
    @Option(label = "Show adamntite rocks", type = Option.Type.TOGGLE, value = "true")
    private boolean showAdamntite;
    @Option(label = "Show runite rocks", type = Option.Type.TOGGLE, value = "true")
    private boolean showRunite;
    @Option(label = "Draw rocks on minimap", type = Option.Type.TOGGLE, value = "true")
    private boolean showOnMinimap;
    @Option(label = "Draw rocks on screen", type = Option.Type.TOGGLE, value = "true")
    private boolean showOnScreen;
    @Option(label = "Highlight nearest matching rock", type = Option.Type.TOGGLE, value = "true")
    private boolean highlightRock;
    @Option(label = "Floating overlay", value = "false", type = Option.Type.TOGGLE)
    private boolean floating;
    private Filter<GameObject> filter;
    private List<GameObject> rocks;
    private long lastYield;
    private long sampleStart;
    private int sampleOresMined;
    private int oresMined = 0;

    private float opacityMultiplier = 0.01f;
    private boolean opacityUp = true;
    private int opacityRate = 0;

    private Map<Short, String> nameForColorMap = new HashMap<>();

    private boolean active;

    public MiningOverlayPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Mining";
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
            sampleOresMined = 0;
        }

        if (!isLoggedIn()) {
            return;
        }

        rocks = objects.find()
                .type(GameObjectType.INTERACTABLE)
                .hasAction("Mine")
                .distance(17)
                .filter(filter)
                .asList()
                .stream()
                .sorted(Comparator.comparingInt(x -> x.distanceTo(player)))
                .collect(Collectors.toList());

        active = inventory.contains(PICKAXE_IDS) || equipment.contains(PICKAXE_IDS);
    }

    @EventHandler
    public void onPaint(PaintEvent e) {
        if (!isLoggedIn() || rocks == null || !active || bank.isOpen()) {
            return;
        }

        Graphics2D g = (Graphics2D) e.getGraphics().create();
        int idx = 0;
        for (GameObject rock : rocks) {
            if (rock.unwrap() == null) {
                continue;
            }
            if (showOnScreen) {
                Point basePoint = rock.getBasePoint();
                if (highlightRock && idx == 0 && rock.getModel().isValid()) {
                    g.setColor(new Color(255, 255, 0, (int) (150 * opacityMultiplier)));
                    rock.getModel().fill(g);
                }

                if (basePoint.x != -1 && basePoint.y != -1) {
                    g.setColor(Application.COLOUR_SCHEME.getText());
                    g.setFont(FONT);
                    PaintUtils.drawString(g, getNameForRock(rock), basePoint.x, basePoint.y);
                }
            }

            if (showOnMinimap) {
                Point minimapPoint = minimap.convert(rock.getTile());
                if (minimapPoint.x != -1 && minimapPoint.y != -1) {
                    g.setColor(Color.CYAN);
                    g.setFont(FONT.deriveFont(10f));
                    PaintUtils.drawString(g, getNameForRock(rock), minimapPoint.x, minimapPoint.y);
                }
            }
            idx++;
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
        if (event.getMessage().contains("You manage to mine")) {
            lastYield = System.currentTimeMillis();
            oresMined++;


            if (sampleOresMined == 0) {
                sampleStart = System.currentTimeMillis();
            }
            sampleOresMined++;
        }
    }

    @Override
    public void onOptionsChanged() {
        RockType.CLAY.setShow(showClay);
        RockType.COPPER.setShow(showCopper);
        RockType.TIN.setShow(showTin);
        RockType.IRON.setShow(showIron);
        RockType.SILVER.setShow(showSilver);
        RockType.COAL.setShow(showCoal);
        RockType.GOLD.setShow(showGold);
        RockType.MITHRIL.setShow(showMithril);
        RockType.ADAMANTITE.setShow(showAdamntite);
        RockType.RUNITE.setShow(showRunite);

        List<Short> colors = new ArrayList<>();

        for (RockType r : RockType.values()) {
            if (r.isShow()) {
                colors.addAll(Shorts.asList(r.getColors()));
            }
        }

        filter = acceptable -> acceptable.getComposite() != null &&
                Arrays.asList(acceptable.getComposite().getOriginalModelColors())
                        .stream().anyMatch(shorts -> {
                    if (shorts == null) {
                        return false;
                    }
                    for (short color : shorts) {
                        if (colors.contains(color)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    private String getNameForRock(GameObject object) {
        if (object.getComposite() == null) {
            return null;
        }
        for (short color : object.getComposite().getOriginalModelColors()) {
            String name = getNameForColor(color);
            if (name != null) {
                return name;
            }
        }
        return null;
    }

    private String getNameForColor(short color) {
        if (nameForColorMap.containsKey(color)) {
            return nameForColorMap.get(color);
        }
        for (RockType type : RockType.values()) {
            for (short color_ : type.getColors()) {
                if (color_ == color) {
                    nameForColorMap.put(color, type.getName());
                    return type.getName();
                }
            }
        }
        return null;
    }

    private class MiningBoxOverlay extends BoxOverlay {
        private final Color BACKGROUND_COLOR = ColorUtils.setOpacity(new Color(75, 67, 54, 255), 200);
        private final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 9);

        protected MiningBoxOverlay(Plugin owner) {
            super(owner);
            setFloating(floating);
        }

        @Override
        public void draw(Graphics2D gfx) {
            gfx.setColor(BACKGROUND_COLOR);
            gfx.fillRect(0, 0, getWidth(), getHeight());

            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.setFont(FONT);
            PaintUtils.drawString(gfx, "Mining", 10, 15);
            gfx.drawLine(10, 20, getWidth() - 10, 20);

            gfx.setFont(FONT);
            int oresMinedWidth = gfx.getFontMetrics().stringWidth("Ores mined:");
            PaintUtils.drawString(gfx, "Ores mined:", 10, 35);

            gfx.setFont(FONT.deriveFont(Font.PLAIN));
            PaintUtils.drawString(gfx, String.valueOf(oresMined), oresMinedWidth + 20, 35);


            gfx.setFont(FONT);
            int oresPerHrWidth = gfx.getFontMetrics().stringWidth("Ores/hr:");
            PaintUtils.drawString(gfx, "Ores/hr:", 10, 50);

            gfx.setFont(FONT.deriveFont(Font.PLAIN));
            if (sampleOresMined > 0) {
                double timePerOre = (System.currentTimeMillis() - sampleStart) / sampleOresMined;
                int oresPerHour = (int) (3600000D / timePerOre);
                PaintUtils.drawString(gfx, String.valueOf(oresPerHour), oresPerHrWidth + 20, 50);
            } else {
                PaintUtils.drawString(gfx, "N/A", oresPerHrWidth + 20, 50);
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
            MiningOverlayPlugin.this.floating = floating;
            MiningOverlayPlugin.this.persistOptions();
            super.setFloating(floating);
        }
    }

}

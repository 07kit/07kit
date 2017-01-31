package com.kit.plugins.cluescrolls;

import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.OptionChangedEvent;
import com.kit.api.event.PaintEvent;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.ColorUtils;
import com.kit.api.util.NotificationsUtil;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Area;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.api.wrappers.Tile;
import com.kit.api.wrappers.WidgetItem;
import com.kit.plugins.map.WorldMapPlugin;
import com.kit.plugins.map.marker.ClueScrollMarker;
import com.kit.plugins.skills.mining.MiningOverlayPlugin;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class ClueScrollPlugin extends Plugin {

    public static final String CLUE_SCROLL = "Clue scroll";
    public static final String UNSUPPORTED_SCROLL = "UNSUPPORTED: SCROLL_";
    public static final String PUZZLE_BOX = "Puzzle box";
    public static final String UNSUPPORTED_PUZZLE = "UNSUPPORTED: PUZZLE_";
    public static final String SHERLOCK_MESSAGE = "Talk to Sherlock";


    public static final int SPADE = 952;

    public static final Area SHERLOCK_AREA = new Area(new Tile(2727, 3424, 0), new Tile(2739, 3411, 0));

    public static final int[] SPADE_CHECK = {SPADE};
    public static final int[] MACHETES = {975, 6313, 6315, 6317};
    public static final int[] AXES = {1349, 1351, 1353, 1355, 1357, 1359, 1361, 6739};

    public static final int[] CLUE_SCROLL_IDS = Arrays.stream(ClueScroll.values()).mapToInt(c -> c.id).toArray();

    private final ClueScrollBoxOverlay box = new ClueScrollBoxOverlay(this);

    @Option(label = "Floating overlay", value = "false", type = Option.Type.TOGGLE)
    private boolean floating;

    private ClueScroll current = null;
    private ClueScrollMarker currentMarker = null;

    private WorldMapPlugin worldMap;

    public ClueScrollPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Clue Scrolls";
    }

    @Override
    public void start() {
        worldMap = (WorldMapPlugin) getManager().getPlugins().stream().filter(p -> p.getClass().equals(WorldMapPlugin.class))
                .findFirst().get();
        ui.registerBoxOverlay(box);
    }

    @Override
    public void stop() {
        ui.deregisterBoxOverlay(box);
    }

    @Schedule(1000)
    public void checkForScrolls() {
        WidgetItem item = Session.get().inventory.find(CLUE_SCROLL_IDS).single();
        if (item != null) {
            current = ClueScroll.forId(item.getId());
            if (currentMarker == null || !currentMarker.getScroll().equals(current)) {
                worldMap.removeMarker(currentMarker);
                currentMarker = new ClueScrollMarker(worldMap, current);
                worldMap.addMarker(currentMarker);
            }
        } else {
            current = null;
            worldMap.removeMarker(currentMarker);
            currentMarker = null;
        }
    }

    public class ClueScrollBoxOverlay extends BoxOverlay {
        private final Color BACKGROUND_COLOR = ColorUtils.setOpacity(new Color(75, 67, 54, 255), 200);
        private final Color CLICKED_COLOR = ColorUtils.setOpacity(new Color(56, 48, 34, 255), 240);
        private final Color HOVER_COLOR = ColorUtils.setOpacity(new Color(150, 142, 128, 255), 220);
        private final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);

        private Rectangle locateOnMapBounds;
        private boolean hovered = false;
        private boolean clicking = false;

        protected ClueScrollBoxOverlay(Plugin owner) {
            super(owner);
            setFloating(floating);
        }

        @Override
        public void draw(Graphics2D gfx) {
            gfx.setColor(BACKGROUND_COLOR);
            gfx.fillRect(0, 0, getWidth(), getHeight());

            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.setFont(FONT);
            PaintUtils.drawString(gfx, "Clue Scrolls", 10, 20);
            gfx.drawLine(10, 30, getWidth() - 10, 30);

            gfx.setFont(FONT.deriveFont(11f));
            int difficultyWidth = gfx.getFontMetrics().stringWidth("Difficulty:");
            PaintUtils.drawString(gfx, "Difficulty:", 10, 50);

            gfx.setFont(FONT.deriveFont(Font.PLAIN, 12f));
            PaintUtils.drawString(gfx, current.getLevel().getName(), difficultyWidth + 20, 50);

            gfx.setFont(FONT.deriveFont(11f));
            int typeWidth = gfx.getFontMetrics().stringWidth("Type:");
            PaintUtils.drawString(gfx, "Type:", 10, 70);

            gfx.setFont(FONT.deriveFont(Font.PLAIN, 12f));
            PaintUtils.drawString(gfx, current.getType().getName(), typeWidth + 20, 70);

            //TODO paint all the things
            if (currentMarker != null) {
                String locateOnMap = "Locate";

                int locateOnMapWidth = gfx.getFontMetrics().stringWidth(locateOnMap);

                if (locateOnMapBounds == null) {
                    locateOnMapBounds =
                            new Rectangle((getWidth() / 2) - (locateOnMapWidth / 2),
                                    80, locateOnMapWidth + 10, gfx.getFontMetrics().getHeight() + 4);
                }

                if (clicking) {
                    gfx.setColor(CLICKED_COLOR);
                } else if (hovered) {
                    gfx.setColor(HOVER_COLOR);
                } else {
                    gfx.setColor(BACKGROUND_COLOR);
                }

                gfx.fill(locateOnMapBounds);
                gfx.setColor(Application.COLOUR_SCHEME.getText());
                gfx.draw(locateOnMapBounds);
                PaintUtils.drawString(gfx, locateOnMap, ((getWidth() / 2) - (locateOnMapWidth / 2)) + 5, locateOnMapBounds.y + gfx.getFontMetrics().getHeight());
            } else {
                locateOnMapBounds = null;
            }
        }

        @EventHandler
        public void handleLocateOnMapClick(MouseEvent event) {
            if (locateOnMapBounds != null && locateOnMapBounds.contains(event.getX() - getPosition().getX(), event.getY() - getPosition().getY())) {
                if (event.getID() == MouseEvent.MOUSE_PRESSED) {
                    clicking = true;
                    event.consume();
                } else if (event.getID() == MouseEvent.MOUSE_CLICKED) {
                    if (currentMarker != null) {
                        currentMarker.moveTo();
                    } else {
                        NotificationsUtil.showNotification("Clue Scroll", "No map marker found");
                    }
                    event.consume();
                } else {
                    hovered = true;
                    clicking = false;
                    event.consume();
                }
            } else {
                clicking = false;
                hovered = false;
            }
        }
        @Override
        public DockPosition getDockPosition() {
            return DockPosition.LEFT;
        }

        @Override
        public int getWidth() {
            return 200;
        }

        @Override
        public int getHeight() {
            return 110;
        }

        @Override
        public boolean isShowing() {
            return isLoggedIn() && current != null;
        }

        @Override
        public void setFloating(boolean floating) {
            ClueScrollPlugin.this.floating = floating;
            ClueScrollPlugin.this.persistOptions();
            super.setFloating(floating);
        }

    }
}

package com.kit.plugins.streamhelper;

import com.kit.Application;
import com.kit.api.event.ActionEvent;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.PaintUtils;
import com.kit.api.util.PriceLookup;
import com.kit.api.util.Utilities;
import com.kit.core.Session;
import com.kit.Application;
import com.kit.api.event.ActionEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.ColorUtils;
import com.kit.api.util.PaintUtils;
import com.kit.api.util.PriceLookup;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.WidgetItem;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LootProfitAndDropRecorder extends Plugin {

    public static final int MAX_TAKEN_TIME_MS = 5000;
    public static final String PROFIT_OPTIONAL_LBL = "profit";
    public static final String LOSS_OPTIONAL_LBL = "loss";

    private static final Color BORDER_COLOR = new Color(0, 0, 0, 255);
    private static final Color BACKGROUND_COLOR = ColorUtils.setOpacity(new Color(75, 67, 54, 255), 200);
    private static final Color CLICKED_COLOR = ColorUtils.setOpacity(new Color(56, 48, 34, 255), 240);
    private static final Color HOVER_COLOR = ColorUtils.setOpacity(new Color(150, 142, 128, 255), 220);
    private static final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 9);
    private static final int OVERLAY_WIDTH = 120;
    private static final int OVERLAY_HEIGHT = 80;

    private final LootProfitAndDropOverlay overlay = new LootProfitAndDropOverlay(this);

    @Option(label = PROFIT_OPTIONAL_LBL, value = "0", type = Option.Type.HIDDEN)
    private int profit;
    @Option(label = LOSS_OPTIONAL_LBL, value = "0", type = Option.Type.HIDDEN)
    private int loss;

    private Map<Integer, Long> takenItemsQueue = new ConcurrentHashMap<>();

    private Map<String, Integer> lastInventoryScan = new HashMap<>();

    private boolean firstScan = true;

    private volatile boolean bankOpen = false;

    public LootProfitAndDropRecorder(PluginManager manager) {
        super(manager);
    }

    @Override
    public boolean isEnabledByDefault() {
        return false;
    }

    @EventHandler
    public void onPickup(ActionEvent event) {
        bankOpen = Session.get().bank.isOpen();
        if (event.getInteraction().equals("Take")) {
            takenItemsQueue.put(event.getId(), System.currentTimeMillis());
        }
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Schedule(1000)
    public void checkIfLootWasTaken() {
        if (Session.get().isLoggedIn()) {
            boolean persist = false;
            Map<String, Integer> currentItemsMap = new HashMap<>(lastInventoryScan);
            for (WidgetItem item : Session.get().inventory.getAll().stream()
                    .filter(i -> i.getName() != null && i.getName().trim().length() > 0)
                    .filter(distinctByKey(WidgetItem::getName)).collect(Collectors.toList())) {
                int lastCount = 0;
                int newCount = Session.get().inventory.getCount(true, item.getName());
                if (!firstScan && !bankOpen) {
                    if (lastInventoryScan.containsKey(item.getName())) {
                        lastCount += lastInventoryScan.get(item.getName());
                    }
                    if (lastCount > newCount) {
                        loss += (lastCount - newCount) * item.getComposite().getPrice();
                        persist = true;
                    } else if (newCount > lastCount && takenItemsQueue.containsKey(item.getId()) &&
                            (System.currentTimeMillis() - takenItemsQueue.get(item.getId())) < MAX_TAKEN_TIME_MS) {
                        profit += (newCount - lastCount) * item.getComposite().getPrice();
                        persist = true;
                    }
                }
                firstScan = false;
                currentItemsMap.remove(item.getName());

                lastInventoryScan.put(item.getName(), newCount);
            }
            if (currentItemsMap.size() > 0) {
                persist = true;
                currentItemsMap.entrySet().forEach(entry -> {
                    lastInventoryScan.remove(entry.getKey());
                    loss += entry.getValue() * PriceLookup.getPrice(entry.getKey());
                });
            }
            if (persist) {
                persistOptions();
            }
        }
    }



    public class LootProfitAndDropOverlay extends BoxOverlay {

        private Rectangle resetBounds;
        private boolean hovered = false;
        private boolean clicking = false;

        protected LootProfitAndDropOverlay(Plugin owner) {
            super(owner);
        }

        @Override
        public void draw(Graphics2D gfx) {
            // Draw the box
            gfx.setColor(BACKGROUND_COLOR);
            gfx.fillRect(0, 0, OVERLAY_WIDTH, OVERLAY_HEIGHT);

            // Prepare to draw interacting info
            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.setFont(FONT);

            PaintUtils.drawString(gfx, "Profit/Loss", 10, 15);

            gfx.setFont(gfx.getFont().deriveFont(Font.PLAIN));

            // Draw underline
            gfx.drawLine(10, 20, OVERLAY_WIDTH - 10, 20);

            PaintUtils.drawString(gfx, String.format("Profit : %s", Utilities.prettyFormat(profit)), 10, 35);
            PaintUtils.drawString(gfx, String.format("Loss : %s", Utilities.prettyFormat(loss)), 10, 50);

            String reset = "Reset";

            int resetWidth = gfx.getFontMetrics().stringWidth(reset);

            if (resetBounds == null) {
                resetBounds =
                        new Rectangle((OVERLAY_WIDTH / 2) - (resetWidth / 2),
                                60, resetWidth + 10, gfx.getFontMetrics().getHeight() + 4);
            }

            if (clicking) {
                gfx.setColor(CLICKED_COLOR);
            } else if (hovered) {
                gfx.setColor(HOVER_COLOR);
            } else {
                gfx.setColor(BACKGROUND_COLOR);
            }

            gfx.fill(resetBounds);
            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.draw(resetBounds);
            PaintUtils.drawString(gfx, reset, ((OVERLAY_WIDTH / 2) - (resetWidth / 2)) + 5, resetBounds.y + gfx.getFontMetrics().getHeight());

            // Draw the box outline
            gfx.setColor(BORDER_COLOR);
            gfx.drawRect(0, 0, OVERLAY_WIDTH - 1, OVERLAY_HEIGHT - 1);
        }

        @EventHandler
        public void handleReset(MouseEvent event) {
            if (resetBounds != null && resetBounds.contains(event.getX() - getPosition().getX(), event.getY() - getPosition().getY())) {
                if (event.getID() == MouseEvent.MOUSE_PRESSED) {
                    clicking = true;
                    event.consume();
                } else if (event.getID() == MouseEvent.MOUSE_CLICKED) {
                    resetKills();
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

        private void resetKills() {
            profit = 0;
            loss = 0;
            persistOptions();
        }

        @Override
        public DockPosition getDockPosition() {
            return DockPosition.RIGHT;
        }

        @Override
        public int getWidth() {
            return OVERLAY_WIDTH;
        }

        @Override
        public int getHeight() {
            return OVERLAY_HEIGHT;
        }

        @Override
        public boolean isShowing() {
            return getOwner().isEnabled();
        }
    }

    @Override
    public String getName() {
        return "Profit & Loss Recorder";
    }

    @Override
    public void start() {
        ui.registerBoxOverlay(overlay);
    }

    @Override
    public void stop() {

    }
}

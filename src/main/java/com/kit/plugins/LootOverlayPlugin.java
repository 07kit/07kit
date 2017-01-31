package com.kit.plugins;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.kit.Application;
import com.kit.api.event.ActionEvent;
import com.kit.api.event.SpawnGroundItemEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NotificationsUtil;
import com.kit.api.util.PaintUtils;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.Loot;
import com.kit.api.wrappers.Tile;
import com.kit.core.Session;
import com.kit.Application;
import com.kit.api.collection.Filter;
import com.kit.api.event.ActionEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.event.SpawnGroundItemEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NotificationsUtil;
import com.kit.api.util.PaintUtils;
import com.kit.api.util.PriceLookup;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.Loot;
import com.kit.api.wrappers.PriceInfo;
import com.kit.api.wrappers.Tile;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.Application;
import com.kit.api.event.SpawnGroundItemEvent;
import com.kit.api.plugin.Option;
import com.kit.api.wrappers.Loot;
import com.kit.core.control.PluginManager;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 */
public class LootOverlayPlugin extends Plugin {

    @Option(label = "Maximum distance (in tiles)", value = "20", type = Option.Type.NUMBER)
    private int maximumDistance;
    @Option(label = "Hide items in the filtered list", value = "false", type = Option.Type.TOGGLE)
    private boolean hideFiltered;
    @Option(label = "Enter item names (separated by comma) to filter", value = "Bones", type = Option.Type.TEXT)
    private String filter;
    @Option(label = "Send a notification when filtered item spawns", value = "true", type = Option.Type.TOGGLE)
    private boolean sendNotification;
    @Option(label = "Show GE price", value = "false", type = Option.Type.TOGGLE)
    private boolean showGePrice;
    @Option(label = "Minimum GE price", value = "0", type = Option.Type.NUMBER)
    private int minimumGePrice;

    private Multimap<Tile, Loot> lootMap;
    private String compiledFilter;
    private String compiledAgainst;

    public LootOverlayPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Loot";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Schedule(600)
    public void pollLoot() {
        if (!isLoggedIn()) {
            return;
        }
        // Prepare the regex filter if required.
        checkFilter();

        // Group all loot by tile
        Multimap<Tile, Loot> tmpLootMap = ArrayListMultimap.create();
        Session.get().loot.find().distance(maximumDistance)
                .filter(acceptable -> {
                    if (acceptable.getName().toLowerCase().matches(compiledFilter)) {
                        return !hideFiltered;
                    }
                    return filter.length() == 0 || hideFiltered;
                })
                .asList()
                .forEach(item -> {
                    item.getComposite().getPrice();//force price to load
                    if (item.getComposite().getPrice() >= minimumGePrice) {
                        tmpLootMap.put(item.getTile(), item);
                    }
                });
        lootMap = tmpLootMap;
    }

    @EventHandler
    public void onGroundItemSpawned(SpawnGroundItemEvent evt) {
        if (sendNotification &&
                !hideFiltered && filter.length() > 0 && evt.getLoot().stream().anyMatch(x ->
                x.getName().toLowerCase().matches(compiledFilter)) && !ui.isFocused()) {
            NotificationsUtil.showNotification("An item has spawned.", "One of your filtered items has spawned.");
        }
        pollLoot(); // Force an update :-)
    }

    private void checkFilter() {
        if (!filter.equals(compiledAgainst)) {
            String[] options = filter.toLowerCase().split(",");
            StringBuilder regexBuilder = new StringBuilder();
            for (int i = 0; i < options.length; i++) {
                regexBuilder.append("(").append(options[i].replaceAll("\\*", ".*?")).append(")");
                if (i < options.length - 1) {
                    regexBuilder.append("|");
                }
            }
            compiledFilter = regexBuilder.toString();
            compiledAgainst = filter;
        }
    }

    @EventHandler
    public void onActionEvent(ActionEvent evt) {
        pollLoot();
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        if (lootMap != null && isLoggedIn() && !bank.isOpen()) {
            Graphics2D g = (Graphics2D) event.getGraphics().create();
            lootMap.keySet().forEach(tile -> {
                g.setColor(new Color(255, 255, 0, 30));
                g.draw(tile.getPolygon());
                g.setColor(new Color(255, 0, 0, 30));
                g.fill(tile.getPolygon());

                int idx = 0;
                for (Loot item : lootMap.get(tile)) {
                    if (item.getName() == null) {
                        continue;
                    }
                    Point screenPoint = item.getBasePoint();
                    if (screenPoint == null) {
                        continue;
                    }

                    g.setColor(Application.COLOUR_SCHEME.getText());

                    if (!showGePrice) {
                        PaintUtils.drawString(g, String.format("%s (%d)", item.getName(), item.getStackSize()), screenPoint.x, screenPoint.y - (idx * 15));
                    } else {
                        PaintUtils.drawString(g, String.format("%s (%d @ %s)",
                                item.getName(), item.getStackSize(), Utilities.prettyFormat(item.getComposite().getPrice())), screenPoint.x, screenPoint.y - (idx * 15));
                    }
                    idx++;

                    Point minimapPoint = minimap.convert(item.getTile());
                    if (minimapPoint.x != -1 && minimapPoint.y != -1) {
                        g.setColor(Color.PINK);
                        g.fillOval(minimapPoint.x, minimapPoint.y, 1, 1);
                    }
                }
            });
            g.dispose();
        }
    }
}

package com.kit.plugins;

import ch.swingfx.color.ColorUtil;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.wrappers.Tab;
import com.kit.api.wrappers.WidgetItem;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryMarkerPlugin extends Plugin {

    public static final int ALPHA = 100;

    public static final Color RED = new Color(208, 14, 13, ALPHA);
    public static final Color BLUE = new Color(69, 148, 255, ALPHA);
    public static final Color ORANGE = new Color(221, 149, 37, ALPHA);
    
    @Option(label = "Enter item names you want to mark with red (separated by ,)", value = "", type = Option.Type.TEXT)
    private String redNames;
    @Option(label = "Enter item names you want to mark with blue (separated by ,)", value = "", type = Option.Type.TEXT)
    private String blueNames;
    @Option(label = "Enter item names you want to mark with orange (separated by ,)", value = "", type = Option.Type.TEXT)
    private String orangeNames;

    private String compiledRedFilter;
    private String compiledRedAgainst;
    private String compiledBlueFilter;
    private String compiledBlueAgainst;
    private String compiledOrangeFilter;
    private String compiledOrangeAgainst;
    
    private Map<Color, java.util.List<Rectangle>> overlays = new HashMap<>();
    
    public InventoryMarkerPlugin(PluginManager manager) {
        super(manager);
    }
    
    @Schedule(600)
    public void updateOverlays() {
        if (!Session.get().isLoggedIn()) {
            return;
        }

        checkBlueFilter();
        checkRedFilter();
        checkOrangeFilter();

        overlays.put(RED, getFiltered(compiledRedFilter));
        overlays.put(BLUE, getFiltered(compiledOrangeFilter));
        overlays.put(ORANGE, getFiltered(compiledBlueFilter));
    }

    private java.util.List<Rectangle> getFiltered(String filter) {
        return Session.get().inventory.find()
                .filter(acceptable -> acceptable.getName() != null &&
                        acceptable.getName().toLowerCase().matches(filter)).asList()
                .stream().map(WidgetItem::getArea).collect(Collectors.toList());
    }

    private void checkRedFilter() {
        if (!redNames.equals(compiledRedAgainst)) {
            String[] options = redNames.toLowerCase().split(",");
            StringBuilder regexBuilder = new StringBuilder();
            for (int i = 0; i < options.length; i++) {
                regexBuilder.append("(").append(options[i].replaceAll("\\*", ".*?")).append(")");
                if (i < options.length - 1) {
                    regexBuilder.append("|");
                }
            }
            compiledRedFilter = regexBuilder.toString();
            compiledRedAgainst = redNames;
        }
    }

    private void checkBlueFilter() {
        if (!blueNames.equals(compiledBlueAgainst)) {
            String[] options = blueNames.toLowerCase().split(",");
            StringBuilder regexBuilder = new StringBuilder();
            for (int i = 0; i < options.length; i++) {
                regexBuilder.append("(").append(options[i].replaceAll("\\*", ".*?")).append(")");
                if (i < options.length - 1) {
                    regexBuilder.append("|");
                }
            }
            compiledBlueFilter = regexBuilder.toString();
            compiledBlueAgainst = blueNames;
        }
    }

    private void checkOrangeFilter() {
        if (!orangeNames.equals(compiledOrangeAgainst)) {
            String[] options = orangeNames.toLowerCase().split(",");
            StringBuilder regexBuilder = new StringBuilder();
            for (int i = 0; i < options.length; i++) {
                regexBuilder.append("(").append(options[i].replaceAll("\\*", ".*?")).append(")");
                if (i < options.length - 1) {
                    regexBuilder.append("|");
                }
            }
            compiledOrangeFilter = regexBuilder.toString();
            compiledOrangeAgainst = orangeNames;
        }
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        if (!Session.get().isLoggedIn() || !Session.get().tabs.isOpen(Tab.INVENTORY)) {
            return;
        }

        for (Map.Entry<Color, List<Rectangle>> colorListEntry : overlays.entrySet()) {
            event.getGraphics().setColor(colorListEntry.getKey());
            colorListEntry.getValue().forEach(r -> event.getGraphics().fillRect(r.x, r.y, r.width, r.height));
        }
    }
    
    @Override
    public String getName() {
        return "Inventory Marker";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}

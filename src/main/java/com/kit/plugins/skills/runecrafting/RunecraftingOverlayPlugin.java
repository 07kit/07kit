package com.kit.plugins.skills.runecrafting;

import com.kit.Application;
import com.kit.api.util.PaintUtils;
import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.WidgetItem;
import com.kit.core.control.PluginManager;

import java.awt.*;
import java.util.List;

import static java.awt.Color.GREEN;
import static java.awt.Color.RED;

/**
 */
public class RunecraftingOverlayPlugin extends Plugin {
    private static final int POUCH_ESSENCE_SETTING = 486;
    private List<WidgetItem> pouchWidgets;
    private int[] pouchIds;

    public RunecraftingOverlayPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Runecrafting";
    }

    @Override
    public String getGroup() {
        return "Skills";
    }

    @Override
    public void start() {
        pouchIds = new int[Pouch.values().length];
        for (int i = 0; i < pouchIds.length; i++) {
            pouchIds[i] = Pouch.values()[i].id;
        }
    }

    @Override
    public void stop() {

    }

    @Schedule(600)
    public void refresh() {
        if (!isLoggedIn()) {
            return;
        }

        pouchWidgets = inventory.find(pouchIds).asList();
    }

    @EventHandler
    public void paint(PaintEvent event) {
        if (!isLoggedIn() || pouchWidgets == null || pouchWidgets.size() == 0 || bank.isOpen()) {
            return;
        }

        Graphics2D g = (Graphics2D) event.getGraphics();
        int pouchCounts = settings.getPlayerSetting(POUCH_ESSENCE_SETTING);
        for (WidgetItem item : pouchWidgets) {
            Pouch pouch = Pouch.forId(item.getId());
            if (pouch == null) {
                continue;
            }

            int essenceCount = pouch.contents(pouchCounts);
            if (essenceCount <= 0) {
                g.setColor(RED);
            } else {
                g.setColor(GREEN);
            }

            Rectangle area = item.getArea();
            g.fill(area);
            g.setColor(Application.COLOUR_SCHEME.getText());
            PaintUtils.drawString(g, String.valueOf(essenceCount), area.x + 5, area.y + 5);
        }
        g.dispose();
    }

    private enum Pouch {

        SMALL(5509, 0, 0x3),
        MEDIUM(5510, 3, 0x7),
        LARGE(5512, 9, 0xf),
        GIGANTIC(5514, 18, 0x1f);

        private final int id;
        private final int shift;
        private final int mask;

        Pouch(final int id, final int shift, final int mask) {
            this.id = id;
            this.shift = shift;
            this.mask = mask;
        }

        public static Pouch forId(final int id) {
            for (final Pouch p : values()) {
                if (id == p.id) {
                    return p;
                }
            }
            return null;
        }

        public int contents(final int value) {
            if (shift == 0) {
                return value & mask;
            }
            return (value >> shift) & mask;
        }

    }

}

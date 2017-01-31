package com.kit.plugins.combat;

import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.LoginEvent;
import com.kit.api.event.PaintEvent;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.ColorUtils;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Skill;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;
import com.kit.core.control.PluginManager;
import com.kit.Application;
import com.kit.api.util.ColorUtils;
import com.kit.api.wrappers.Skill;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;
import com.kit.core.control.PluginManager;
import com.kit.plugins.skills.mining.MiningOverlayPlugin;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 */
public class BoostsOverlayPlugin extends Plugin {
    private final BoostsBoxOverlay box = new BoostsBoxOverlay(this);
    private List<CheckedStat> stats = new ArrayList<>();
    private int changeCount;

    private BoostItem currentBoostItem;
    private String[] boostItemNames;

    @Option(label = "Floating overlay", value = "false", type = Option.Type.TOGGLE)
    private boolean floating;


    public BoostsOverlayPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Boosts";
    }

    @Override
    public String getGroup() {
        return "Combat";
    }

    @Override
    public void start() {
        for (Skill skill : Skill.values()) {
            if (skill == Skill.PRAYER || skill == Skill.COMBAT || skill == Skill.OVERALL || skill == Skill.HITPOINTS) {
                continue;
            }
            stats.add(new CheckedStat(skill));
        }

        boostItemNames = new String[BoostItem.values().length];
        for (int i = 0; i < boostItemNames.length; i++) {
            BoostItem item = BoostItem.values()[i];
            boostItemNames[i] = item.name;
        }

        ui.registerBoxOverlay(box);
    }

    @Override
    public void stop() {
        ui.deregisterBoxOverlay(box);
    }

    @Schedule(100)
    public void detectChanges() {
        if (!isLoggedIn()) {
            return;
        }

        int newChangeCount = 0;
        for (CheckedStat stat : stats) {
            stat.update();
            if (stat.show()) {
                newChangeCount++;
            }
        }
        changeCount = newChangeCount;
    }

    @EventHandler
    public void onPaintEvent(PaintEvent event) {
        if (currentBoostItem != null) {
            int dynamicBoost = (int) Math.floor(skills.getBaseLevel(currentBoostItem.skill) * currentBoostItem.multiplier);
            int totalBoost = currentBoostItem.baseBoost + dynamicBoost;

            Graphics2D gfx = (Graphics2D) event.getGraphics().create();
            gfx.setColor(Color.GREEN);
            PaintUtils.drawString(gfx, String.format("+%d", totalBoost), mouse.getPosition().x, mouse.getPosition().y);
            gfx.dispose();
        }
    }

    @EventHandler
    public void onMouseEvent(MouseEvent event) {
        if (!isLoggedIn()) {
            return;
        }

        Widget bounds = inventory.getWidget();
        if (bounds != null && bounds.getArea().contains(event.getX(), event.getY())) {
            Optional<WidgetItem> hoverTarget = inventory.find().nameContains(boostItemNames)
                    .asList()
                    .stream()
                    .filter(x -> x.getArea().contains(event.getX(), event.getY()))
                    .findFirst();

            if (hoverTarget.isPresent()) {
                WidgetItem widget = hoverTarget.get();
                currentBoostItem = BoostItem.forName(widget.getName());
            } else {
                currentBoostItem = null;
            }
        } else { // aint got nuffin'
            currentBoostItem = null;
        }
    }

    @EventHandler
    public void onLoginEvent(LoginEvent event) {
        stats.forEach(x -> {
            x.changed = false;
            x.nextChange = System.currentTimeMillis() + 60000;
        });
    }

    private class CheckedStat {
        public Skill skill;
        public int base;
        public int level;
        public boolean changed;
        public long nextChange;

        public CheckedStat(Skill skill) {
            this.skill = skill;
        }

        public void update() {
            int newLevel = skills.getLevel(skill);
            base = skills.getBaseLevel(skill);
            if (level != newLevel) {
                nextChange = System.currentTimeMillis() + 60000;
                changed = true;
            }
            level = newLevel;
        }

        public boolean show() {
            return level != base;
        }
    }

    private class BoostsBoxOverlay extends BoxOverlay {
        private final Color BACKGROUND_COLOR = ColorUtils.setOpacity(new Color(75, 67, 54, 255), 200);
        private final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 9);

        protected BoostsBoxOverlay(Plugin owner) {
            super(owner);
            setFloating(floating);
        }

        @Override
        public void draw(Graphics2D gfx) {
            gfx.setColor(BACKGROUND_COLOR);
            gfx.fillRect(0, 0, getWidth(), getHeight());

            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.setFont(FONT);
            PaintUtils.drawString(gfx, "Boosts", 10, 15);

            gfx.drawLine(10, 20, getWidth() - 10, 20);

            gfx.setFont(FONT.deriveFont(Font.PLAIN));

            int offset = 0;
            for (CheckedStat stat : stats) {
                stat.update();
                if (stat.show()) {
                    PaintUtils.drawString(gfx, String.format("%s %d/%d (%d)", stat.skill.getName(), stat.level, stat.base, (stat.nextChange - System.currentTimeMillis()) / 1000), 10, 35 + (offset * 15));
                    offset++;
                }
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
            return 35 + (changeCount * 15);
        }

        @Override
        public boolean isShowing() {
            return isLoggedIn() && changeCount > 0;
        }

        @Override
        public void setFloating(boolean floating) {
            BoostsOverlayPlugin.this.floating = floating;
            BoostsOverlayPlugin.this.persistOptions();
            super.setFloating(floating);
        }
    }

    private enum BoostItem {

        ATTACK_POTION(Skill.ATTACK, "Attack potion", 3, 0.10),
        STRENGTH_POTION(Skill.STRENGTH, "Strength potion", 3, 0.10),
        DEFENCE_POTION(Skill.DEFENCE, "Defence potion", 3, 0.10),
        MAGIC_POTION(Skill.MAGIC, "Magic potion", 4, 0.0),
        RANGING_POTION(Skill.RANGED, "Ranging potion", 4, 0.10),

        SUPER_ATTACK_POTION(Skill.ATTACK, "Super attack", 5, 0.15),
        SUPER_STRENGTH_POTION(Skill.STRENGTH, "Super strength", 5, 0.15),
        SUPER_DEFENCE_POTION(Skill.DEFENCE, "Super defence", 5, 0.15),
        SUPER_MAGIC_POTION(Skill.MAGIC, "Super magic", 5, 0.15),
        SUPER_RANGING_POTION(Skill.RANGED, "Super magic", 5, 0.15);

        Skill skill;
        String name;
        int baseBoost;
        double multiplier;

        BoostItem(Skill skill, String name, int baseBoost, double multiplier) {
            this.skill = skill;
            this.name = name;
            this.baseBoost = baseBoost;
            this.multiplier = multiplier;
        }

        public static BoostItem forName(String name) {
            for (BoostItem item : BoostItem.values()) {
                if (name.startsWith(item.name)) {
                    return item;
                }
            }
            return null;
        }
    }
}

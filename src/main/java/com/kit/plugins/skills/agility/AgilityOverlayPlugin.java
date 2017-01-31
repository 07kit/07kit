package com.kit.plugins.skills.agility;

import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.*;
import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.ColorUtils;
import com.kit.api.util.PaintUtils;
import com.kit.core.control.PluginManager;
import com.kit.plugins.skills.mining.MiningOverlayPlugin;

import java.awt.*;
import java.util.HashSet;

/**
 * Created by Matt on 04/09/2016.
 */
public class AgilityOverlayPlugin extends Plugin {
    private static final int MARK_OF_GRACE = 11849;

    @Option(label = "Floating overlay", value = "false", type = Option.Type.TOGGLE)
    private boolean floating;

    private AgilityCourse currentCourse = null;
    private int laps = 0;
    private HashSet<Model> modelSet = new HashSet<>();
    private Model markModel = null;
    private Tile markLocation = null;
    private int agilityXp = 0;
    private boolean active;
    private AgilityBoxOverlay box = new AgilityBoxOverlay(this);
    private float opacityMultiplier = 0.01f;
    private boolean opacityUp = true;
    private int opacityRate = 0;

    public AgilityOverlayPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Agility";
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

    @EventHandler
    public void onPaint(PaintEvent e) {
        if (bank.isOpen()) {
            return;
        }

        Graphics2D g = (Graphics2D) e.getGraphics().create();
        g.setColor(Color.YELLOW);

        modelSet.forEach(model -> {
            if (!model.isOnScreen())
                return;

            Point point = model.getBasePoint();
            g.setColor(new Color(255, 255, 0, (int) (150 * opacityMultiplier)));
            g.fillOval(point.x - 16, point.y - 16, 30, 30);
            //model.fill(g);
        });

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

        if (markLocation == null)
            return;

        g.setColor(new Color(255, 255, 0, 30));
        g.draw(markLocation.getPolygon());
        g.setColor(new Color(255, 0, 0, 30));
        g.fill(markLocation.getPolygon());


        g.dispose();
    }

    @Schedule(2000)
    public void refreshCourse() {
        int level = skills.getLevel(Skill.AGILITY);
        for (AgilityCourse course : AgilityCourse.values()) {
            if (course.level() > level)
                continue;
            for (Area area : course.areas()) {
                if (!area.contains(player))
                    continue;
                if (course != currentCourse) {
                    currentCourse = course;
                    laps = 0;
                    active = true;
                }
                return;
            }
        }
        currentCourse = null;
        laps = 0;
        modelSet.clear();
        active = false;
    }

    @Schedule(600)
    public void updateData() {
        if (currentCourse == null)
            return;

        objects.find(currentCourse.objectIds()).filter(obj -> obj.getZ() == player.getZ()).forEach(obj -> {
            Model model = obj.getModel();
            if (model == null || modelSet.contains(model))
                return;
            modelSet.add(model);
        });

        if (!currentCourse.rooftop()) {
            markLocation = null;
            markModel = null;
        }

        Loot markOfGrace = loot.find(MARK_OF_GRACE).single();
        if (markOfGrace == null) {
            markLocation = null;
            markModel = null;
            return;
        }
        markLocation = markOfGrace.getTile();
        markModel = markOfGrace.getModel();

    }

    @Schedule(300)
    public void checkXp() {
        int newAgility = skills.getExperience(Skill.AGILITY);
        if (agilityXp == 0) {
            agilityXp = newAgility;
            return;
        }

        if (currentCourse == null)
            return;

        if (agilityXp == newAgility)
            return;

        int difference = newAgility - agilityXp;
        agilityXp = newAgility;
        if (difference != Math.floor(currentCourse.lastObstacleXp()) && difference != Math.ceil(currentCourse.lastObstacleXp()))
            return;

        Tile location = player.getTile();
        for (Tile tile : currentCourse.finalTiles()) {
            if (!tile.equals(location))
                continue;
            laps++;
            return;
        }
    }

    private class AgilityBoxOverlay extends BoxOverlay {
        private final Color BACKGROUND_COLOR = ColorUtils.setOpacity(new Color(75, 67, 54, 255), 200);
        private final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 9);

        AgilityBoxOverlay(AgilityOverlayPlugin plugin) {
            super(plugin);
            setFloating(floating);
        }

        @Override
        public void draw(Graphics2D gfx) {
            gfx.setColor(BACKGROUND_COLOR);
            gfx.fillRect(0, 0, getWidth(), getHeight());

            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.setFont(FONT);
            PaintUtils.drawString(gfx, "Agility", 10, 15);
            gfx.drawLine(10, 20, getWidth() - 10, 20);

            gfx.setFont(FONT);
            int currentCourseWidth = gfx.getFontMetrics().stringWidth("Course:");
            PaintUtils.drawString(gfx, "Course:", 10, 35);

            gfx.setFont(FONT.deriveFont(Font.PLAIN));
            PaintUtils.drawString(gfx, currentCourse.toString(), currentCourseWidth + 20, 35);

            gfx.setFont(FONT);
            int lapsDoneWidth = gfx.getFontMetrics().stringWidth("Laps:");
            PaintUtils.drawString(gfx, "Laps:", 10, 50);

            gfx.setFont(FONT.deriveFont(Font.PLAIN));
            PaintUtils.drawString(gfx, String.valueOf(laps), lapsDoneWidth + 20, 50);


            gfx.setFont(FONT);
            int lapsTilLevelWidth = gfx.getFontMetrics().stringWidth("Laps til level:");
            PaintUtils.drawString(gfx, "Laps til level:", 10, 65);

            gfx.setFont(FONT.deriveFont(Font.PLAIN));
            long lapsTilLevel = Math.round(skills.getExperienceToLevel(Skill.AGILITY, skills.getLevel(Skill.AGILITY) + 1) / currentCourse.totalXp());
            PaintUtils.drawString(gfx, String.valueOf(lapsTilLevel), lapsTilLevelWidth + 20, 65);

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
            return 140;
        }

        @Override
        public int getHeight() {
            return 75;
        }

        @Override
        public boolean isShowing() {
            return isLoggedIn() && !bank.isOpen() && active;
        }

        @Override
        public void setFloating(boolean floating) {
            AgilityOverlayPlugin.this.floating = floating;
            AgilityOverlayPlugin.this.persistOptions();
            super.setFloating(floating);
        }
    }
}

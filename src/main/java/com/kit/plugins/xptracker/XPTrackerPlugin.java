package com.kit.plugins.xptracker;

import com.kit.Application;
import com.kit.api.plugin.Option;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Skill;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
public class XPTrackerPlugin extends Plugin {
    private final BufferedImage hoverPanel = new BufferedImage(170, 80, BufferedImage.TYPE_INT_ARGB);
    //private final XPTrackerSidebarWidget sidebarWidget = new XPTrackerSidebarWidget(this);
    private final XPTrackerSidebarTab sidebarTab = new XPTrackerSidebarTab(this);
    private final Map<Skill, TrackedSkill> trackingMap = new LinkedHashMap<>();

    @Option(label = "Show skill Orb for ... seconds", value = "10", type = Option.Type.NUMBER)
    private int orbDuration;

    private boolean isRenderable = false;
    private boolean isHovering = false;
    private BufferedImage screenBuffer;
    private TrackedSkill trackedSkill;

    public XPTrackerPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "XP Tracker";
    }

    @Override
    public String getGroup() {
        return "Skills";
    }

    @Override
    public void start() {
        for (Skill skill : Skill.values()) {
            if (skill == Skill.OVERALL || skill == Skill.COMBAT) continue;
            trackingMap.put(skill, new TrackedSkill(skill));
        }

        //ui.registerSidebarWidget(sidebarWidget);
        ui.registerTab(sidebarTab);
    }

    @Override
    public void stop() {
        //ui.deregisterSidebarWidget(sidebarWidget);
        ui.deregisterTab(sidebarTab);
    }

    @Schedule(600)
    public void pollXpChanged() {
        if (screenBuffer == null || screenBuffer.getWidth() != Session.get().getAppletLoader().getApplet().getWidth()) {
            // TODO: lol spaghetti
            if (Session.get().getAppletLoader().getApplet().getWidth() <= 0) {
                return;
            }
            screenBuffer = new BufferedImage(Session.get().getAppletLoader().getApplet().getWidth(), 300, BufferedImage.TYPE_INT_ARGB);
        }

        boolean changeRequired = false;
        long time = System.currentTimeMillis();
        for (TrackedSkill tracker : trackingMap.values()) {
            if (tracker.startLevel == 0) {
                tracker.startLevel = skills.getBaseLevel(tracker.skill);
                tracker.startXp = skills.getExperience(tracker.skill);
                tracker.lastLevel = skills.getBaseLevel(tracker.skill);
                tracker.lastXp = skills.getExperience(tracker.skill);
            } else {
                int curLevel = skills.getBaseLevel(tracker.skill);
                int curXp = skills.getExperience(tracker.skill);
                if (curLevel != tracker.lastLevel || curXp != tracker.lastXp) {
                    if (!tracker.isActive() || tracker.isExpired()) {
                        // If the tracker wasn't updated in the last 15 mins we reset the start time so exp/hr isn't fucekd.
                        tracker.startTime = System.currentTimeMillis();
                        tracker.xpGained += tracker.lastXp - tracker.startXp;
                        tracker.startXp = tracker.lastXp;
                    }
                    // We've updated :-)
                    tracker.updatedTime = System.currentTimeMillis();
                    tracker.lastLevel = curLevel;
                    tracker.lastXp = curXp;
                    tracker.recalculate();
                    changeRequired = true;
                }
            }

            if (time - tracker.updatedTime <= (orbDuration * 1000) + 1000) {
                // If any trackers were updated in the last 11 seconds make sure we update the overlays.
                changeRequired = true;
            }
        }

        if (changeRequired || isHovering) {
            //sidebarWidget.update();
            sidebarTab.update();
            drawOverlays();
        }
    }

    @EventHandler
    public void onMouseMove(MouseEvent event) {
        Optional<TrackedSkill> targetedTracker = trackingMap.values().stream()
                .filter(x -> System.currentTimeMillis() - x.updatedTime < (orbDuration * 1000) && x.renderedBounds.contains(event.getPoint()))
                .findFirst();
        if (targetedTracker.isPresent()) {
            trackedSkill = targetedTracker.get();
            isHovering = true;
            isRenderable = true;
        } else if (isHovering) { // Clear off the hover thing..
            isHovering = false;
            isRenderable = true;
            drawOverlays();
        }
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        if (isRenderable) {
            event.getGraphics().drawImage(screenBuffer, 0, 0, null);
        }
    }

    private void drawOverlays() {
        List<TrackedSkill> changedSkills = trackingMap.values().stream()
                .filter(x -> System.currentTimeMillis() - x.updatedTime < (orbDuration * 1000))
                .collect(Collectors.toList());
        if (changedSkills.size() == 0) { // No bulbs = no render
            isRenderable = false;
            return;
        }

        Graphics2D g = (Graphics2D) screenBuffer.getGraphics().create();
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setBackground(new Color(0, 0, 0, 0));
        g.clearRect(0, 0, screenBuffer.getWidth(), screenBuffer.getHeight());

        boolean inResizable = Session.get().inResizableMode();
        int trackerWidth = inResizable ? 50 : 40;
        int viewportOffset = ((changedSkills.size()) / 2) * trackerWidth;

        int xInset = 0;
        int yInset = inResizable ? 15 : 40;
        for (TrackedSkill tracker : changedSkills) {
            int widthOffset = (client().getViewportWidth() / 2) - viewportOffset + (xInset * trackerWidth);
            if (!inResizable) {
                widthOffset = (client().getViewportWidth() - (changedSkills.size() * trackerWidth) + (xInset * trackerWidth)) - 20;
            }


            // Draw the progress chart thingy
            int width = trackerWidth - 10;
            int height = trackerWidth - 10;
            int radius = Math.min(width, height);
            int centerX = widthOffset + ((width - radius) / 2);
            int centerY = yInset + ((height - radius) / 2);
            double extent = 360d * tracker.percentileToLevel;
            Arc2D completedSlice = new Arc2D.Double(centerX, centerY, radius, radius, 90, -extent, Arc2D.PIE);
            g.setColor(Application.COLOUR_SCHEME.getLight());
            g.fill(completedSlice);

            extent = 360 - extent;
            Arc2D uncompletedSlice = new Arc2D.Double(centerX, centerY, radius, radius, 90, extent, Arc2D.PIE);
            g.setColor(Application.COLOUR_SCHEME.getDark());
            g.fill(uncompletedSlice);
            // Draw the icon in the middle of the chart thingy
            if (tracker.icon != null) {
                g.drawImage(tracker.icon, centerX + (width / 4), centerY + (height / 4), width / 2, height / 2, null);
            }

            xInset++;
            tracker.renderedBounds = new Rectangle(centerX - 15, centerY - 15, width + 15, height + 15); // give us a nice area to work with lol
        }
        if (isHovering) {
            Graphics2D panelGraphics = (Graphics2D) hoverPanel.getGraphics();
            panelGraphics.setBackground(new Color(0, 0, 0, 0));
            panelGraphics.clearRect(0, 0, hoverPanel.getWidth(), hoverPanel.getHeight());

            panelGraphics.setColor(new Color(75, 67, 54, 255));
            panelGraphics.fillRect(0, 0, hoverPanel.getWidth(), hoverPanel.getHeight());

            panelGraphics.setColor(Color.BLACK);
            panelGraphics.drawRect(0, 0, hoverPanel.getWidth() - 1, hoverPanel.getHeight() - 1);

            panelGraphics.setColor(Application.COLOUR_SCHEME.getText());
            panelGraphics.setFont(panelGraphics.getFont().deriveFont(Font.BOLD, 11f));

            int idx = 1;
            PaintUtils.drawString(panelGraphics, trackedSkill.skill.getName(), 10, idx * 15);
            idx++;
            PaintUtils.drawString(panelGraphics, "Levels gained: " + (trackedSkill.lastLevel - trackedSkill.startLevel), 10, idx * 15);
            idx++;
            PaintUtils.drawString(panelGraphics, "XP gained: " + (trackedSkill.xpGained + (trackedSkill.lastXp - trackedSkill.startXp)), 10, idx * 15);
            idx++;
            PaintUtils.drawString(panelGraphics, "XP/hr: " + trackedSkill.xpPerHour, 10, idx * 15);
            idx++;

            Duration duration = Duration.ofMillis(trackedSkill.timeToLevel);
            String s = String.format("%02d:%02d:%02d%n", duration.toHours(),
                    duration.minusHours(duration.toHours()).toMinutes(),
                    duration.minusMinutes(duration.toMinutes()).getSeconds());
            PaintUtils.drawString(panelGraphics, "Time to level: " + s, 10, idx * 15);

            g.drawImage(hoverPanel, mouse.getPosition().x, mouse.getPosition().y, null);
        }
        g.dispose();
        isRenderable = true;
    }

    public Collection<TrackedSkill> getTrackedSkills() {
        return trackingMap.values();
    }


    public class TrackedSkill {
        public final Skill skill;
        public int startLevel;
        public int startXp;
        public int lastLevel;
        public int lastXp;
        public int xpToLevel;
        public int xpGained;
        public long startTime;
        public long updatedTime;
        public long xpPerHour;
        public long timeToLevel;
        public float percentileToLevel;
        public Image icon;
        public javafx.scene.image.Image image;

        public Rectangle renderedBounds = new Rectangle(0, 0, 0, 0);

        public TrackedSkill(Skill skill) {
            try {
                this.icon = ImageIO.read(getClass().getResourceAsStream("/" + skill.getName().toLowerCase() + ".gif"));
                this.image = new javafx.scene.image.Image(getClass().getResourceAsStream("/" + skill.getName().toLowerCase() + ".gif"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.skill = skill;
        }

        public void recalculate() {
            int xpDelta = lastXp - startXp;
            double xpPerMilli;
            if (updatedTime == startTime) {
                xpPerMilli = (double) xpDelta / (double) 1000;
            } else {
                xpPerMilli = ((double) xpDelta / (double) (updatedTime - startTime));
            }
            xpPerHour = (long) (xpPerMilli * 3600000.0F); // XP per millisecond
            timeToLevel = (long) ((double) skills.getExperienceToLevel(skill, lastLevel + 1) / xpPerMilli);
            xpToLevel = skills.getExperienceAtLevel(lastLevel + 1) - lastXp;
            int baseXp = skills.getExperienceAtLevel(lastLevel);
            percentileToLevel = (float) (lastXp - baseXp) / (float) (skills.getExperienceAtLevel(lastLevel + 1) - baseXp);
        }

        public boolean isActive() {
            return System.currentTimeMillis() - updatedTime < 900000 && startTime != 0; // If there's been some XP gain in the last 15 minutes the tracker is active.
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - updatedTime > 300000;
        }

        public void reset() {
            startLevel = 0;
            startXp = 0;
            lastLevel = 0;
            lastXp = 0;
            xpToLevel = 0;
            xpGained = 0;
            startTime = 0;
            updatedTime = 0;
            xpPerHour = 0;
            timeToLevel = 0;
            percentileToLevel = 0;
        }
    }
}

package com.kit.plugins.xptracker;

import com.kit.Application;
import com.kit.gui.component.MateProgressBar;
import com.kit.gui.component.MateScrollPane;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;
import com.kit.Application;
import com.kit.api.wrappers.World;
import com.kit.core.Session;
import com.kit.gui.component.MateProgressBar;
import com.kit.gui.component.SidebarWidget;
import com.kit.plugins.quickhop.QuickHopSidebarWidget;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.time.Duration;
import java.util.*;

/**
 */
public class XPTrackerSidebarWidget extends JPanel implements SidebarWidget {
    private final Image NORMAL_ICON = IconFontSwing.buildImage(FontAwesome.LINE_CHART, 20, Color.GRAY);
    private final Image TOGGLED_ICON = IconFontSwing.buildImage(FontAwesome.LINE_CHART, 20, Color.WHITE);
    private final Map<XPTrackerPlugin.TrackedSkill, SkillWidget> widgetMap = new HashMap<>();
    private final XPTrackerPlugin plugin;
    private final JPanel container;
    private final Box spacing;

    public XPTrackerSidebarWidget(XPTrackerPlugin plugin) {
        this.plugin = plugin;
        setLayout(new BorderLayout());

        spacing = Box.createVerticalBox();

        container = new JPanel();
        container.setLayout(new MigLayout("insets 0, gap rel 0"));
        container.setBackground(Application.COLOUR_SCHEME.getDark());

        container.add(spacing, "span, grow, pushy, wrap push");

        MateScrollPane scrollPane = new MateScrollPane(container);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(75);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void update() {
        for (XPTrackerPlugin.TrackedSkill skill : plugin.getTrackedSkills()) {
            SkillWidget widget = widgetMap.get(skill);
            if (widget != null) {
                widget.update();
            } else if (skill.isActive()) {
                widget = new SkillWidget(skill, widgetMap.size());
                widgetMap.put(skill, widget);
                container.add(widget, "push, growx, span");
            }
        }

        container.remove(spacing);
        container.add(spacing, "span, grow, pushy, wrap push");
        invalidate();
        repaint();
    }

    @Override
    public Component getContent() {
        return this;
    }

    @Override
    public String getTitle() {
        return "XP Tracker";
    }

    @Override
    public Image getIcon(boolean toggled) {
        return toggled ? TOGGLED_ICON : NORMAL_ICON;
    }

    private class SkillWidget extends JPanel {
        private final XPTrackerPlugin.TrackedSkill skill;
        private final MateProgressBar progressBar;
        private final JLabel xpGained;
        private final JLabel xpToLevel;
        private final JLabel timeToLevel;

        private SkillWidget(XPTrackerPlugin.TrackedSkill skill, int idx) {
            this.skill = skill;

            setLayout(new MigLayout("insets 0, gap rel 0"));
            setBackground(idx % 2 == 0 ? Application.COLOUR_SCHEME.getBright() : Application.COLOUR_SCHEME.getDark());
            setMinimumSize(new Dimension(200, 75));


            //row 1
            JLabel skillLbl = new JLabel(new ImageIcon(skill.icon));
            skillLbl.setForeground(Application.COLOUR_SCHEME.getText());
            add(skillLbl, "gapleft 10, gaptop 10");

            this.progressBar = new MateProgressBar();
            progressBar.setMinimumSize(new Dimension(160, 20));
            progressBar.setPreferredSize(new Dimension(160, 20));
            progressBar.setMaximumSize(new Dimension(160, 20));
            progressBar.setBackgroundColour(Color.RED.darker().darker());
            progressBar.setForegroundColour(Color.GREEN.darker());
            progressBar.setBorderColour(Color.WHITE.darker());
            progressBar.setBorderWidth(1);
            progressBar.setMinimum(0);
            progressBar.setMaximum(100);
            progressBar.setValue(0);
            progressBar.setShowText(true);
            progressBar.setShowPercentage(true);

            add(progressBar, "gaptop 7, gapright 10, pushx, growx, wrap");

            JPanel detailPanel = new JPanel(new MigLayout("insets 0, gap rel 0"));
            detailPanel.setBackground(getBackground());
//
//
            JLabel xpGainedP = new JLabel("XP gained:");
            xpGainedP.setFont(xpGainedP.getFont().deriveFont(Font.BOLD, 9f));
            xpGainedP.setForeground(Application.COLOUR_SCHEME.getText());
            detailPanel.add(xpGainedP, "gapleft 40, gaptop 10");

            xpGained = new JLabel("N/A");
            xpGained.setFont(xpGained.getFont().deriveFont(Font.PLAIN, 9f));
            xpGained.setForeground(Application.COLOUR_SCHEME.getText());
            detailPanel.add(xpGained, "pushx");

            JLabel xpToLevelP = new JLabel("XP left:");
            xpToLevelP.setFont(xpGainedP.getFont().deriveFont(Font.BOLD, 9f));
            xpToLevelP.setForeground(Application.COLOUR_SCHEME.getText());
            detailPanel.add(xpToLevelP);

            xpToLevel = new JLabel("N/A");
            xpToLevel.setFont(xpToLevel.getFont().deriveFont(Font.PLAIN, 9f));
            xpToLevel.setHorizontalAlignment(SwingConstants.RIGHT);
            xpToLevel.setForeground(Application.COLOUR_SCHEME.getText());
            detailPanel.add(xpToLevel, "growx, pushx, gapright 10, span, wrap");

            timeToLevel = new JLabel("N/A");
            timeToLevel.setFont(timeToLevel.getFont().deriveFont(Font.PLAIN, 9f));
            timeToLevel.setForeground(Application.COLOUR_SCHEME.getText());
            detailPanel.add(timeToLevel, "pushx, growx, gapleft 40, gaptop 10, gapright 10, span");

            add(detailPanel, "span, pushx, growx, gapbottom 10");
        }

        public void update() {
            progressBar.setValue((int) (skill.percentileToLevel * 100));
            xpGained.setText(String.valueOf((skill.xpGained + (skill.lastXp - skill.startXp))));
            xpToLevel.setText(String.format("%d XP", skill.xpToLevel));

            Duration duration = Duration.ofMillis(skill.timeToLevel);
            String s = String.format("%02d:%02d:%02d%n until level-up", duration.toHours(),
                    duration.minusHours(duration.toHours()).toMinutes(),
                    duration.minusMinutes(duration.toMinutes()).getSeconds());
            timeToLevel.setText(s);
        }
    }
}
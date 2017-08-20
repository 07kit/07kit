package com.kit.plugins.hiscore;

import com.kit.Application;
import com.kit.api.wrappers.Skill;
import com.kit.api.wrappers.hiscores.HiscoreLookup;
import com.kit.gui.component.MateProgressBar;
import com.kit.gui.component.MateTextField;
import com.kit.Application;
import com.kit.api.wrappers.Skill;
import com.kit.api.wrappers.hiscores.HiscoreLookup;
import com.kit.api.wrappers.hiscores.HiscoreSkill;
import com.kit.gui.component.MateProgressBar;
import com.kit.gui.component.MateTextField;
import com.kit.gui.component.SidebarWidget;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class HiscoreSidebarWidget extends JPanel implements SidebarWidget {
    private Map<Skill, HiscoreSkill> skillResults = new HashMap<>();
    private final Map<Skill, JLabel> skillLabels = new HashMap<>();
    private final MateTextField usernameText;
    private final JLabel rankLabel;
    private final JLabel xpLabel;
    private final JLabel xpLeftLabel;
    private final MateProgressBar xpLeftProgress;
    private final HiscorePlugin plugin;
    private final Image NORMAL_ICON = IconFontSwing.buildImage(FontAwesome.BAR_CHART, 20, Color.GRAY);
    private final Image TOGGLED_ICON = IconFontSwing.buildImage(FontAwesome.BAR_CHART, 20, Color.WHITE);

    public HiscoreSidebarWidget(HiscorePlugin plugin) {
        this.plugin = plugin;
        setBackground(Application.COLOUR_SCHEME.getDark());
        setLayout(new MigLayout("insets 0, gap rel 0", "[]0[]0[]"));

        usernameText = new MateTextField("Username");
        usernameText.setMargin(new Insets(0, 0, 0, 0));
        usernameText.setMinimumSize(new Dimension(210, 35));
        usernameText.setBackground(Application.COLOUR_SCHEME.getDark());
        usernameText.setHorizontalAlignment(SwingConstants.CENTER);
        usernameText.setForeground(Application.COLOUR_SCHEME.getText());
        usernameText.addActionListener(e -> plugin.search(usernameText.getText()));
        usernameText.setBorder(BorderFactory.createLineBorder(Application.COLOUR_SCHEME.getText()));
        add(usernameText, "span, growx, pushx");

        // All the skills lol
        int x = 0;
        int y = 2;

        // Skills ordered in the way we want to render them (LTR)
        Skill[] skills = new Skill[]{Skill.ATTACK, Skill.HITPOINTS, Skill.MINING, Skill.STRENGTH,
                Skill.AGILITY, Skill.SMITHING, Skill.DEFENCE, Skill.HERBLORE, Skill.FISHING, Skill.RANGED, Skill.THIEVING,
                Skill.COOKING, Skill.PRAYER, Skill.CRAFTING, Skill.FIREMAKING, Skill.MAGIC, Skill.FLETCHING, Skill.WOODCUTTING,
                Skill.RUNECRAFTING, Skill.SLAYER, Skill.FARMING, Skill.CONSTRUCTION, Skill.HUNTER};
        for (final Skill s : skills) {
            try {
                Image skillIcon = Application.SKILL_IMAGE_ICONS.get(s);
                JPanel skillPanel = new JPanel(new BorderLayout());
                skillPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
                skillPanel.getInsets().set(0, 0, 0, 0);
                skillPanel.setBackground(Application.COLOUR_SCHEME.getDark());
                skillPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (skillResults == null || skillResults.size() == 0) {
                            return;
                        }
                        rankLabel.setText(String.valueOf(skillResults.get(s).getRank()));
                        xpLabel.setText(String.valueOf(skillResults.get(s).getExperience()));

                        int level = skillResults.get(s).getLevel();
                        long experience = skillResults.get(s).getExperience();
                        long experienceBase = plugin.skills.getExperienceAtLevel(level);
                        long experienceGoal = plugin.skills.getExperienceAtLevel(level + 1);
                        long experienceReq = experienceGoal - experienceBase;
                        long experienceLeft = experienceGoal - experience;
                        long experienceProg = experience - experienceBase; // gained so far
                        xpLeftLabel.setText(String.valueOf(experienceLeft));

                        float percentageComplete = ((float) experienceProg / (float) experienceReq) * 100;
                        xpLeftProgress.setValue(percentageComplete);
                    }
                });

                JLabel skillIconLabel = new JLabel(new ImageIcon(skillIcon));
                skillIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
                skillPanel.add(skillIconLabel, BorderLayout.WEST);
                skillIconLabel.setOpaque(false);

                JLabel skillLevelLabel = new JLabel("1");
                skillLevelLabel.setFont(skillLevelLabel.getFont().deriveFont(Font.BOLD));
                skillLevelLabel.setHorizontalAlignment(SwingConstants.CENTER);
                skillLevelLabel.setForeground(Application.COLOUR_SCHEME.getText());
                skillLevelLabel.setOpaque(false);
                skillPanel.add(skillLevelLabel, BorderLayout.EAST);
                skillLabels.put(s, skillLevelLabel);


                x++;

                /* Move down a row */
                if (x == 3) {
                    add(skillPanel, "growx, pushx, wrap");
                    x = 0;
                    y++;
                } else {
                    add(skillPanel, "growx, pushx");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        add(Box.createVerticalStrut(30), "growx, pushx, span");

        GridBagConstraints hoverConstraints = new GridBagConstraints();
        JPanel hoverPanel = new JPanel(new GridBagLayout());
        hoverPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        hoverPanel.setBackground(Application.COLOUR_SCHEME.getBright());

        JLabel rankPrefix = new JLabel("Rank: ");
        rankPrefix.setForeground(Application.COLOUR_SCHEME.getText());
        rankLabel = new JLabel("Click a skill.");
        rankLabel.setFont(rankLabel.getFont().deriveFont(Font.BOLD));
        rankLabel.setForeground(Application.COLOUR_SCHEME.getText());

        hoverConstraints = new GridBagConstraints();
        hoverConstraints.fill = GridBagConstraints.HORIZONTAL;
        hoverConstraints.anchor = GridBagConstraints.NORTH;
        hoverConstraints.gridx = 0;
        hoverConstraints.gridy = GridBagConstraints.RELATIVE;
        hoverConstraints.weightx = 1.0;
        hoverPanel.add(rankPrefix, hoverConstraints);
        hoverConstraints.gridx = 1;
        hoverPanel.add(rankLabel, hoverConstraints);

        JLabel xpPrefix = new JLabel("XP: ");
        xpPrefix.setForeground(Application.COLOUR_SCHEME.getText());
        xpLabel = new JLabel("Click a skill.");
        xpLabel.setFont(xpLabel.getFont().deriveFont(Font.BOLD));
        xpLabel.setForeground(Application.COLOUR_SCHEME.getText());

        hoverConstraints = new GridBagConstraints();
        hoverConstraints.fill = GridBagConstraints.HORIZONTAL;
        hoverConstraints.anchor = GridBagConstraints.NORTH;
        hoverConstraints.gridx = 0;
        hoverConstraints.gridy = GridBagConstraints.RELATIVE;
        hoverConstraints.weightx = 1.0;
        hoverPanel.add(xpPrefix, hoverConstraints);
        hoverConstraints.gridx = 1;
        hoverPanel.add(xpLabel, hoverConstraints);

        JLabel xpLeftPrefix = new JLabel("XP to level: ");
        xpLeftPrefix.setForeground(Application.COLOUR_SCHEME.getText());
        xpLeftLabel = new JLabel("Click a skill.");
        xpLeftLabel.setFont(xpLeftLabel.getFont().deriveFont(Font.BOLD));
        xpLeftLabel.setForeground(Application.COLOUR_SCHEME.getText());

        hoverConstraints = new GridBagConstraints();
        hoverConstraints.fill = GridBagConstraints.HORIZONTAL;
        hoverConstraints.anchor = GridBagConstraints.NORTH;
        hoverConstraints.gridx = 0;
        hoverConstraints.gridy = GridBagConstraints.RELATIVE;
        hoverConstraints.weightx = 1.0;
        hoverPanel.add(xpLeftPrefix, hoverConstraints);
        hoverConstraints.gridx = 1;
        hoverPanel.add(xpLeftLabel, hoverConstraints);

        hoverConstraints = new GridBagConstraints();
        hoverConstraints.fill = GridBagConstraints.HORIZONTAL;
        hoverConstraints.anchor = GridBagConstraints.NORTH;
        hoverConstraints.gridx = 0;
        hoverConstraints.gridy = GridBagConstraints.RELATIVE;
        hoverConstraints.weightx = 1.0;
        hoverPanel.add(Box.createVerticalStrut(10), hoverConstraints);

        xpLeftProgress = new MateProgressBar();
        xpLeftProgress.setBorderColour(Application.COLOUR_SCHEME.getText());
        xpLeftProgress.setBorderWidth(1);
        xpLeftProgress.setMinimum(0);
        xpLeftProgress.setMaximum(100);
        xpLeftProgress.setValue(50);

        hoverConstraints = new GridBagConstraints();
        hoverConstraints.fill = GridBagConstraints.HORIZONTAL;
        hoverConstraints.anchor = GridBagConstraints.NORTH;
        hoverConstraints.gridx = 0;
        hoverConstraints.gridy = GridBagConstraints.RELATIVE;
        hoverConstraints.ipady = 5;
        hoverConstraints.gridwidth = 3;
        hoverConstraints.weightx = 1.0;
        hoverPanel.add(xpLeftProgress, hoverConstraints);

        add(Box.createVerticalBox(), "span, grow, pushy, wrap push");
        add(hoverPanel, "growx, pushx, span, dock south");
    }

    public void setHiscores(String player, HiscoreLookup results) {
        if (results != null) {
            skillResults = results.getSkills();
            for (Map.Entry<Skill, HiscoreSkill> skillEntry : skillResults.entrySet()) {
                JLabel skillLevelLabel = skillLabels.get(skillEntry.getKey());
                if (skillLevelLabel != null) {
                    skillLevelLabel.setText(String.valueOf(skillEntry.getValue().getLevel()));
                    skillLevelLabel.repaint();
                }
            }
        } else {
            usernameText.setBackground(Color.RED.darker().darker());
            skillResults = null;
            for (Skill skill : Skill.values()) {
                JLabel skillLevelLabel = skillLabels.get(skill);
                if (skillLevelLabel != null) {
                    skillLevelLabel.setText("-");
                    skillLevelLabel.repaint();
                }
            }
        }
        usernameText.setText(player);
    }


    @Override
    public Component getContent() {
        return this;
    }

    @Override
    public String getTitle() {
        return "Hiscore Lookup";
    }

    @Override
    public Image getIcon(boolean toggled) {
        return toggled ? TOGGLED_ICON : NORMAL_ICON;
    }


}

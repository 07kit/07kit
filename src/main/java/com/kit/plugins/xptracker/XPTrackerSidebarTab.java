package com.kit.plugins.xptracker;

import com.kit.api.plugin.SidebarTab;
import com.kit.jfx.JFX;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class XPTrackerSidebarTab implements SidebarTab {

    private static class SkillController {

        private final XPTrackerPlugin.TrackedSkill skill;

        @FXML
        private Label nameLabel;

        @FXML
        private ProgressBar progressBar;

        public SkillController(XPTrackerPlugin.TrackedSkill skill) {
            this.skill = skill;
        }

        public void update() {
            nameLabel.setText(skill.skill.getName());
            progressBar.setProgress(skill.percentileToLevel);
        }

    }

    private final XPTrackerPlugin plugin;
    private final HashMap<XPTrackerPlugin.TrackedSkill, SkillController> skillMap = new HashMap<>();

    @FXML
    private VBox vbox;

    public XPTrackerSidebarTab(XPTrackerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Parent root() {
        return JFX.load("sidebar-xptracker", "jfx/views/plugins/xpTracker/xp-sidebar.fxml", this);
    }

    @Override
    public String title() {
        return "XP Tracker";
    }

    @Override
    public Image image() {
        return null;
    }

    @Override
    public FontAwesomeIcon icon() {
        return FontAwesomeIcon.BAR_CHART;
    }

    public void update() {
        Platform.runLater(() -> {
            for (XPTrackerPlugin.TrackedSkill skill : plugin.getTrackedSkills()) {
                SkillController controller = skillMap.get(skill);
                if (controller != null) {
                    controller.update();
                } else if (skill.isActive()) {
                    controller = new SkillController(skill);
                    Parent parent = JFX.load(skill.skill.getName() + "-tracker", "jfx/views/plugins/xpTracker/skill-view.fxml", controller);
                    skillMap.put(skill, controller);
                    vbox.getChildren().add(parent);
                }
            }
        });
    }

}

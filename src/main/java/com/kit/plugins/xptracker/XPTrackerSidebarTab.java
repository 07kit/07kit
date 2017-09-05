package com.kit.plugins.xptracker;

import com.kit.api.plugin.SidebarTab;
import com.kit.jfx.JFX;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

import java.time.Duration;
import java.util.HashMap;

public class XPTrackerSidebarTab implements SidebarTab {

    private static class SkillController {

        private final XPTrackerPlugin.TrackedSkill skill;

        @FXML
        private ProgressBar progressBar;

        @FXML
        private Label gained;

        @FXML
        private Label left;

        @FXML
        private Label perHour;

        @FXML
        private Label timeLeft;
        @FXML
        private ImageView skillImage;

        @FXML
        private VBox pane;

        private final ContextMenu contextMenu;

        public SkillController(XPTrackerPlugin.TrackedSkill skill) {
            this.skill = skill;
            contextMenu = new ContextMenu();
            MenuItem reset = new MenuItem("Reset");
            reset.setOnAction(event -> {
                XPTrackerSidebarTab tab = JFX.controller("sidebar-xptracker");
                tab.reset(skill);
            });
            contextMenu.getItems().add(reset);
        }

        public void init() {
            skillImage.setImage(skill.image);
            pane.setOnMouseClicked(event -> {
                if (!event.getButton().equals(MouseButton.SECONDARY))
                    return;
                contextMenu.show(pane, event.getScreenX(), event.getScreenY());
            });
            update();
        }

        public void update() {
            skillImage.setImage(skill.image);
            progressBar.setProgress(skill.percentileToLevel);
            gained.setText(String.valueOf(skill.xpGained + (skill.lastXp - skill.startXp)));
            left.setText(String.valueOf(skill.xpToLevel));
            perHour.setText(String.valueOf(skill.xpPerHour));

            Duration duration = Duration.ofMillis(skill.timeToLevel);
            String s = String.format("%02d:%02d:%02d", duration.toHours(),
                    duration.minusHours(duration.toHours()).toMinutes(),
                    duration.minusMinutes(duration.toMinutes()).getSeconds());
            timeLeft.setText(s);
        }

    }

    private final XPTrackerPlugin plugin;
    private final HashMap<XPTrackerPlugin.TrackedSkill, SkillController> skillMap = new HashMap<>();
    private final HashMap<XPTrackerPlugin.TrackedSkill, Parent> skillParentMap = new HashMap<>();

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
                    skillParentMap.put(skill, parent);
                    controller.init();
                    vbox.getChildren().add(parent);
                }
            }
        });
    }

    public void reset(XPTrackerPlugin.TrackedSkill skill) {
        final Parent parent = skillParentMap.get(skill);
        Platform.runLater(() -> {
            if (parent == null)
                return;
            vbox.getChildren().remove(parent);
        });
        skillMap.remove(skill);
        skillParentMap.remove(skill);
        skill.reset();
    }

}

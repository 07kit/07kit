package com.kit.jfx.controllers;

import com.kit.Application;
import com.kit.api.plugin.SidebarTab;
import com.kit.jfx.JFX;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

import java.util.Objects;

public class SidebarController {

    @FXML
    private TabPane tabPane;

    public void add(SidebarTab tab) {
        Platform.runLater(() -> {
            Tab newTab = new Tab();
            newTab.setId(tab.title());
            newTab.setContent(tab.root());
            newTab.setClosable(false);
            newTab.getStyleClass().add("green");
            if (tab.icon() != null) {
                FontAwesomeIconView iconView = new FontAwesomeIconView(tab.icon());
                iconView.getStyleClass().add("fontawesome-dark");
                iconView.getStyleClass().add("medium");
                newTab.setGraphic(iconView);
            } else if (tab.image() != null) {
                newTab.setGraphic(new ImageView(tab.image()));
            } else {
                newTab.setText(tab.title());
            }
            tabPane.getTabs().add(newTab);
        });
    }

    public void remove(SidebarTab tab) {
        Platform.runLater(() -> {
            Tab remove = tabPane.getTabs().stream().filter(jfxTab -> Objects.equals(jfxTab.getId(), tab.title())).findFirst().orElse(null);
            if (remove == null)
                return;
            tabPane.getTabs().remove(remove);
        });
    }



}

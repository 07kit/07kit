package com.kit.plugins.test;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TestPluginController {

    @FXML
    private Label counter;

    public void setCounter(int value) {
        Platform.runLater(() -> {
            counter.setText("Counted to: " + value);
        });

    }

}

package com.kit.api.plugin;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Parent;
import javafx.scene.image.Image;

public interface SidebarTab {

    Parent root();

    String title();

    Image image();

    FontAwesomeIcon icon();

}

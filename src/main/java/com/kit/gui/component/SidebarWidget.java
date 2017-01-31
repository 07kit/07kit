package com.kit.gui.component;

import javax.swing.*;
import java.awt.*;

/**
 */
public interface SidebarWidget {

    /**
     * The content of the sidebar pane, typically a JPanel
     *
     * @return sidebar content
     */
    Component getContent();

    /**
     * Title of the sidebar pane
     *
     * @return title
     */
    String getTitle();

    /**
     * Icon for the toggle button on the sidebar
     *
     * @return icon
     */
    Image getIcon(boolean toggled);

}

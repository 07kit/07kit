package com.kit.api;

import com.kit.api.overlay.BoxOverlay;
import com.kit.gui.component.SidebarWidget;
import com.kit.api.overlay.BoxOverlay;
import com.kit.gui.component.SidebarWidget;

/**
 */
public interface UserInterface {

    void registerSidebarWidget(SidebarWidget widget);

    void deregisterSidebarWidget(SidebarWidget widget);

    void registerBoxOverlay(BoxOverlay boxOverlay);

    void deregisterBoxOverlay(BoxOverlay boxOverlay);

    boolean isFocused();
}

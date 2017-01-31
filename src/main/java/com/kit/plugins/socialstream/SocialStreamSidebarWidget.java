package com.kit.plugins.socialstream;

import com.kit.Application;
import com.kit.gui.component.LoadingContainer;
import com.kit.gui.component.MateScrollPane;
import com.kit.util.LimitedHashMap;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import com.kit.Application;
import com.kit.gui.component.LoadingContainer;
import com.kit.gui.component.SidebarWidget;
import com.kit.http.TwitchStream;
import com.kit.util.LimitedHashMap;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SocialStreamSidebarWidget extends MateScrollPane implements SidebarWidget {

    public static final int CACHE_SIZE = 500;

    private final Image NORMAL_ICON = IconFontSwing.buildImage(FontAwesome.USERS, 20, Color.GRAY);
    private final Image TOGGLED_ICON = IconFontSwing.buildImage(FontAwesome.USERS, 20, Color.WHITE);

    private JPanel loadingPanel;
    private Map<String, Image> imageCache;

    public SocialStreamSidebarWidget() {
        imageCache = new LimitedHashMap<>(CACHE_SIZE);
        loadingPanel = createLoadingContainer();

        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getVerticalScrollBar().setUnitIncrement(75);
        setViewportView(loadingPanel);
        setBorder(null);
    }

    private JPanel createLoadingContainer() {
        return new LoadingContainer();
    }

    public void createContainer(List<StreamWidget> streamPanels) {
        SwingUtilities.invokeLater(() -> {
            setViewportView(loadingPanel);
            JPanel container = new JPanel();
            container.setBackground(Application.COLOUR_SCHEME.getDark().brighter());
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            container.getInsets().set(0, 10, 10, 10);

            for (int i = 0; i < streamPanels.size(); i++) {
                StreamWidget streamPanel = streamPanels.get(i);
                streamPanel.getInnerPanel().setBackground(i % 2 == 0 ? Application.COLOUR_SCHEME.getBright() : Application.COLOUR_SCHEME.getDark());
                streamPanel.setBackground(i % 2 == 0 ? Application.COLOUR_SCHEME.getBright() : Application.COLOUR_SCHEME.getDark());
                container.add(streamPanel, "push, growx, span");
            }
            Box spacing = Box.createVerticalBox();
            container.add(spacing, "span, grow, pushy, wrap push");

            setViewportView(container);
            setBorder(null);
        });
    }

    @Override
    public Component getContent() {
        return this;
    }

    @Override
    public String getTitle() {
        return "Social";
    }

    @Override
    public Image getIcon(boolean toggled) {
        return toggled ? TOGGLED_ICON : NORMAL_ICON;
    }


    public Map<String, Image> getImageCache() {
        return imageCache;
    }
}

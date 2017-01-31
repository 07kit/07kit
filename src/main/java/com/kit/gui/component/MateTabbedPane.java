package com.kit.gui.component;

import net.miginfocom.swing.MigLayout;
import com.kit.Application;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class MateTabbedPane extends JPanel {

    private final List<Tab> tabs = new ArrayList<>();
    private final JToolBar toolBar;
    private final JPanel root;

    public MateTabbedPane() {
        setLayout(new MigLayout("insets 0, gap rel 0"));

        toolBar = new JToolBar();
        toolBar.setMinimumSize(new Dimension(0, 40));
        toolBar.setFloatable(false);
        toolBar.setBackground(Application.COLOUR_SCHEME.getDark().brighter());
        toolBar.setForeground(Application.COLOUR_SCHEME.getText());
        add(toolBar, "dock north, growx, wrap");

        root = new JPanel();
        root.setLayout(new MigLayout("insets 0, gap rel 0"));
        root.setBackground(Application.COLOUR_SCHEME.getDark());
        add(root, "grow, push");
    }

    public Tab addTab(String text, Component component) {
        Tab tab = new Tab(text, component);
        toolBar.add(tab);
        tabs.add(tab);

        toolBar.invalidate();
        toolBar.repaint();
        return tab;
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    public JPanel getRoot() {
        return root;
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    public Tab getSelected() {
        return tabs.stream().filter(Tab::isSelected).findFirst().orElse(null);
    }

    public Tab getAt(int idx) {
        return tabs.size() > idx ? tabs.get(idx) : null;
    }

    @Override
    public void removeAll() {
        // Don't disturb this container, motherfucker...
        toolBar.removeAll();
        tabs.clear();
        root.removeAll();
    }

    public void removeTab(Tab tab) {
        if (tab.isSelected()) {
            tab.deselect();
        }
        tabs.remove(tab);
        toolBar.remove(tab);
        toolBar.invalidate();
        toolBar.repaint();
    }

    public class Tab extends MateButton {
        private String name;
        private Component component;
        private boolean selected;

        public Tab(String name, Component component) {
            super(name);

            setMinimumSize(new Dimension(80, 40));
            setPreferredSize(new Dimension(230, 40));
            setMaximumSize(new Dimension(230, 40));

            setOpaque(true);

            setFont(getFont().deriveFont(16f));
            addActionListener(e -> {
                select();
            });

            this.name = name;
            this.component = component;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Component getComponent() {
            return component;
        }

        public void setComponent(Component component) {
            this.component = component;
        }

        @Override
        public boolean isSelected() {
            return selected;
        }

        public void select() {
            tabs.forEach(Tab::deselect);
            root.removeAll();
            root.add(component, "grow, push");

            setBackground(Application.COLOUR_SCHEME.getHighlight());
            MateTabbedPane.this.invalidate();
            MateTabbedPane.this.revalidate();
            MateTabbedPane.this.repaint();
            selected = true;
        }

        public void deselect() {
            root.remove(component);
            setBackground(Application.COLOUR_SCHEME.getDark());
            selected = false;
        }
    }
}

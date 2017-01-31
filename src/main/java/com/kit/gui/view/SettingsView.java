package com.kit.gui.view;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.kit.Application;
import com.kit.api.plugin.Plugin;
import com.kit.core.Session;
import com.kit.gui.component.MateTabbedPane;
import com.kit.Application;
import com.kit.api.plugin.Plugin;
import com.kit.core.Session;
import com.kit.gui.component.MateTabbedPane;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.List;

/**
 */
public class SettingsView extends JPanel {
    private final MateTabbedPane tabbedPane = new MateTabbedPane();

    public SettingsView() {
        setMinimumSize(new Dimension(800, 480));
        setPreferredSize(new Dimension(800, 480));
        setLayout(new BorderLayout());

        add(tabbedPane, BorderLayout.CENTER);

    }

    public void load() {
        tabbedPane.removeAll();

        List<Plugin> plugins = Session.get().getPluginManager().getPlugins();
        Multimap<String, Plugin> pluginGroups = Multimaps.index(plugins, Plugin::getGroup);
        for (String groupName : pluginGroups.keySet()) {
            PluginGroup group = new PluginGroup(groupName, pluginGroups.get(groupName));
            tabbedPane.addTab(group.getName(), group);
        }

        MateTabbedPane.Tab first = tabbedPane.getAt(0);
        if (first != null) {
            first.select();
        }
    }

    private class PluginGroup extends JPanel {
        private final JList<PluginListItem> pluginsList;
        private final JPanel contentView;
        private final String name;

        public PluginGroup(String name, Collection<Plugin> plugins) {
            this.name = name;

            setLayout(new BorderLayout());
            setMinimumSize(new Dimension(800, 460));
            setPreferredSize(new Dimension(800, 460));
            setMaximumSize(new Dimension(800, 460));

            setBackground(Application.COLOUR_SCHEME.getHighlight().brighter().brighter().brighter());

            pluginsList = new JList<>();
            pluginsList.setPreferredSize(new Dimension(165, 500));
            pluginsList.setBackground(Application.COLOUR_SCHEME.getDark().darker());
            pluginsList.setForeground(Application.COLOUR_SCHEME.getText());
            pluginsList.setFont(pluginsList.getFont().deriveFont(Font.BOLD, 14f));
            add(pluginsList, BorderLayout.WEST);

            contentView = new JPanel();
            contentView.setLayout(new BorderLayout());
            contentView.setBackground(Application.COLOUR_SCHEME.getDark());
            add(contentView, BorderLayout.CENTER);

            pluginsList.addListSelectionListener(e -> {
                PluginListItem item = pluginsList.getSelectedValue();
                PluginSettingsView view = new PluginSettingsView(item.plugin);
                contentView.removeAll();
                contentView.add(view, BorderLayout.CENTER);
                contentView.revalidate();
                contentView.repaint();
            });

            DefaultListModel<PluginListItem> model = new DefaultListModel<>();
            plugins.stream().filter(Plugin::hasOptions).forEach(plugin -> {
                model.addElement(new PluginListItem(plugin));
            });
            pluginsList.setModel(model);
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private static class PluginListItem {
        private final Plugin plugin;

        private PluginListItem(Plugin plugin) {
            this.plugin = plugin;
        }

        public String toString() {
            return plugin.getName();
        }

    }

}

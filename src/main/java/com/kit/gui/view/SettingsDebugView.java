package com.kit.gui.view;


import com.kit.gui.component.MateScrollPane;
import com.kit.gui.controller.SettingsDebugController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * Date: 05/11/13
 * Time: 09:29
 *
 * @author Matt Collinge
 */
public class SettingsDebugView extends JDialog implements ListSelectionListener {

    // Controller instance
    private SettingsDebugController settingsDebugController;

    // Components
    private JList<String> settingsList;
    private JTable settingsInfoTable;
    private JTextArea settingsLogArea;

    public SettingsDebugView(final SettingsDebugController settingsDebugController) {
        this.settingsDebugController = settingsDebugController;

        setTitle("Settings Explorer Debug");

        settingsList = new JList<>();
        settingsList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        settingsList.addListSelectionListener(this);
        MateScrollPane listScrollPane = new MateScrollPane(settingsList);
        listScrollPane.setPreferredSize(new Dimension(200, 400));
        add(listScrollPane, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        settingsInfoTable = new JTable();
        settingsInfoTable.setPreferredSize(new Dimension(400, 75));
        infoPanel.add(settingsInfoTable);

        settingsLogArea = new JTextArea();
        settingsLogArea.setEditable(false);
        MateScrollPane logAreaScrollPane = new MateScrollPane(settingsLogArea);
        logAreaScrollPane.setPreferredSize(new Dimension(400, 325));
        infoPanel.add(logAreaScrollPane);

        add(infoPanel, BorderLayout.CENTER);

        pack();
    }

    public JList<String> getSettingsList() {
        return settingsList;
    }

    public JTable getSettingsInfoTable() {
        return settingsInfoTable;
    }

    public JTextArea getSettingsLogArea() {
        return settingsLogArea;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        settingsDebugController.onListSelectionChanged();
    }
}

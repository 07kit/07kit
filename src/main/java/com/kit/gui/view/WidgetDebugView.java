package com.kit.gui.view;

import com.kit.gui.component.MateScrollPane;
import com.kit.gui.component.WidgetTreeModel;
import com.kit.gui.controller.WidgetDebugController;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author const_
 * @author A_C
 */
public class WidgetDebugView extends JDialog implements TreeSelectionListener, ActionListener {

    private WidgetDebugController widgetDebugController;

    private JTree widgetTree;
    private JTextField searchBox;
    private JButton searchButton;
    private JButton refreshButton;
    private JTable infoTable;

    private WindowAdapter adapter = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosed(e);
            widgetDebugController.hide();
        }
    };

    public WidgetDebugView(final WidgetDebugController widgetDebugController) {
        this.widgetDebugController = widgetDebugController;
        addWindowListener(adapter);
        setTitle("Widget Explorer Debug");
        widgetTree = new JTree();
        widgetTree.addTreeSelectionListener(this);
        widgetTree.setRootVisible(false);
        widgetTree.setEditable(false);
        widgetTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        MateScrollPane scrollPane = new MateScrollPane(widgetTree);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        add(scrollPane, BorderLayout.WEST);

        infoTable = new JTable();
        infoTable.setPreferredSize(new Dimension(300, 300));
        add(infoTable, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.add(new JLabel("Search String: "));

        searchBox = new JTextField(25);
        searchPanel.add(searchBox);

        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchPanel.add(searchButton);

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(this);
        searchPanel.add(refreshButton);

        add(searchPanel, BorderLayout.NORTH);

        pack();
    }

    public JTree getWidgetTree() {
        return widgetTree;
    }

    public JTable getInfoTable() {
        return infoTable;
    }

    public JTextField getSearchBox() {
        return searchBox;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (widgetTree.getLastSelectedPathComponent() instanceof WidgetTreeModel.WidgetTreeWrapper) {
            widgetDebugController.onWidgetSelected();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand().toLowerCase()) {
            case "search":
                widgetDebugController.onSearch();
                break;
            case "refresh":
                widgetDebugController.onRefresh();
                break;
        }
    }
}

package com.kit.gui.view;

import com.kit.Application;
import com.kit.Application;
import com.kit.gui.controller.SidebarController;
import net.miginfocom.swing.MigLayout;
import com.kit.Application;
import com.kit.gui.controller.SidebarController;

import javax.swing.*;
import java.awt.*;

/**
 */
public class SidebarView extends JPanel {
    private static final Dimension STD_SIZE = new Dimension(245, 500);
    private static final Dimension TOOLBAR_SIZE = new Dimension(35, 500);
    private static final Dimension TITLE_SIZE = new Dimension(210, 40);
    private final SidebarController controller;
    private final JToolBar toolbar;
    private final JPanel content;
    private final JLabel title;

    public SidebarView(SidebarController controller) {
        this.controller = controller;
        setBackground(Application.COLOUR_SCHEME.getDark());
        setMinimumSize(STD_SIZE);
        setPreferredSize(STD_SIZE);
        setLayout(new BorderLayout());

        toolbar = new JToolBar("", JToolBar.VERTICAL);
        toolbar.setBorder(null);
        toolbar.setBackground(Application.COLOUR_SCHEME.getDark());
        toolbar.setFloatable(false);
        toolbar.setPreferredSize(TOOLBAR_SIZE);
        JPanel strut = new JPanel();
        strut.setPreferredSize(new Dimension(35, 40));
        strut.setMaximumSize(new Dimension(35, 40));
        strut.setOpaque(false);
        toolbar.add(strut);
        add(toolbar, BorderLayout.WEST);

        content = new JPanel();
        content.setBackground(Application.COLOUR_SCHEME.getDark());
        content.setLayout(new MigLayout("insets 0, gap rel 0"));

        title = new JLabel("", JLabel.CENTER);
        title.setMinimumSize(TITLE_SIZE);
        title.setPreferredSize(TITLE_SIZE);
        title.setForeground(Application.COLOUR_SCHEME.getText());
        title.setBackground(Application.COLOUR_SCHEME.getDark());
        title.setOpaque(true);

        JPanel panel = new JPanel();
        panel.setBackground(Application.COLOUR_SCHEME.getDark());
        setContent(panel);
        add(content);
    }

    public void addSidebarButton(Component component) {
        toolbar.add(component);
        toolbar.revalidate();
        toolbar.repaint();
    }

    public void removeSidebarButton(Component component) {
        toolbar.remove(component);
        toolbar.revalidate();
        toolbar.repaint();
    }

    public void setContent(Component component) {
        content.removeAll();

        content.add(title, "span, growx, push, north");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(component, BorderLayout.CENTER);
        panel.setBackground(Application.COLOUR_SCHEME.getDark());
        content.add(panel, "grow, push");

        content.revalidate();
        content.repaint();
    }

    public void setTitle(String titleText) {
        title.setText(titleText);
    }
}

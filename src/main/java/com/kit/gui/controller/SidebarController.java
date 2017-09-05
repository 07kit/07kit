package com.kit.gui.controller;

import com.kit.gui.Controller;
import com.kit.gui.component.MateSidebarButton;
import com.kit.gui.view.SidebarView;
import com.kit.gui.ControllerManager;
import com.kit.gui.component.SidebarWidget;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class SidebarController extends Controller<SidebarView> implements ActionListener {
    private final SidebarView sidebarView = new SidebarView(this);
    private final List<SidebarWidget> sidebarWidgets = new ArrayList<>();
    private final Map<SidebarWidget, MateSidebarButton> sidebarButtons = new HashMap<>();

    public SidebarController() {
        ControllerManager.add(SidebarController.class, this);
    }

    @Override
    public SidebarView getComponent() {
        return sidebarView;
    }

    public void registerSidebarWidget(SidebarWidget widget) {
        MateSidebarButton button = new MateSidebarButton(widget, this);
        sidebarView.addSidebarButton(button);

        sidebarButtons.put(widget, button);
        sidebarWidgets.add(widget);

        if (sidebarWidgets.size() == 1) {
            sidebarView.setTitle(button.getWidget().getTitle());
            sidebarView.setContent(button.getWidget().getContent());
            button.setToggled(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof MateSidebarButton) {
            changeTo((MateSidebarButton) e.getSource());
        }
    }

    public void changeTo(MateSidebarButton button) {
        sidebarView.setTitle(button.getWidget().getTitle());

        sidebarView.setContent(button.getWidget().getContent());
        for (MateSidebarButton otherButton : sidebarButtons.values()) {
            otherButton.setToggled(false);
        }
        button.setToggled(true);
        sidebarView.revalidate();
        sidebarView.repaint();
    }

    public void deregisterSidebarWidget(SidebarWidget widget) {
        MateSidebarButton button = sidebarButtons.remove(widget);
        if (button != null) {
            sidebarView.removeSidebarButton(button);
            sidebarWidgets.remove(widget);
            if (button.isToggled()) {
                MateSidebarButton nextButton = sidebarButtons.get(sidebarWidgets.get(0));
                sidebarView.setTitle(nextButton.getWidget().getTitle());
                sidebarView.setContent(nextButton.getWidget().getContent());
                nextButton.setToggled(true);
            }
        }
        sidebarView.invalidate();
        sidebarView.repaint();
    }

    public MateSidebarButton getSidebarButton(SidebarWidget widget) {
        return sidebarButtons.get(widget);
    }
}

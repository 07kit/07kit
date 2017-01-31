package com.kit.gui.component;

import com.kit.Application;

import javax.swing.*;

public class MatePopupMenu extends JPopupMenu {

    public MatePopupMenu() {
        init();
    }

    public MatePopupMenu(String label) {
        super(label);
        init();
    }

    private void init() {
        setBackground(Application.COLOUR_SCHEME.getDark());
        setForeground(Application.COLOUR_SCHEME.getText());
    }
}

package com.kit.gui.component;

import com.kit.Application;
import com.kit.Application;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class MateMenuItem extends JMenuItem {

    public MateMenuItem() {
        init();
    }

    public MateMenuItem(Icon icon) {
        super(icon);
        init();
    }

    public MateMenuItem(String text) {
        super(text);
        init();
    }

    public MateMenuItem(Action a) {
        super(a);
        init();
    }

    public MateMenuItem(String text, Icon icon) {
        super(text, icon);
        init();
    }

    public MateMenuItem(String text, int mnemonic) {
        super(text, mnemonic);
        init();
    }

    private void init() {
        setBackground(Application.COLOUR_SCHEME.getDark());
        setForeground(Application.COLOUR_SCHEME.getText());
    }
}

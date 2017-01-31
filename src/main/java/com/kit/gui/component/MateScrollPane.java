package com.kit.gui.component;

import com.kit.Application;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalScrollBarUI;
import java.awt.*;

/**
 * Date: 26/01/2017
 * Time: 10:52
 *
 * @author Matt Collinge
 */
public class MateScrollPane extends JScrollPane {

    public MateScrollPane() {
        init();
    }

    public MateScrollPane(Component component) {
        super(component);
        init();
    }

    private void init() {
        getVerticalScrollBar().setPreferredSize(new Dimension(7, Integer.MAX_VALUE));
        getVerticalScrollBar().setUI(new MateScrollBarUI());
        getHorizontalScrollBar().setPreferredSize(new Dimension(Integer.MAX_VALUE, 7));
        getHorizontalScrollBar().setUI(new MateScrollBarUI());
        setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    private class MateScrollBarUI extends MetalScrollBarUI {

        private final JButton button;

        public MateScrollBarUI() {
            button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            g.setColor(Application.COLOUR_SCHEME.getLight());
            g.fillRect(thumbBounds.x + 1, thumbBounds.y, thumbBounds.width - 2, thumbBounds.height);
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(Application.COLOUR_SCHEME.getDark());
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return button;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return button;
        }
        
    }

}

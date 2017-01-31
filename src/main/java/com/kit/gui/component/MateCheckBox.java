package com.kit.gui.component;

import com.kit.Application;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import com.kit.Application;
import com.kit.gui.icons.Icons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

/**
 * Date: 07/12/2016
 * Time: 10:22
 *
 * @author Matt Collinge
 */
public class MateCheckBox extends JCheckBox {

    private static final Icon CHECKED;
    private static final Icon UNCHECKED;

    static {
        /*CHECKED = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/checkmark.png")));
        UNCHECKED = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/close.png")));*/
            /*CHECKED = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/checkbox-checked.png")));
            UNCHECKED = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/checkbox-unchecked.png")));*/
        CHECKED = IconFontSwing.buildIcon(FontAwesome.CHECK_CIRCLE_O, 20, Application.COLOUR_SCHEME.getText());
        UNCHECKED = IconFontSwing.buildIcon(FontAwesome.CIRCLE_O, 20, Application.COLOUR_SCHEME.getText());
    }

    public MateCheckBox() {
        super();
        setIcon(UNCHECKED);
        setSelectedIcon(CHECKED);
        setForeground(Application.COLOUR_SCHEME.getText());
    }

    public MateCheckBox(String label) {
        super(label);
        setIcon(UNCHECKED);
        setSelectedIcon(CHECKED);
        setForeground(Application.COLOUR_SCHEME.getText());
    }

}

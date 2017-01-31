package com.kit.gui.icons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

/**
 */
public class Icons {


    //   public static final ImageIcon DISABLED = new ImageIcon("./resources/arrow_down.png");

//    public static final ImageIcon HALTED = new ImageIcon("./resources/arrow_right.png");



    public static final ImageIcon ADD = new ImageIcon("./resources/add.png");

    //  public static final Image CLOSE;

    public static final ImageIcon ENABLED;

    public static final ImageIcon DISABLED;

    public static final ImageIcon PLAY;

    public static final ImageIcon STOP;

    public static final ImageIcon PAUSE;

    public static final ImageIcon HOME;

    public static final ImageIcon PLUS;

    public static final ImageIcon USER;

    static {
        try {
            USER = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/user.png")));
            PLUS = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/plus.png")));
            HOME = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/home2.png")));
            STOP = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/stop2.png")));
            PLAY = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/play3.png")));
            DISABLED = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/close.png")));
            ENABLED = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/checkmark.png")));
            PAUSE = new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/pause2.png")));
        } catch (IOException e) {
            throw new RuntimeException("Resource error!", e);
        }
    }

}

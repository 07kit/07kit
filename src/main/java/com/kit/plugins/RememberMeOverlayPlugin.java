package com.kit.plugins;

import com.kit.core.model.Property;
import com.kit.api.event.EventHandler;
import com.kit.api.event.LoginEvent;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.core.model.Property;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class RememberMeOverlayPlugin extends Plugin {


    private boolean render;
    private BufferedImage buffer;
    public static final int INIT_WIDTH = 765;
    public static final int INIT_X = 390;
    private static final int OVERLAY_WIDTH = 152;
    private static final int OVERLAY_HEIGHT = 18;
    private static final int OVERLAY_Y = 284;
    private static final Color BOX_COLOR = Color.YELLOW;
    private static final Color TEXT_COLOR = Color.WHITE;
    int lastState = 0;

    public RememberMeOverlayPlugin(PluginManager manager) {
        super(manager);
        buffer = new BufferedImage(OVERLAY_WIDTH, OVERLAY_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public String getName() {
        return "Remember Me";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    public int getOverlayX() {
        return ((((Applet) client()).getWidth() - INIT_WIDTH) / 2) + INIT_X;
    }

    @Schedule(200)
    public void makeBuffer() {
        if (lastState != client().getGameState()) {
            lastState = client().getGameState();
            if (Session.get().isRememberUsername()&& Session.get().getRememberedUsername() != null) {
                client().setUsername(Session.get().getRememberedUsername().getValue());
            }
        }
        if (client().getLoginIndex() != 10 || client().getGameState() != 2) {
            render = false;
            logger.debug("not valid game state");
            return;
        }
        buffer = new BufferedImage(OVERLAY_WIDTH, OVERLAY_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        Graphics2D gfx = (Graphics2D) buffer.getGraphics();
        gfx.setColor(BOX_COLOR);
        gfx.drawRect(2, 2, 8, 8);
        if (Session.get().isRememberUsername()) {
            gfx.drawLine(2, 2, 10, 10);
            gfx.drawLine(10, 2, 2, 10);
        }
        gfx.setColor(TEXT_COLOR);
        gfx.setBackground(new Color(0, 0, 0, 0));
        gfx.drawString("Remember username?", 16, (int) (gfx.getFontMetrics().getHeight() / 1.3));
        render = true;
    }

    @EventHandler
    public void onClick(MouseEvent event) {
        if (event.getClickCount() == 0 || Session.get().client().getGameState() != 2) {
            return;
        }
        Rectangle rect = new Rectangle(getOverlayX(), OVERLAY_Y, OVERLAY_WIDTH, OVERLAY_HEIGHT);
        if (rect.contains(event.getX(), event.getY())) {
            Session.get().setRememberUsername(!Session.get().isRememberUsername());
        }
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        if (Session.get().isRememberUsername()) {
            if (Session.get().getRememberedUsername() == null) {
                Property property = new Property(Session.USERNAME_PROPERTY_KEY, client().getUsername());
                Session.get().setRememberedUsername(property);
                property.save();
            } else {
                Session.get().getRememberedUsername().setValue(client().getUsername());
                Session.get().getRememberedUsername().save();
            }
        } else if (Session.get().getRememberedUsername() != null) {
            Session.get().getRememberedUsername().remove();
        }
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        if (render) {
            event.getGraphics().drawImage(buffer, getOverlayX(), OVERLAY_Y, null);
        }
    }

    @Override
    public boolean hasOptions() {
        return false;
    }
}

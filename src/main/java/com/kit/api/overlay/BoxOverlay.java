package com.kit.api.overlay;

import com.kit.api.Mouse;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.core.control.OverlayManager;
import com.kit.api.event.EventHandler;
import com.kit.api.event.OptionChangedEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.core.control.OverlayManager;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 */
public abstract class BoxOverlay {

    public enum DockPosition {
        LEFT, RIGHT
    }

    private Point position = new Point(-1, -1);
    private boolean floating;
    private boolean dragging;
    private boolean locked = true;
    private int z;

    private final Plugin owner;
    private Option xPosition;
    private Option yPosition;

    private OverlayManager manager;

    protected BoxOverlay(Plugin owner) {
        this.owner = owner;
        for (Option option : owner.getOptions()) {
            if (option.label().equals("OverlayX"))
                xPosition = option;
            if (option.label().equals("OverlayY"))
                yPosition = option;
        }
    }

    public Plugin getOwner() {
        return owner;
    }

    public void setOverlayManager(OverlayManager manager) {
        this.manager = manager;
    }

    public abstract void draw(Graphics2D gfx);

    public abstract DockPosition getDockPosition();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract boolean isShowing();

    public Rectangle getArea() {
        return new Rectangle(position.x, position.y, getWidth(), getHeight());
    }

    public Rectangle getLockedArea() {
        return new Rectangle((position.x + getWidth()) - 16, position.y + 1, 15, 15);
    }

    public void setFloating(boolean floating) {
        this.floating = floating;
    }

    public boolean isFloating() {
        return floating;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setPosition(Point position) {
        this.position = position;
        boolean changed = owner.overlayX != position.x || owner.overlayY != position.y;
        owner.overlayX = this.position.x;
        owner.overlayY = this.position.y;
        if (changed) {
            owner.persistOptions();
        }
    }

    public Point getPosition() {
        if (position.x == -1)
            position = new Point(owner.overlayX, owner.overlayY);
        return position;
    }

    private void handleLockArea(MouseEvent mouseEvent) {


    }

    public void onButtonAction(MouseEvent event) {
        if (!isShowing())
            return;

        if (!getArea().contains(event.getX(), event.getY()))
            return;

        if (event.getID() == MouseEvent.MOUSE_PRESSED && getLockedArea().contains(event.getX(), event.getY())) {
            locked = !locked;
            if (!floating && !locked)
                setFloating(true); // Allow user to make overlay floating by "unlocking" once.
            event.consume();
            return;
        }

        if (!floating)
            return;

        if (locked)
            return;

        if (event.getID() == MouseEvent.MOUSE_RELEASED) {
            dragging = false;
            owner.overlayX = position.x;
            owner.handleOptionChanged(new OptionChangedEvent(owner.getClass(), xPosition, position.x));
            owner.overlayY = position.y;
            owner.handleOptionChanged(new OptionChangedEvent(owner.getClass(), yPosition, position.y));
            event.consume();
        } else if (event.getID() == MouseEvent.MOUSE_PRESSED) {
            dragging = true;
            manager.bringToFront(this);
            event.consume();
        }
    }

    public void onMouseDragged(MouseEvent event) {
        if (event.getID() != MouseEvent.MOUSE_DRAGGED)
            return;

        if (!dragging)
            return;

        position = new Point(event.getX(), event.getY());
        event.consume();
    }

}

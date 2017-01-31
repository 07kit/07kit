package com.kit.gui.util;


import com.kit.gui.view.MainView;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class allows you to move a Component by using a mouse. The Component
 * moved can be a high level Window (ie. Window, Frame, Dialog) in which case
 * the Window is moved within the desktop. Or the Component can belong to a
 * Container in which case the Component is moved within the Container.
 * <p/>
 * When moving a Window, the listener can be added to a child Component of
 * the Window. In this case attempting to move the child will result in the
 * Window moving. For example, you might create a custom "Title Bar" for an
 * undecorated Window and moving of the Window is accomplished by moving the
 * title bar only. Multiple components can be registered as "window movers".
 * <p/>
 * Components can be registered when the class is created. Additional
 * components can be added at any time using the registerComponent() method.
 */
public class ComponentMover extends MouseAdapter {
    private Insets dragInsets = new Insets(0, 0, 0, 0);
    private Dimension snapSize = new Dimension(1, 1);
    private Rectangle bounds = new Rectangle(0, 0, 0, 0);
    private boolean changeCursor = true;

    private Component destinationComponent;
    private Component destination;
    private Component source;

    private Point pressed;
    private Point location;

    private Cursor originalCursor;
    private boolean autoscrolls;
    private boolean potentialDrag;

    /**
     * Constructor to specify a parent component that will be moved when drag
     * events are generated on a registered child component.
     *
     * @param destinationComponent the component drage events should be forwareded to
     * @param components           the Components to be registered for forwarding drag
     *                             events to the parent component to be moved
     */
    public ComponentMover(Component destinationComponent, Component... components) {
        this.destinationComponent = destinationComponent;
        registerComponent(components);
        getBoundingSize(destinationComponent);
    }

    /**
     * Add the required listeners to the specified component
     *
     * @param components the component the listeners are added to
     */
    public void registerComponent(Component... components) {
        for (Component component : components) {
            component.addMouseListener(this);
        }
    }

    /**
     * Setup the variables used to control the moving of the component:
     * <p/>
     * source - the source component of the mouse event
     * destination - the component that will ultimately be moved
     * pressed - the Point where the mouse was pressed in the destination
     * component coordinates.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        source = e.getComponent();
        int width = source.getSize().width - dragInsets.left - dragInsets.right;
        int height = source.getSize().height - dragInsets.top - dragInsets.bottom;
        Rectangle r = new Rectangle(dragInsets.left, dragInsets.top, width, height);

        if (r.contains(e.getPoint())) {
            setupForDragging(e);
        }
    }

    private void setupForDragging(MouseEvent e) {
        source.addMouseMotionListener(this);
        potentialDrag = true;

        //  Determine the component that will ultimately be moved

        if (destinationComponent != null) {
            destination = destinationComponent;
        }

//        if (destination instanceof MainView) {
//            final MainView gui = (MainView) destination;
//            if (gui.isMaximized()) {
//                return;
//            }
//        }

        pressed = e.getLocationOnScreen();
        location = destination.getLocation();

        if (changeCursor) {
            originalCursor = source.getCursor();
            source.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

        }

        //  Making sure autoscrolls is false will allow for smoother dragging of
        //  individual components

        if (destination instanceof JComponent) {
            JComponent jc = (JComponent) destination;
            autoscrolls = jc.getAutoscrolls();
            jc.setAutoscrolls(false);
        }
    }

    /**
     * Move the component to its new location. The dragged Point must be in
     * the destination coordinates.
     */
    @Override
    public void mouseDragged(MouseEvent e) {

        Point dragged = e.getLocationOnScreen();
        int dragX = getDragDistance(dragged.x, pressed.x, snapSize.width);
        int dragY = getDragDistance(dragged.y, pressed.y, snapSize.height);

//        if (dragX != 0 || dragY != 0) {
//            if (destination instanceof MainView) {
//                final MainView gui = (MainView) destination;
//                if (gui.isMa) {
//                    return;
//                }
//            }
//        }

        int locationX = location.x + dragX;
        int locationY = location.y + dragY;

        //  Mouse dragged events are not generated for every pixel the mouse
        //  is moved. Adjust the location to make sure we are still on a
        //  snap value.

        getBoundingSize(destination);


        //If the component is a MainView and it is maximized... lets restore

        while (locationX < bounds.x) {
            locationX += snapSize.width;
        }

        while (locationY < bounds.y) {
            locationY += snapSize.height;
        }

        while (locationX > bounds.width) {
            locationX -= snapSize.width;
        }

        while (locationY > bounds.height) {
            locationY -= snapSize.height;
        }


        //  Adjustments are finished, move the component
        destination.setLocation(locationX, locationY);
    }

    /*
     *  Determine how far the mouse has moved from where dragging started
     *  (Assume drag direction is down and right for positive drag distance)
     */
    private int getDragDistance(int larger, int smaller, int snapSize) {
        int halfway = snapSize / 2;
        int drag = larger - smaller;
        drag += (drag < 0) ? -halfway : halfway;
        drag = (drag / snapSize) * snapSize;

        return drag;
    }

    /*
     *  Get the bounds of the parent of the dragged component.
     */
    private void getBoundingSize(Component source) {
        if (source instanceof Window) {
            final Rectangle bounds = new Rectangle(0, 0, 0, 0);
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            for (final GraphicsDevice gd : env.getScreenDevices()) {
                final Rectangle gBounds = gd.getDefaultConfiguration().getBounds();
                if (bounds.x > gBounds.x) {
                    bounds.x = gBounds.x;
                }
                if (bounds.y > gBounds.y) {
                    bounds.y = gBounds.y;
                }
                bounds.width += gBounds.width + 100;
                bounds.height += gBounds.height + 100;
            }
            this.bounds = bounds;
        } else {
            this.bounds = source.getBounds();
        }
    }

    /**
     * Restore the original state of the Component
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (!potentialDrag) {
            return;
        }

        source.removeMouseMotionListener(this);
        potentialDrag = false;

        if (changeCursor) {
            source.setCursor(originalCursor);
        }

        if (destination instanceof JComponent) {
            ((JComponent) destination).setAutoscrolls(autoscrolls);
        }
    }
}

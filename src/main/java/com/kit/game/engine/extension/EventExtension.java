package com.kit.game.engine.extension;

import com.google.common.eventbus.EventBus;
import com.kit.api.event.Events;
import com.kit.api.event.Events;
import com.kit.core.Session;
import com.kit.api.event.Events;
import com.kit.core.Session;

import java.awt.event.*;

/**
 * Event extension for the client's mouse/movement/focus listeners.
 *
 */
public abstract class EventExtension implements MouseListener, MouseMotionListener, FocusListener {

    public static final int HEAD_SIZE = 38;
    private boolean acceptingEvents = true;
    private boolean skipHead = false;
    private int mouseX, mouseY = 0;


    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        submitMouseEvent(e);
        if (!e.isConsumed() && acceptingEvents && (!skipHead || e.getY() > HEAD_SIZE)) {
            _mouseClicked(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent e) {
        submitMouseEvent(e);
        if (!e.isConsumed() && acceptingEvents && (!skipHead || e.getY() > HEAD_SIZE)) {
            _mousePressed(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        submitMouseEvent(e);
        if (!e.isConsumed() && acceptingEvents && (!skipHead || e.getY() > HEAD_SIZE)) {
            _mouseReleased(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        submitMouseEvent(e);
        if (!e.isConsumed() && acceptingEvents && (!skipHead || e.getY() > HEAD_SIZE)) {
            _mouseEntered(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseExited(MouseEvent e) {
        submitMouseEvent(e);
        if (!e.isConsumed() && acceptingEvents && (!skipHead || e.getY() > HEAD_SIZE)) {
            _mouseExited(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        submitMouseEvent(e);
        if (!e.isConsumed() && acceptingEvents && (!skipHead || e.getY() > HEAD_SIZE)) {
            _mouseDragged(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        submitMouseEvent(e);
        this.mouseX = e.getX();
        this.mouseY = e.getY();
        if (!e.isConsumed() && acceptingEvents && (!skipHead || e.getY() > HEAD_SIZE)) {
            _mouseMoved(e);
        }
    }

	/**
     * We call this guy from the client and will return; if it returns false
     * @param e the event
     */
    public boolean canMouseWheelMove(MouseWheelEvent e) {
        submitMouseEvent(e);
        return !e.isConsumed() && acceptingEvents && (!skipHead || e.getY() > HEAD_SIZE);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void focusGained(FocusEvent e) {
        submitEvent(e);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void focusLost(FocusEvent e) {
        submitEvent(e);
    }
    

    private Events getEventBus() {
        Session session = Session.get();
        return session != null ? session.getEventBus() : null;
    }

    private void submitEvent(Object e) {
        Events bus = getEventBus();
        if (bus != null) {
            bus.submit(e);
        }
    }
    
    private void submitMouseEvent(MouseEvent e) {
        Events bus = getEventBus();
        if (bus != null) {
            bus.submitMouseEvent(e);
        }
    }

    /**
     * Check to see if the client is currently
     * accepting mouse events or not.
     *
     * @return events
     */
    public boolean isAcceptingEvents() {
        return acceptingEvents;
    }

    /**
     * Sets the flag for listener to accept
     * or deny mouse events.
     *
     * @param acceptingEvents true if accepting
     */
    public void setAcceptingEvents(boolean acceptingEvents) {
        this.acceptingEvents = acceptingEvents;
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
    public abstract void _mouseClicked(MouseEvent e);

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public abstract void _mousePressed(MouseEvent e);

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public abstract void _mouseReleased(MouseEvent e);

    /**
     * Invoked when the mouse enters a component.
     */
    public abstract void _mouseEntered(MouseEvent e);

    /**
     * Invoked when the mouse exits a component.
     */
    public abstract void _mouseExited(MouseEvent e);

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&Drop implementations,
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
     * Drag&Drop operation.
     */
    public abstract void _mouseDragged(MouseEvent e);

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     */
    public abstract void _mouseMoved(MouseEvent e);


    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setSkipHead(boolean skipHead) {
        this.skipHead = skipHead;
    }

    public interface AcceptanceListener {

        void setAcceptingEvents(boolean eh);

    }
}

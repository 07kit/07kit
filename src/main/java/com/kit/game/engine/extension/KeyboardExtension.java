package com.kit.game.engine.extension;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 */
public abstract class KeyboardExtension implements KeyListener {
    private final List<KeyListener> children = newArrayList();
    private AcceptanceListener acceptanceListener;
    private boolean acceptingEvents = true;

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     */
    public void keyTyped(KeyEvent e) {
        for (KeyListener child : children) {
            child.keyTyped(e);
            if (e.isConsumed()) {
                return;
            }
        }

        if (acceptingEvents) {
            _keyTyped(e);
        }
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     */
    public void keyPressed(KeyEvent e) {
        for (KeyListener child : children) {
            child.keyPressed(e);
            if (e.isConsumed()) {
                return;
            }
        }

        if (acceptingEvents) {
            _keyPressed(e);
        }
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     */
    public void keyReleased(KeyEvent e) {
        for (KeyListener child : children) {
            child.keyReleased(e);
            if (e.isConsumed()) {
                return;
            }
        }

        if (acceptingEvents) {
            _keyReleased(e);
        }
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link java.awt.event.KeyEvent} for a definition of
     * a key typed event.
     */
    public abstract void _keyTyped(KeyEvent e);

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     */
    public abstract void _keyPressed(KeyEvent e);

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     */
    public abstract void _keyReleased(KeyEvent e);

    public boolean isAcceptingEvents() {
        return acceptingEvents;
    }

    public void setAcceptingEvents(boolean acceptingEvents) {
        if (acceptanceListener != null) {
            acceptanceListener.setAcceptingEvents(acceptingEvents);
        }
        this.acceptingEvents = acceptingEvents;
    }

    public void addChild(KeyListener listener) {
        children.add(listener);
    }

    public void removeChild(KeyListener listener) {
        children.remove(listener);
    }

    /**
     * Utility method to make it easier to dispatch KeyEvents to the Canvas
     *
     * @param keyEvent KeyEvent to dispatch.
     */
    public void dispatchEvent(KeyEvent keyEvent) {
        switch (keyEvent.getID()) {
            case KeyEvent.KEY_PRESSED:
                for (KeyListener child : children) {
                    child.keyPressed(keyEvent);
                    if (keyEvent.isConsumed()) {
                        return;
                    }
                }
                _keyPressed(keyEvent);
                break;
            case KeyEvent.KEY_RELEASED:
                for (KeyListener child : children) {
                    child.keyReleased(keyEvent);
                    if (keyEvent.isConsumed()) {
                        return;
                    }
                }
                _keyReleased(keyEvent);
                break;
            case KeyEvent.KEY_TYPED:
                for (KeyListener child : children) {
                    child.keyTyped(keyEvent);
                    if (keyEvent.isConsumed()) {
                        return;
                    }
                }
                _keyTyped(keyEvent);
                break;
        }
    }

    public AcceptanceListener getAcceptanceListener() {
        return acceptanceListener;
    }

    public void setAcceptanceListener(AcceptanceListener acceptanceListener) {
        this.acceptanceListener = acceptanceListener;
    }

    public interface AcceptanceListener {

        void setAcceptingEvents(boolean eh);

    }
}

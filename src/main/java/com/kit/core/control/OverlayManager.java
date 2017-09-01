package com.kit.core.control;

import com.kit.Application;
import com.kit.api.overlay.BoxOverlay;
import com.kit.core.Session;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.overlay.BoxOverlay;
import com.kit.core.Session;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static com.kit.api.overlay.BoxOverlay.DockPosition.LEFT;
import static com.kit.api.overlay.BoxOverlay.DockPosition.RIGHT;

/**
 */
public final class OverlayManager {
    private final List<BoxOverlay> boxOverlays = new ArrayList<>();
    private final Session session;

    public OverlayManager(Session session) {
        this.session = session;
        session.getEventBus().register(this);
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        if (!Session.get().isLoggedIn()) {
            return;
        }
        drawBoxOverlays((Graphics2D) event.getGraphics());
    }

    @EventHandler
    public void onMouseMove(MouseEvent event) {
        for (BoxOverlay overlay : boxOverlays) {
            if (!overlay.isFloating())
                continue;

            if (!overlay.getArea().contains(event.getX(), event.getY()))
                continue;

            if (overlay.getLockedArea().contains(event.getX(), event.getY()))
                continue;

            if (overlay.isLocked())
                continue;

            Session.get().getAppletView().setCursor(new Cursor(Cursor.MOVE_CURSOR));
            return;
        }
        Session.get().getAppletView().setCursor(Cursor.getDefaultCursor());
    }

    @EventHandler
    public void onMouse(MouseEvent event) {
        for (BoxOverlay boxOverlay : boxOverlays) {
            if (event.isConsumed())
                return;
            boxOverlay.onButtonAction(event);
            if (event.isConsumed())
                return;
            boxOverlay.onMouseDragged(event);
        }
    }

    private void drawBoxOverlays(Graphics2D gfx) {
        gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        gfx.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int yOffsetLeft = 40;
        int yOffsetRight = !session.inResizableMode() ? 80 : 200; // Make space for XP orbs in fixed mode and minimap in resizable

        for (int i = boxOverlays.size() - 1; i > -1; i--) {
            BoxOverlay boxOverlay = boxOverlays.get(i);
            if (!boxOverlay.isShowing()) {
                continue;
            }

            if (boxOverlay.getPosition().x == -1 || !boxOverlay.isFloating()) {
                if (boxOverlay.getDockPosition() == BoxOverlay.DockPosition.LEFT) {
                    boxOverlay.setPosition(new Point(10, yOffsetLeft));
                    yOffsetLeft += boxOverlay.getHeight() + 20;
                } else if (boxOverlay.getDockPosition() == BoxOverlay.DockPosition.RIGHT) {
                    boxOverlay.setPosition(new Point(session.client().getViewportWidth() - (boxOverlay.getWidth() + 10), yOffsetRight));
                    yOffsetRight += boxOverlay.getHeight() + 20;
                }
            }

            Graphics2D sectored = (Graphics2D) gfx.create(boxOverlay.getPosition().x, boxOverlay.getPosition().y, boxOverlay.getWidth(), boxOverlay.getHeight());
            boxOverlay.draw(sectored);
            Rectangle lockedButton = new Rectangle(boxOverlay.getWidth() - 16, 0, 15, 15);
            sectored.setColor(Application.COLOUR_SCHEME.getHighlight());
            sectored.drawRect(lockedButton.x, lockedButton.y, lockedButton.width, lockedButton.height);
            sectored.setColor(Application.COLOUR_SCHEME.getText());
            if (boxOverlay.isLocked()) {
                sectored.fillRect(lockedButton.x + 3, lockedButton.y + 8, 10, 5);
                sectored.drawArc(lockedButton.x + 4, lockedButton.y + 4, 7, 8, 0, 180);
            } else {
                sectored.fillRect(lockedButton.x + 3, lockedButton.y + 8, 10, 5);
            }
            sectored.dispose();
        }
    }

    public void addBoxOverlay(BoxOverlay boxOverlay) {
        session.getEventBus().register(boxOverlay);
        boxOverlay.setOverlayManager(this);
        boxOverlays.add(boxOverlay);
    }

    public void removeBoxOverlay(BoxOverlay boxOverlay) {
        session.getEventBus().deregister(boxOverlay);
        boxOverlays.remove(boxOverlay);
        boxOverlay.setOverlayManager(null);
    }

    public void bringToFront(BoxOverlay overlay) {
        int index = boxOverlays.indexOf(overlay);
        if (index == 0)
            return;

        boxOverlays.remove(overlay);
        boxOverlays.add(0, overlay);
    }
}

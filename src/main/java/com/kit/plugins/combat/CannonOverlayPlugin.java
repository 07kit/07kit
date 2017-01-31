package com.kit.plugins.combat;

import com.kit.Application;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.ColorUtils;
import com.kit.api.util.PaintUtils;
import com.kit.core.control.PluginManager;
import com.kit.plugins.skills.mining.MiningOverlayPlugin;

import java.awt.*;

/**
 * Created by Matt on 13/09/2016.
 */
public class CannonOverlayPlugin extends Plugin {
    private final CannonBoxOverlay boxOverlay = new CannonBoxOverlay(this);

    @Option(label = "Floating overlay", value = "false", type = Option.Type.TOGGLE)
    private boolean floating;


    int cannonBalls = 0;

    public CannonOverlayPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Cannon";
    }

    @Override
    public void start() {
        ui.registerBoxOverlay(boxOverlay);
    }

    @Override
    public void stop() {
        ui.deregisterBoxOverlay(boxOverlay);
    }

    @Schedule(600)
    public void update() {
        for (int setting : settings.getWidgetSettings()) {
        }
    }

    private class CannonBoxOverlay extends BoxOverlay {
        private final Color BACKGROUND_COLOR = ColorUtils.setOpacity(new Color(75, 67, 54, 255), 200);
        private final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);

        protected CannonBoxOverlay(Plugin owner) {
            super(owner);
            setFloating(floating);
        }

        @Override
        public void draw(Graphics2D gfx) {
            gfx.setColor(BACKGROUND_COLOR);
            gfx.fillRect(0, 0, getWidth(), getHeight());

            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.setFont(FONT);
            PaintUtils.drawString(gfx, "Cannon", 10, 20);
            gfx.drawLine(10, 30, getWidth() - 10, 30);

            gfx.setFont(FONT.deriveFont(11f));
            int currentCourseWidth = gfx.getFontMetrics().stringWidth("Left:");
            PaintUtils.drawString(gfx, "Left:", 10, 50);
            gfx.setFont(FONT.deriveFont(Font.PLAIN, 11f));
            PaintUtils.drawString(gfx, Integer.toString(cannonBalls), currentCourseWidth + 20, 50);
        }

        @Override
        public DockPosition getDockPosition() {
            return DockPosition.RIGHT;
        }

        @Override
        public int getWidth() {
            return 100;
        }

        @Override
        public int getHeight() {
            return 60;
        }

        @Override
        public boolean isShowing() {
            return true;
        }

        @Override
        public void setFloating(boolean floating) {
            CannonOverlayPlugin.this.floating = floating;
            CannonOverlayPlugin.this.persistOptions();
            super.setFloating(floating);
        }
    }
}

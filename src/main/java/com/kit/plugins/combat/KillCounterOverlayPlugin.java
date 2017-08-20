package com.kit.plugins.combat;

import com.kit.Application;
import com.kit.api.impl.LocalPlayer;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Entity;
import com.kit.core.Session;
import com.kit.game.engine.renderable.entity.IEntity;
import com.kit.game.engine.renderable.entity.INpc;
import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.event.OptionChangedEvent;
import com.kit.api.impl.LocalPlayer;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.ColorUtils;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Entity;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.game.engine.renderable.entity.IEntity;
import com.kit.game.engine.renderable.entity.INpc;
import com.kit.plugins.skills.mining.MiningOverlayPlugin;

import java.awt.*;
import java.awt.event.MouseEvent;

public class KillCounterOverlayPlugin extends Plugin {

    public static final String NUMBER_NPC_KILLS_OPTIONAL_LBL = "numberNpcKills";
    public static final String NUMBER_PLAYER_KILLS_OPTIONAL_LBL = "numberPlayerKills";

    private static final Color BORDER_COLOR = new Color(0, 0, 0, 255);
    private static final Color BACKGROUND_COLOR = ColorUtils.setOpacity(new Color(75, 67, 54, 255), 200);
    private static final Color CLICKED_COLOR = ColorUtils.setOpacity(new Color(56, 48, 34, 255), 240);
    private static final Color HOVER_COLOR = ColorUtils.setOpacity(new Color(150, 142, 128, 255), 220);
    private static final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 9);
    private static final int OVERLAY_WIDTH = 120;
    private static final int OVERLAY_HEIGHT = 80;

    @Option(label = NUMBER_NPC_KILLS_OPTIONAL_LBL, value = "0", type = Option.Type.HIDDEN)
    private int numberNpcKills;
    @Option(label = NUMBER_PLAYER_KILLS_OPTIONAL_LBL, value = "0", type = Option.Type.HIDDEN)
    private int numberPlayerKills;

    private IEntity lastRecordedKill = null;

    private KillCounterOverlay killCounterOverlayOverlay = new KillCounterOverlay(this);

    public KillCounterOverlayPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public boolean isEnabledByDefault() {
        return false;
    }

    @Override
    public String getName() {
        return "Kill Counter";
    }

    @Override
    public String getGroup() {
        return "Combat";
    }

    @Override
    public void start() {
        ui.registerBoxOverlay(killCounterOverlayOverlay);
    }

    @Override
    public void stop() {
        ui.deregisterBoxOverlay(killCounterOverlayOverlay);
    }

    @Schedule(500)
    public void trackInteracting() {
        if (!Session.get().isLoggedIn()) {
            return;
        }
        LocalPlayer player = Session.get().player;
        if (player != null && player.getInteractingEntity() != null) {
            Entity interacting = player.getInteractingEntity();
            if (interacting.getHealthPercent() < 0 &&
                    player.isInCombat()
                    && (lastRecordedKill == null || !lastRecordedKill.equals(interacting.unwrap()))) {
                if (interacting.unwrap() instanceof INpc) {
                    numberNpcKills++;
                    persistOptions();
                } else {
                    numberPlayerKills++;
                    persistOptions();
                }
                lastRecordedKill = interacting.unwrap();
            }
        }
    }

    public class KillCounterOverlay extends BoxOverlay {

        private Rectangle resetBounds;
        private boolean hovered = false;
        private boolean clicking = false;

        protected KillCounterOverlay(Plugin owner) {
            super(owner);
        }

        @Override
        public void draw(Graphics2D gfx) {
            if (!Session.get().isLoggedIn()) {
                return;
            }
            // Draw the box
            gfx.setColor(BACKGROUND_COLOR);
            gfx.fillRect(0, 0, OVERLAY_WIDTH, OVERLAY_HEIGHT);

            // Prepare to draw interacting info
            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.setFont(FONT);

            PaintUtils.drawString(gfx, "Kill Counter", 10, 15);

            gfx.setFont(gfx.getFont().deriveFont(Font.PLAIN));

            // Draw underline
            gfx.drawLine(10, 20, OVERLAY_WIDTH - 10, 20);

            PaintUtils.drawString(gfx, String.format("Npc Kill(s) : %d", numberNpcKills), 10, 35);
            PaintUtils.drawString(gfx, String.format("Player Kill(s) : %d", numberPlayerKills), 10, 50);

            String reset = "Reset";

            int resetWidth = gfx.getFontMetrics().stringWidth(reset);

            if (resetBounds == null) {
                resetBounds =
                        new Rectangle((OVERLAY_WIDTH / 2) - (resetWidth / 2),
                                60, resetWidth + 10, gfx.getFontMetrics().getHeight() + 4);
            }

            if (clicking) {
                gfx.setColor(CLICKED_COLOR);
            } else if (hovered) {
                gfx.setColor(HOVER_COLOR);
            } else {
                gfx.setColor(BACKGROUND_COLOR);
            }

            gfx.fill(resetBounds);
            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.draw(resetBounds);
            PaintUtils.drawString(gfx, reset, ((OVERLAY_WIDTH / 2) - (resetWidth / 2)) + 5, resetBounds.y + gfx.getFontMetrics().getHeight());

            // Draw the box outline
            gfx.setColor(BORDER_COLOR);
            gfx.drawRect(0, 0, OVERLAY_WIDTH - 1, OVERLAY_HEIGHT - 1);
        }

        @EventHandler
        public void handleReset(MouseEvent event) {
            if (resetBounds != null && resetBounds.contains(event.getX() - getPosition().getX(), event.getY() - getPosition().getY())) {
                if (event.getID() == MouseEvent.MOUSE_PRESSED) {
                    clicking = true;
                    event.consume();
                } else if (event.getID() == MouseEvent.MOUSE_CLICKED) {
                    resetKills();
                    event.consume();
                } else {
                    hovered = true;
                    clicking = false;
                    event.consume();
                }
            } else {
                clicking = false;
                hovered = false;
            }
        }

        private void resetKills() {
            numberNpcKills = 0;
            numberPlayerKills = 0;
            persistOptions();
        }

        @Override
        public DockPosition getDockPosition() {
            return DockPosition.RIGHT;
        }

        @Override
        public int getWidth() {
            return OVERLAY_WIDTH;
        }

        @Override
        public int getHeight() {
            return OVERLAY_HEIGHT;
        }

        @Override
        public boolean isShowing() {
            return getOwner().isEnabled() && isLoggedIn() && !bank.isOpen();
        }
    }
}

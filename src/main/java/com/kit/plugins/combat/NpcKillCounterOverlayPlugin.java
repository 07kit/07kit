package com.kit.plugins.combat;

import com.google.common.reflect.TypeToken;
import com.google.gson.annotations.SerializedName;
import com.kit.Application;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Entity;
import com.kit.core.model.*;
import com.kit.game.engine.renderable.entity.INpc;
import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.LoginEvent;
import com.kit.api.impl.LocalPlayer;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.ColorUtils;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Entity;
import com.kit.api.wrappers.Npc;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.core.model.Container;
import com.kit.game.engine.renderable.entity.IEntity;
import com.kit.game.engine.renderable.entity.INpc;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NpcKillCounterOverlayPlugin extends Plugin {
    private static final Color BORDER_COLOR = new Color(0, 0, 0, 255);
    private static final Color BACKGROUND_COLOR = ColorUtils.setOpacity(new Color(75, 67, 54, 255), 200);
    private static final Color CLICKED_COLOR = ColorUtils.setOpacity(new Color(56, 48, 34, 255), 240);
    private static final Color HOVER_COLOR = ColorUtils.setOpacity(new Color(150, 142, 128, 255), 220);
    private static final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 9);
    private static final int OVERLAY_WIDTH = 120;
    private static final int OVERLAY_HEIGHT = 60;

    private final com.kit.core.model.Container<NpcLog> npcLogContainer = new com.kit.core.model.Container<NpcLog>("npc_kills") {
        @Override
        public Type getElementsType() {
            return new TypeToken<List<NpcLog>>() {
            }.getType();
        }
    };
    private NpcLog activeLog;

    private IEntity lastRecordedKill = null;

    private NpcKillCounterBoxOverlay npcKillCounterOverlayBoxOverlay = new NpcKillCounterBoxOverlay(this);

    public NpcKillCounterOverlayPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public boolean isEnabledByDefault() {
        return false;
    }

    @Override
    public String getName() {
        return "NPC Kill Counter";
    }

    @Override
    public String getGroup() {
        return "Combat";
    }

    @Override
    public void start() {
        ui.registerBoxOverlay(npcKillCounterOverlayBoxOverlay);
    }

    @Override
    public void stop() {
        ui.deregisterBoxOverlay(npcKillCounterOverlayBoxOverlay);
    }

    @Schedule(500)
    public void trackInteracting() {
        if (!Session.get().isLoggedIn() || activeLog == null) {
            return;
        }

        LocalPlayer player = Session.get().player;
        if (player != null && player.getInteractingEntity() != null) {
            Entity interacting = player.getInteractingEntity();
            if (interacting.getHealthPercent() < 0 && interacting.isInCombat()
                    && (lastRecordedKill == null || !lastRecordedKill.equals(interacting.unwrap()))) {
                if (interacting.unwrap() instanceof INpc) {
                    Npc npc = (Npc) interacting;
                    long count = 0;
                    if (activeLog.counters.containsKey(npc.getName())) {
                        count = activeLog.counters.get(npc.getName());
                    }
                    activeLog.counters.put(npc.getName(), count + 1);
                    npcLogContainer.save();
                    lastRecordedKill = interacting.unwrap();
                }
            }
        }
    }

    @EventHandler
    public void onLoginEvent(LoginEvent event) {
        logger.info("Logged in - finding/creating ledger.");
        NpcLog selectedLog = getLog();
        if (selectedLog == null) {
            logger.info("Creating new log for " + client().getUsername());
            selectedLog = new NpcLog(client().getUsername(), new HashMap<>());
            npcLogContainer.add(selectedLog);
            npcLogContainer.save();
        }
        activeLog = selectedLog;
    }

    private NpcLog getLog() {
        for (NpcLog log : npcLogContainer.getAll()) {
            if (log.getLoginName().equals(client().getUsername())) {
                logger.info("Found existing log for " + client().getUsername());
                return log;
            }
        }
        return null;
    }

    public class NpcKillCounterBoxOverlay extends BoxOverlay {

        private Rectangle resetBounds;
        private boolean hovered = false;
        private boolean clicking = false;

        protected NpcKillCounterBoxOverlay(Plugin owner) {
            super(owner);
        }

        @Override
        public void draw(Graphics2D gfx) {
            if (activeLog == null) {
                return;
            }

            // Draw the box
            gfx.setColor(BACKGROUND_COLOR);
            gfx.fillRect(0, 0, OVERLAY_WIDTH, getHeight());

            // Prepare to draw interacting info
            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.setFont(FONT);

            PaintUtils.drawString(gfx, "NPC kills", 10, 15);

            gfx.setFont(gfx.getFont().deriveFont(Font.PLAIN));

            // Draw underline
            gfx.drawLine(10, 20, OVERLAY_WIDTH - 10, 20);

            int offset = 0;
            for (Map.Entry<String, Long> entry : activeLog.counters.entrySet()) {
                PaintUtils.drawString(gfx, String.format("%s [%d kills]", entry.getKey(), entry.getValue()), 10, 35 + (offset * 15));
                offset++;
            }

            String reset = "Reset";

            int resetWidth = gfx.getFontMetrics().stringWidth(reset);

            resetBounds =
                    new Rectangle((OVERLAY_WIDTH / 2) - (resetWidth / 2),
                            getHeight() - (gfx.getFontMetrics().getHeight() * 2), resetWidth + 10, gfx.getFontMetrics().getHeight() + 4);

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
            gfx.drawRect(0, 0, OVERLAY_WIDTH - 1, getHeight());
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
            if (activeLog != null) {
                activeLog.counters.clear();
            }
            npcLogContainer.save();
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
            return OVERLAY_HEIGHT + (activeLog != null ? activeLog.counters.size() * 15 : 0);
        }

        @Override
        public boolean isShowing() {
            return getOwner().isEnabled() && isLoggedIn() && !bank.isOpen();
        }
    }

    public static class NpcLog {
        @SerializedName("loginName")
        private String loginName;
        @SerializedName("counters")
        private Map<String, Long> counters;

        public NpcLog(String loginName, Map<String, Long> counters) {
            this.loginName = loginName;
            this.counters = counters;
        }

        public NpcLog() {
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public Map<String, Long> getCounters() {
            return counters;
        }

        public void setCounters(Map<String, Long> counters) {
            this.counters = counters;
        }

        @Override
        public String toString() {
            return "NpcLog{" +
                    "loginName='" + loginName + '\'' +
                    ", counters=" + counters +
                    '}';
        }
    }
}

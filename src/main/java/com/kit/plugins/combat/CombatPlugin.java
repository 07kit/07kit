package com.kit.plugins.combat;

import com.kit.Application;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.*;
import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.ColorUtils;
import com.kit.api.util.NotificationsUtil;
import com.kit.api.util.PaintUtils;
import com.kit.core.control.PluginManager;
import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.ColorUtils;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.control.PluginManager;
import com.kit.plugins.skills.mining.MiningOverlayPlugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class CombatPlugin extends Plugin {
    private static final Color BORDER_COLOR = new Color(0, 0, 0, 255);
    private static final Color BACKGROUND_COLOR = ColorUtils.setOpacity(new Color(75, 67, 54, 255), 240);
    private static final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 9);
    private static final int OVERLAY_WIDTH = 120;
    private static final int OVERLAY_HEIGHT = 70;

    private final CombatBoxOverlay box = new CombatBoxOverlay(this);

    @Option(label = "Highlight players that can attack me", value = "true", type = Option.Type.TOGGLE)
    private boolean showAttackablePlayers;
    @Option(label = "Only show colour for players that can attack me", value = "true", type = Option.Type.TOGGLE)
    private boolean showDotOnly;
    @Option(label = "Notify when health falls below (%)", value = "20", type = Option.Type.NUMBER)
    private int healthNotifyThreshold;
    private long lastHealthNotification;
    @Option(label = "Notify when prayer falls below (%)", value = "20", type = Option.Type.NUMBER)
    private int prayerNotifyThreshold;
    private long lastPrayerNotification;
    @Option(label = "Notify when engaging in combat while 07Kit isn't focused.", value = "true", type = Option.Type.TOGGLE)
    private boolean notifyWhenUnfocused;
    @Option(label = "Floating overlay", value = "false", type = Option.Type.TOGGLE)
    private boolean floating;

    private boolean isInCombat;
    private long lastUnfocusedNotification;

    private List<Player> dangerousPlayers;

    public CombatPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Combat";
    }

    @Override
    public String getGroup() {
        return "Combat";
    }

    @Override
    public void start() {
        ui.registerBoxOverlay(box);
    }

    @Override
    public void stop() {
        ui.deregisterBoxOverlay(box);
    }

    @Schedule(600)
    public void checkCombatState() {
        if (!isLoggedIn()) {
            logger.debug("Not logged in.");
            return;
        }

        if (isInCombat != player.isInCombat()) {
            isInCombat = player.isInCombat();
            if (isInCombat && notifyWhenUnfocused && !ui.isFocused() && (System.currentTimeMillis() - lastUnfocusedNotification) >= 5000) { // Rather urgent - every 5 secs.
                NotificationsUtil.showNotification("Danger!", "You're under attack!");
                lastUnfocusedNotification = System.currentTimeMillis();
            }
        }

        if (!isInCombat) {

            double healthPercentage = (double) skills.getLevel(Skill.HITPOINTS) / (double) skills.getBaseLevel(Skill.HITPOINTS) * 100D;
            if (healthPercentage <= healthNotifyThreshold && (System.currentTimeMillis() - lastHealthNotification) >= 10000) { // At max notify once every 10 secs.
                NotificationsUtil.showNotification("Danger!", "Your health has dropped below " + healthNotifyThreshold + "%");
                lastHealthNotification = System.currentTimeMillis();
            }

            double prayerPercentage = (double) skills.getLevel(Skill.PRAYER) / (double) skills.getBaseLevel(Skill.PRAYER) * 100D;
            if (prayerPercentage <= prayerNotifyThreshold && (System.currentTimeMillis() - lastPrayerNotification) >= 10000) { // At max notify once every 10 secs.
                NotificationsUtil.showNotification("Danger!", "Your prayer has dropped below " + prayerNotifyThreshold + "%");
                lastPrayerNotification = System.currentTimeMillis();
            }

            Widget wildernessWidget = widgets.find(90, 29);
            if (wildernessWidget != null && wildernessWidget.getText().contains("Level: ")) {
                Integer level = Integer.valueOf(wildernessWidget.getText().split(" ")[1]);
                dangerousPlayers = players.find()
                        .levelBetween(player.getCombatLevel() - level, player.getCombatLevel() + level)
                        .asList();
            } else {
                dangerousPlayers = null;
            }
        }
    }

    @EventHandler
    public void onPaintEvent(PaintEvent event) {
        if (!isLoggedIn() || dangerousPlayers == null) {
            return;
        }

        if (player.getInteractingEntity() == null) {
            Graphics2D g2d = (Graphics2D) event.getGraphics().create();
            for (Player dangerousPlayer : dangerousPlayers) {
                Point baseLocation = dangerousPlayer.getBasePoint();
                Point mmLocation = minimap.convert(dangerousPlayer.getTile());
                if (baseLocation.x != -1 && baseLocation.y != -1) {
                    g2d.setFont(g2d.getFont().deriveFont(10.0f));

                    g2d.setColor(getDangerColour(dangerousPlayer, true));
                    dangerousPlayer.getTile().drawBox(g2d, dangerousPlayer.unwrap().getHeight());

                    g2d.setColor(getDangerColour(dangerousPlayer, false));
                    String marker = dangerousPlayer.getName() + " (Lvl-" + dangerousPlayer.getCombatLevel() + ")";
                    int width = g2d.getFontMetrics().stringWidth(marker);
                    if (!showDotOnly) {
                        PaintUtils.drawString(g2d, marker, baseLocation.x - (width / 2), baseLocation.y);
                    }
                } else if (mmLocation.x != -1 && mmLocation.y != -1) {
                    g2d.setFont(g2d.getFont().deriveFont(8.0f));
                    g2d.setColor(getDangerColour(dangerousPlayer, false));
                    String marker = dangerousPlayer.getName() + " (Lvl-" + dangerousPlayer.getCombatLevel() + ")";
                    PaintUtils.drawString(g2d, marker, mmLocation.x, mmLocation.y);
                }
            }
            g2d.dispose();
        }
    }

    private Color getDangerColour(Player enemy, boolean transparency) {
        int combatLevelLocal = player.getCombatLevel();
        int combatLevelRemote = enemy.getCombatLevel();
        if (combatLevelLocal > combatLevelRemote) {
            return new Color(0, 220, 0, transparency ? 120 : 255);
        } else if (combatLevelRemote > combatLevelLocal) {
            return new Color(255, 0, 0, transparency ? 120 : 255);
        } else {
            return new Color(255, 255, 0, transparency ? 120 : 255);
        }
    }

    private class CombatBoxOverlay extends BoxOverlay {
        private Image healthIcon;
        private Image prayerIcon;

        private CombatBoxOverlay(Plugin plugin) {
            super(plugin);
            setFloating(floating);
            try {
                healthIcon = ImageIO.read(getClass().getResourceAsStream("/hitpoints.gif")).getScaledInstance(15, 15, Image.SCALE_SMOOTH);
                prayerIcon = ImageIO.read(getClass().getResourceAsStream("/prayer.gif")).getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void draw(Graphics2D gfx) {
            // Draw interacting info
            Entity interactingEntity = player.getInteractingEntity();
            if (interactingEntity.getHealthRatio(interactingEntity.getCombatInfoWrapper()) == -1) {
                return;
            }

            // Draw the box
            gfx.setColor(BACKGROUND_COLOR);
            gfx.fillRect(0, 0, OVERLAY_WIDTH, OVERLAY_HEIGHT);

            // Prepare to draw interacting info
            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.setFont(FONT);

            int level = 0;
            if (interactingEntity instanceof Npc) {
                level = ((Npc) interactingEntity).getComposite().getCombatLevel();
            } else if (interactingEntity instanceof Player) {
                level = ((Player) interactingEntity).getCombatLevel();
            }

            PaintUtils.drawString(gfx, String.format("%s (%d)", interactingEntity.getName(), level), 10, 15);

            // Draw underline
            gfx.drawLine(10, 20, OVERLAY_WIDTH - 10, 20);

            // Draw health (progress) bar
            gfx.setColor(Color.RED.darker());

            int progressBarWidth = OVERLAY_WIDTH - 20;
            int progressBarHeight = 15;

            // Y: 35px, LPAD: 10px, RPAD: 10px, HEIGHT: 30px
            gfx.fillRect(10, 25, OVERLAY_WIDTH - 20, progressBarHeight);

            // Draw progress
            float ratio = ((float) interactingEntity.getHealthPercent()) / 100.0F;
            int progressWidth = (int) (ratio * progressBarWidth);
            gfx.setColor(Color.GREEN.darker());
            gfx.fillRect(10, 25, progressWidth, progressBarHeight);

            int maxHealth = -1;
            if (interactingEntity instanceof Npc) {
                Npc npc = (Npc) interactingEntity;
                NpcInfo npcInfo = npc.getNpcInfo();
                if (npcInfo != null) {
                    maxHealth = npcInfo.getHp();
                }
            } else if (interactingEntity instanceof Player) {
                //TODO implement hiscore lookup?
            }

            if (maxHealth > -1) {
                String hpText = Math.round((interactingEntity.getHealthPercent() / 100.0f) * maxHealth) + "/" + maxHealth;
                gfx.setFont(FONT.deriveFont(9f));
                gfx.setColor(Application.COLOUR_SCHEME.getText());
                PaintUtils.drawString(gfx, hpText, progressBarWidth / 2, 25 +
                        Math.round(progressBarHeight / 2) + Math.round(gfx.getFontMetrics().getHeight() / 2));
            } else {
                // Draw progress (textual)
                String percentage = String.format("%d %%", interactingEntity.getHealthPercent());
                gfx.setFont(FONT.deriveFont(9f));
                gfx.setColor(Application.COLOUR_SCHEME.getText());
                PaintUtils.drawString(gfx, percentage, (progressBarWidth / 2), 25 +
                        Math.round(progressBarHeight / 2) + Math.round(gfx.getFontMetrics().getHeight() / 2));
            }

            int warningXOffset = 0;

            double healthPercentage = (double) skills.getLevel(Skill.HITPOINTS) / (double) skills.getBaseLevel(Skill.HITPOINTS) * 100D;
            if (healthPercentage <= healthNotifyThreshold) {
                gfx.setColor(Color.RED);
                gfx.drawRect(10 + warningXOffset, 50, 15, 15);

                gfx.setColor(new Color(155, 0, 0, (int) (155D * (healthPercentage / 100))));
                gfx.fillRect(10 + warningXOffset, 50, 15, 15);
                gfx.drawImage(healthIcon, 10, 50, 15, 15, null);

                warningXOffset += 35;
            }

            double prayerPercentage = (double) skills.getLevel(Skill.PRAYER) / (double) skills.getBaseLevel(Skill.PRAYER) * 100D;
            if (prayerPercentage <= prayerNotifyThreshold) {
                gfx.setColor(Color.RED);
                gfx.drawRect(10 + warningXOffset, 50, 15, 15);

                gfx.setColor(new Color(155, 0, 0, (int) (155D * (healthPercentage / 100))));
                gfx.fillRect(10 + warningXOffset, 50, 15, 15);
                gfx.drawImage(prayerIcon, 10, 50, 15, 15, null);

                warningXOffset += 35;
            }

            // Draw border
            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.drawRect(10, 25, progressBarWidth, progressBarHeight);

            // Draw the box outline
            gfx.setColor(BORDER_COLOR);
            gfx.drawRect(0, 0, OVERLAY_WIDTH - 1, OVERLAY_HEIGHT - 1);

        }

        @Override
        public DockPosition getDockPosition() {
            return DockPosition.LEFT;
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
            if (client() == null || !isLoggedIn() || !player.isInteracting()) {
                return false;
            }

            Entity interactingEntity = player.getInteractingEntity();
            return player.getInteractingEntity().getHealthRatio(interactingEntity.getCombatInfoWrapper()) != -1;
        }

        @Override
        public void setFloating(boolean floating) {
            CombatPlugin.this.floating = floating;
            CombatPlugin.this.persistOptions();
            super.setFloating(floating);
        }
    }
}

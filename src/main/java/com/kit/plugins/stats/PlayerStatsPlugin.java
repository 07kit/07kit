package com.kit.plugins.stats;

import com.kit.Application;
import com.kit.Application;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.ColorUtils;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Player;
import com.kit.api.wrappers.Skill;
import com.kit.api.wrappers.hiscores.HiscoreLookup;
import com.kit.api.wrappers.hiscores.HiscoreSkill;
import com.kit.api.wrappers.hiscores.HiscoreType;
import com.kit.core.control.PluginManager;

import java.awt.*;

/**
 */
public final class PlayerStatsPlugin extends Plugin {
    private final PlayerStatsBoxOverlay box = new PlayerStatsBoxOverlay(this);

    @Option(label = "Floating overlay", value = "false", type = Option.Type.TOGGLE)
    private boolean floating;

    private HiscoreLookup lookupResults;
    private Player interacting;

    public PlayerStatsPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Player Stats";
    }

    @Override
    public void start() {
        ui.registerBoxOverlay(box);
    }

    @Override
    public void stop() {
        ui.deregisterBoxOverlay(box);
    }

    @Schedule(1000)
    public void tick() {
        if (!isLoggedIn()) {
            return;
        } else if (player.getInteractingEntity() == null) {
            interacting = null;
            lookupResults = null;
        }

        if (player.getInteractingEntity() != interacting) {
            Object tmpInteracting = player.getInteractingEntity();
            if (interacting != null && tmpInteracting instanceof Player) {
                interacting = (Player) player.getInteractingEntity();
                new Thread(() -> {
                    lookupResults = hiscores.lookup(interacting.getName(), HiscoreType.STANDARD);
                }, "player-stats-hiscore-lookup").start();
            }
        }
    }

    private class PlayerStatsBoxOverlay extends BoxOverlay {
        private final Color BACKGROUND_COLOR = ColorUtils.setOpacity(new Color(75, 67, 54, 255), 200);
        private final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 9);

        protected PlayerStatsBoxOverlay(Plugin owner) {
            super(owner);
            setFloating(floating);
        }

        @Override
        public void draw(Graphics2D gfx) {
            if (lookupResults == null) {
                return;
            }

            gfx.setColor(BACKGROUND_COLOR);
            gfx.fillRect(0, 0, getWidth(), getHeight());

            gfx.setColor(Application.COLOUR_SCHEME.getText());
            gfx.setFont(FONT);
            PaintUtils.drawString(gfx, interacting.getName(), 10, 15);

            gfx.drawLine(10, 20, getWidth() - 10, 20);

            gfx.setFont(FONT.deriveFont(Font.PLAIN));

            Skill[] relevantSkills = {Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.HITPOINTS, Skill.RANGED, Skill.MAGIC, Skill.PRAYER};

            int offset = 0;
            for (Skill skill : relevantSkills) {
                HiscoreSkill hiscoreSkill = lookupResults.getSkill(skill);
                if (hiscoreSkill != null && hiscoreSkill.getLevel() > 0) {
                    PaintUtils.drawString(gfx, String.format("%s %d", skill.getName(), hiscoreSkill.getLevel()), 10, 35 + (offset * 15));
                    offset++;
                }
            }

            // Draw the box outline
            gfx.setColor(new Color(0, 0, 0));
            gfx.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }

        @Override
        public DockPosition getDockPosition() {
            return DockPosition.LEFT;
        }

        @Override
        public int getWidth() {
            return 120;
        }

        @Override
        public int getHeight() {
            return 135;
        }

        @Override
        public boolean isShowing() {
            return isLoggedIn() && interacting != null && lookupResults != null;
        }

        @Override
        public void setFloating(boolean floating) {
            PlayerStatsPlugin.this.floating = floating;
            PlayerStatsPlugin.this.persistOptions();
            super.setFloating(floating);
        }
    }
}

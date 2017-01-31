package com.kit.plugins.clan;

import com.kit.api.util.PaintUtils;
import com.kit.core.Session;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Player;
import com.kit.core.Session;
import com.kit.socket.event.ClanSkillEvent;

import java.awt.*;
import java.util.Map;

public class ClanMemberOverlay {

    public static final Color CLAN_MEMBER_COLOR = new Color(18, 133, 244);

    private ClanPlugin clanPlugin;

    public ClanMemberOverlay(ClanPlugin clanPlugin) {
        this.clanPlugin = clanPlugin;
    }

    @EventHandler
    public void onPaint(PaintEvent evt) {
        if (!clanPlugin.isDisplayMembersHealth() || !Session.get().isLoggedIn()) {
            return;
        }
        Graphics2D g2d = (Graphics2D) evt.getGraphics();
        g2d.setColor(CLAN_MEMBER_COLOR);
        Font old = g2d.getFont();
        g2d.setFont(g2d.getFont().deriveFont(10f).deriveFont(Font.BOLD));

        for (Map.Entry<String, Player> nearbyMember : clanPlugin.getNearbyClanMembers().entrySet()) {
            ClanMemberInfo info = clanPlugin.getClanMembers().get(nearbyMember.getKey());
            if (info == null || info.getRank() == null || info.getRank().getIngameName() == null ||
                    info.getSkills() == null || nearbyMember.getValue().unwrap() == null) {
                continue;
            }
            Point minimap = Session.get().minimap.convert(nearbyMember.getValue().getTile());
            if (minimap != null) {
                PaintUtils.drawString(g2d, info.getRank().getIngameName(),
                        minimap.x + 1, minimap.y);
            }
            Point p = nearbyMember.getValue().getBasePoint();
            if (p != null && Session.get().viewport.isInViewport(p)) {
                PaintUtils.drawString(g2d, info.getRank().getIngameName(),
                        p.x, p.y);

                ClanSkillEvent health = info.getSkills().get(ClanSkillEvent.Skill.HITPOINTS);
                if (health != null) {
                    Color healthColor = clanPlugin.getHealthColor(health);
                    if (healthColor!= null) {
                        g2d.setColor(healthColor);
                    }

                    String label = "HP: ";
                    PaintUtils.drawString(g2d, label,
                            p.x, p.y + g2d.getFontMetrics().getHeight());

                    PaintUtils.drawString(g2d,  + health.getCurrentLevel() + "/"
                                    + health.getBaseLevel(),
                            p.x + g2d.getFontMetrics().stringWidth(label), p.y + g2d.getFontMetrics().getHeight());
                    g2d.setColor(CLAN_MEMBER_COLOR);
                }
            }
        }

        g2d.setFont(old);
    }
}

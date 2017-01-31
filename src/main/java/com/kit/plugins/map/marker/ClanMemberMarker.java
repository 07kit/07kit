package com.kit.plugins.map.marker;

import com.kit.api.util.PaintUtils;
import com.kit.plugins.clan.ClanMemberInfo;
import com.kit.plugins.clan.ClanMemberOverlay;
import com.kit.plugins.clan.ClanPlugin;
import com.kit.plugins.map.WorldMapPlugin;
import com.kit.socket.event.ClanSkillEvent;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ClanMemberMarker extends Marker {

    private ClanMemberInfo info;
    private ClanPlugin clanPlugin;

    public ClanMemberMarker(WorldMapPlugin plugin, ClanMemberInfo info, ClanPlugin clanPlugin) {
        super(plugin, null);
        this.info = info;
        this.clanPlugin = clanPlugin;
    }

    @Override
    public Point getPoint() {
        return WorldMapPlugin.pointForTile(info.getLastPosition());
    }

    @Override
    public void render(Graphics2D g, BufferedImage markerIcon, Point p) {
        g.setColor(ClanMemberOverlay.CLAN_MEMBER_COLOR);
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        PaintUtils.drawString(g, info.getRank().getIngameName(), p.x, p.y);

        ClanSkillEvent health = info.getSkills().get(ClanSkillEvent.Skill.HITPOINTS);
        if (health != null) {
            Color healthColor = clanPlugin.getHealthColor(health);
            if (healthColor!= null) {
                g.setColor(healthColor);
            }

            PaintUtils.drawString(g, "(HP: " + health.getCurrentLevel()
                    + "/" + health.getBaseLevel() + ") ", p.x + g.getFontMetrics().stringWidth(info.getRank().getIngameName()), p.y);
        }
    }
}

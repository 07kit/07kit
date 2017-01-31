package com.kit.plugins;

import com.kit.Application;
import com.kit.api.event.ActionEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.NpcComposite;
import com.kit.core.Session;
import com.kit.game.engine.cache.composite.INpcComposite;
import com.kit.Application;
import com.kit.api.event.ActionEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.event.NpcMenuCreatedEvent;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NpcCompositesUtil;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Npc;
import com.kit.api.wrappers.NpcComposite;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.game.engine.cache.composite.INpcComposite;
import com.kit.Application;
import com.kit.api.plugin.Option;
import com.kit.api.wrappers.NpcComposite;
import com.kit.core.control.PluginManager;

import java.awt.*;
import java.util.List;

/**
 */
public class NpcMarkerOverlayPlugin extends Plugin {
    public static final int MARK_NPC_OPCODE = 16002;
    public static final int UNMARK_NPC_OPCODE = 16003;

    @Option(label = "Enter names of NPCs to mark (separated by ,)", value = "", type = Option.Type.TEXT)
    private String npcNames;
    @Option(label = "Only show idle NPCs", value = "false", type = Option.Type.TOGGLE)
    private boolean idleOnly;

    private List<Npc> matchedNpcs;

    public NpcMarkerOverlayPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Npc Marker";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Schedule(600)
    public void pollForNpcs() {
        if (!isLoggedIn()) {
            return;
        }

        String[] names = npcNames.split(",");
        matchedNpcs = npcs.find(names).ignore("", null).distance(20).asList();
    }

    @EventHandler
    public void onPaintEvent(PaintEvent event) {
        if (!isLoggedIn() || matchedNpcs == null || matchedNpcs.size() == 0 || bank.isOpen()) {
            return;
        }

        Graphics2D g2d = (Graphics2D) event.getGraphics().create();
        for (Npc npc : matchedNpcs) {
            if (!idleOnly || !npc.isInCombat()) {
                Point baseLocation = npc.getBasePoint();
                Point mmLocation = minimap.convert(npc.getTile());
                if (baseLocation.x != -1 && baseLocation.y != -1) {
                    g2d.setFont(g2d.getFont().deriveFont(14.0f));
                    g2d.setColor(Application.COLOUR_SCHEME.getText());
                    PaintUtils.drawString(g2d, npc.getName() + " (Lvl-" + npc.getComposite().getCombatLevel() + ")", baseLocation.x, baseLocation.y);
                } else if (mmLocation.x != -1 && mmLocation.y != -1) {
                    g2d.setFont(g2d.getFont().deriveFont(10.0f));
                    g2d.setColor(Color.CYAN);
                    PaintUtils.drawString(g2d, npc.getName() + " (Lvl" + npc.getComposite().getCombatLevel() + ")", mmLocation.x, mmLocation.y);
                }
            }
        }
        g2d.dispose();
    }

    @EventHandler
    public void onMenuClick(ActionEvent evt) {
        if (evt.getOpcode() == ActionEvent.Opcode.MARK_NPC_OPCODE || evt.getOpcode() == ActionEvent.Opcode.UNMARK_NPC_OPCODE) {
            String targetName = evt.getEntityName().substring(0, evt.getEntityName().indexOf('<'));
            String[] npcNamesTokenized = npcNames.split(",");

            StringBuilder builder = new StringBuilder();
            for (String npcName : npcNamesTokenized) {
                if (npcName.toLowerCase().equals(targetName.toLowerCase())) {
                    continue;
                }
                builder.append(npcName).append(',');
            }

            if (evt.getOpcode() == ActionEvent.Opcode.MARK_NPC_OPCODE) {
                builder.append(targetName).append(',');
            }
            npcNames = builder.toString();
        }
    }

    @EventHandler
    public void onMenuCreation(NpcMenuCreatedEvent evt) {
        if (npcNames.toLowerCase().contains(evt.getComposite().getName().toLowerCase())) {
            client().addMenuEntry("Un-mark", evt.getComposite().getName() + getCombatLevel(evt.getComposite()),
                    UNMARK_NPC_OPCODE, evt.getVar0(), evt.getVar1(), evt.getVar2());
        } else {
            client().addMenuEntry("Mark", evt.getComposite().getName() + getCombatLevel(evt.getComposite()),
                    MARK_NPC_OPCODE, evt.getVar0(), evt.getVar1(), evt.getVar2());
        }
    }


    private String getCombatLevel(INpcComposite composite) {
        NpcComposite composite1 =  Session.get().composites.getNpcComposite(composite.getId());
        int combatLevelLocal = player.getCombatLevel();
        int combatLevelRemote = composite1 != null ? composite1.getCombatLevel() : 0;
        StringBuilder builder = new StringBuilder();
        if (combatLevelLocal > combatLevelRemote) {
            builder.append("<col=00ff00>");
        } else if (combatLevelRemote > combatLevelLocal) {
            builder.append("<col=ff0000>");
        } else {
            builder.append("<col=ffff00>");
        }
        return builder.append(" (level-").append(combatLevelRemote).append(")</col>").toString();
    }
}

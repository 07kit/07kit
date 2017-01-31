package com.kit.plugins.hiscore;

import com.kit.api.event.ActionEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.event.PlayerMenuCreatedEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.wrappers.hiscores.HiscoreLookup;
import com.kit.api.wrappers.hiscores.HiscoreType;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.game.engine.renderable.entity.IPlayer;
import com.kit.gui.ControllerManager;
import com.kit.gui.controller.SidebarController;
import com.kit.plugins.quickchat.QuickChatPlugin;
import com.kit.api.event.ActionEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.event.PlayerMenuCreatedEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.wrappers.hiscores.HiscoreLookup;
import com.kit.api.wrappers.hiscores.HiscoreType;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.game.engine.renderable.entity.IPlayer;
import com.kit.gui.ControllerManager;
import com.kit.gui.controller.SidebarController;
import com.kit.plugins.quickchat.QuickChatPlugin;
import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.plugin.Option;
import com.kit.api.wrappers.hiscores.HiscoreLookup;
import com.kit.core.control.PluginManager;
import com.kit.plugins.quickchat.QuickChatPlugin;

/**
 */
public class HiscorePlugin extends Plugin {

    public static final int HISCORES_OPCODE = 16000;

    private final HiscoreSidebarWidget widget = new HiscoreSidebarWidget(this);

    @Option(label = "Show hicores in chatbox", type = Option.Type.TOGGLE, value = "false")
    private boolean showHiscoresInChat;

    public HiscorePlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Hiscore";
    }

    @Override
    public void start() {
        ui.registerSidebarWidget(widget);
    }

    @Override
    public void stop() {
        ui.deregisterSidebarWidget(widget);
    }

    public void search(String player) {
        HiscoreLookup result = hiscores.lookup(player, HiscoreType.STANDARD);
        if (result != null) {
            widget.setHiscores(player, result);
        } else {
            widget.setHiscores(player, null);
        }
    }

    @Override
    public boolean hasOptions() {
        return false;
    }

    private String getCombatLevel(IPlayer player) {
        int combatLevelLocal = Session.get().player.getCombatLevel();
        int combatLevelRemote = player.getCombatLevel();
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

    @EventHandler
    public void onMenuClick(ActionEvent evt) {
        if (evt.getOpcode() == ActionEvent.Opcode.HISCORES_PLAYER) {
            String playerName = evt.getEntityName().substring(0, evt.getEntityName().indexOf('<'));

            if (!showHiscoresInChat && isEnabled()) {
                getWidget().setHiscores(playerName, Session.get().hiscores.lookup(playerName, HiscoreType.STANDARD));//TODO support other types
                SidebarController controller = ControllerManager.get(SidebarController.class);
                controller.changeTo(controller.getSidebarButton(getWidget()));
            } else {
                QuickChatPlugin.sendThroughChatBox(QuickChatPlugin.getAllStats(Session.get(), playerName),
                        Session.get().player.getName(), null, MessageEvent.Type.MESSAGE_CHAT, true);
            }
        }
    }

    @EventHandler
    public void onMenuCreation(PlayerMenuCreatedEvent evt) {
        if (evt.getPlayer().getName().equals(Session.get().player.getName())) {
            return;
        }
        client().addMenuEntry("Hiscores", evt.getPlayer().getName() + getCombatLevel(evt.getPlayer()),
                HISCORES_OPCODE, evt.getVar0(), evt.getVar1(), evt.getVar2());
    }

    public HiscoreSidebarWidget getWidget() {
        return widget;
    }
}

package com.kit.plugins.quickhop;

import com.kit.api.event.ActionEvent;
import com.kit.api.wrappers.World;
import com.kit.api.event.ActionEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.event.LoginEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.World;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuickHopPlugin extends Plugin {

    public static final int FRIENDS_WIDGET_GROUP_ID = 429;
    public static final int FRIENDS_WIDGET_CHILD_ID = 3;
    public static final String HOP_TO_FRIEND_ACTION = "Hop to";

    private final Map<String, Integer> friendWorldMap = new HashMap<>();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final QuickHopSidebarWidget widget = new QuickHopSidebarWidget(this);

    private boolean refreshWidgets = false;

    private boolean pingingWorlds;

    public QuickHopPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Quick Hop";
    }

    @Override
    public void start() {
        ui.registerSidebarWidget(widget);
    }

    @Override
    public void stop() {
        ui.deregisterSidebarWidget(widget);
    }

    @EventHandler
    public void onTabChange(ActionEvent event) {
        if (event.getOpcode() != null &&
                event.getOpcode() == ActionEvent.Opcode.CLICK_WIDGET && event.getInteraction() != null &&
                event.getInteraction().equals("Friends List")) {
            refreshWidgets = true;
        }
    }

    @EventHandler
    public void onFriendHop(ActionEvent event) {
        if (event.getOpcode() != null &&
                event.getOpcode() == ActionEvent.Opcode.CLICK_WIDGET && event.getInteraction() != null &&
                event.getInteraction().equals(HOP_TO_FRIEND_ACTION) &&
                event.getEntityName()!= null) {
            int startIdx = event.getEntityName().indexOf('>');
            int endIdx = event.getEntityName().indexOf('<', startIdx);
            if (startIdx > 0 && endIdx > 0) {
                String friend = event.getEntityName().substring(startIdx + 1, endIdx);
                Integer worldId = friendWorldMap.get(friend.trim());
                if (worldId != null) {
                    for (World world : Session.get().worlds.getAll()) {
                        if (world.getId() == worldId) {
                            Session.get().worlds.switchTo(world);
                            return;
                        }
                    }
                }
            }
        }
    }

    @Schedule(500)
    public void refreshWidgets() {
        if (refreshWidgets) {
            Widget friendWidgetsParent = Session.get().widgets.find(FRIENDS_WIDGET_GROUP_ID, FRIENDS_WIDGET_CHILD_ID);
            if (friendWidgetsParent != null) {
                Widget[] friendWidgets = friendWidgetsParent.getChildren();
                if (friendWidgets != null) {
                    friendWorldMap.clear();
                    Map<String, Widget> widgetsToAddHopTo = new HashMap<>();
                    String lastFriend = null;
                    for (Widget friendWidget : friendWidgets) {
                        if (friendWidget == null || friendWidget.getActions() == null || friendWidget.getActions().length == 0) {
                            if (friendWidget != null && friendWidget.getText() != null
                                    && friendWidget.getText().contains("World") && lastFriend != null) {
                                try {
                                    String[] data = friendWidget.getText().split(" ");
                                    if (data.length > 1) {
                                        int world = Integer.parseInt(data[1].trim());
                                        friendWorldMap.put(lastFriend, world);
                                    }
                                } catch (Exception e) {
                                    logger.error("Unable to get world for friend [" + lastFriend + "]", e);
                                }
                                lastFriend = null;
                            }
                            continue;
                        }
                        lastFriend = friendWidget.getText();
                        widgetsToAddHopTo.put(lastFriend, friendWidget);
                    }
                    for (Map.Entry<String, Integer> friendEntry : friendWorldMap.entrySet()) {
                        Widget widget = widgetsToAddHopTo.get(friendEntry.getKey());
                        if (widget != null) {
                            List<String> newActions = new ArrayList<>();
                            String[] actions = widget.getActions();
                            for (String action : actions) {
                                if (action.equals(HOP_TO_FRIEND_ACTION)) {
                                    continue;
                                }
                                newActions.add(action);
                            }
                            newActions.add(HOP_TO_FRIEND_ACTION);
                            widget.unwrap().setActions(newActions.toArray(new String[newActions.size()]));
                        }
                    }
                }
            }
            refreshWidgets = false;
        }
    }

    @Schedule(180000)
    public void update() {
        if (!pingingWorlds) {
            logger.info("Updating world list.");
            pingingWorlds = true;
            executorService.submit((Runnable) widget::update);
        }
    }

    @EventHandler
    public void onLogin(LoginEvent evt) {
        client().loadWorlds();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client().loadWorlds();
    }

    public boolean isPingingWorlds() {
        return pingingWorlds;
    }

    public void setPingingWorlds(boolean pingingWorlds) {
        this.pingingWorlds = pingingWorlds;
    }

    @Override
    public boolean hasOptions() {
        return false;
    }

}

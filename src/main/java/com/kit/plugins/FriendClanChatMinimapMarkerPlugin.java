package com.kit.plugins;

import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.ClanChatMember;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.ClanChatMember;
import com.kit.api.wrappers.Friend;
import com.kit.api.wrappers.Player;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FriendClanChatMinimapMarkerPlugin extends Plugin {

    public static final Color FRIENDS_COLOR = new Color(0, 244, 0);
    public static final Color CLAN_CHAT_MEMBERS_COLOR = new Color(242, 53, 23);

    private Map<String, Point> friendsMM = new HashMap<>();
    private Map<String, Point> clanChatMembersMM = new HashMap<>();

    private Map<String, Point> friendsScreen = new HashMap<>();
    private Map<String, Point> clanChatMembersScreen = new HashMap<>();

    @Option(label = "Mark friends on minimap", value = "true", type = Option.Type.TOGGLE)
    private boolean markFriendsMinimap;
    @Option(label = "Mark friends on screen", value = "true", type = Option.Type.TOGGLE)
    private boolean markFriendsScreen;

    @Option(label = "Mark clan chat members on minimap", value = "true", type = Option.Type.TOGGLE)
    private boolean markClanChatMembersMinimap;
    @Option(label = "Mark clan chat members on screen", value = "true", type = Option.Type.TOGGLE)
    private boolean markClanChatMembersScreen;


    public FriendClanChatMinimapMarkerPlugin(PluginManager manager) {
        super(manager);
    }

    @Schedule(600)
    public void updatePlayersToMark() {
        try {
            if (!Session.get().isLoggedIn()) {
                return;
            }

            List<String> friendsNames = Session.get().friends.getAll().stream()
                    .filter(f -> f.getName() != null && f.getName().length() > 0)
                    .map(Friend::getName).collect(Collectors.toList());
            List<String> clanChatMemberNames = Session.get().clanChat.getAllMembers().stream()
                    .filter(f -> f.getName() != null && f.getName().length() > 0 &&
                            !f.getName().equals(Session.get().player.getName()))
                    .map(ClanChatMember::getName).collect(Collectors.toList());
            friendsMM = getMMMapFor(friendsNames);
            clanChatMembersMM = getMMMapFor(clanChatMemberNames);
            friendsScreen = getScreenMapFor(friendsNames);
            clanChatMembersScreen = getScreenMapFor(clanChatMemberNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Point> getMMMapFor(List<String> names) {
        List<Player> players = Session.get().players.find(names.toArray(new String[names.size()])).asList();

        Map<String, Point> map = new HashMap<>();

        for (Player player : players) {
            Point minimap = Session.get().minimap.convert(player.getTile());
            if (minimap.x > -1 && minimap.y > -1) {
                map.put(player.getName(), minimap);
            }
        }
        return map;
    }

    private Map<String, Point> getScreenMapFor(List<String> names) {
        List<Player> players = Session.get().players.find(names.toArray(new String[names.size()])).asList();

        Map<String, Point> map = new HashMap<>();

        for (Player player : players) {
            Point screen = Session.get().viewport.convert(player.getTile());
            if (screen.x > -1 && screen.y > -1) {
                map.put(player.getName(), screen);
            }
        }
        return map;
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        if (isLoggedIn()) {
            return;
        }

        Graphics2D g2d = (Graphics2D) event.getGraphics();

        Font old = g2d.getFont();
        g2d.setFont(g2d.getFont().deriveFont(8f));

        g2d.setColor(FRIENDS_COLOR);
        if (markFriendsMinimap) {
            for (Map.Entry<String, Point> stringPointEntry : friendsMM.entrySet()) {
                PaintUtils.drawString(g2d, stringPointEntry.getKey(),
                        stringPointEntry.getValue().x + 1, stringPointEntry.getValue().y);
            }
        }
        if (markFriendsScreen && !bank.isOpen()) {
            for (Map.Entry<String, Point> stringPointEntry : friendsScreen.entrySet()) {
                PaintUtils.drawString(g2d, stringPointEntry.getKey(),
                        stringPointEntry.getValue().x + 1, stringPointEntry.getValue().y);
            }
        }

        g2d.setColor(CLAN_CHAT_MEMBERS_COLOR);
        if (markClanChatMembersMinimap) {
            for (Map.Entry<String, Point> stringPointEntry : clanChatMembersMM.entrySet()) {
                PaintUtils.drawString(g2d, stringPointEntry.getKey(),
                        stringPointEntry.getValue().x + 1, stringPointEntry.getValue().y);
            }
        }
        if (markClanChatMembersScreen && !bank.isOpen()) {
            for (Map.Entry<String, Point> stringPointEntry : clanChatMembersScreen.entrySet()) {
                PaintUtils.drawString(g2d, stringPointEntry.getKey(),
                        stringPointEntry.getValue().x + 1, stringPointEntry.getValue().y);
            }
        }

        g2d.setFont(old);
    }

    @Override
    public String getName() {
        return "Friend & Clan Chat Markers";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}

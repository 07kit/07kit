package com.kit.plugins.clan;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NotificationsUtil;
import com.kit.api.wrappers.Skill;
import com.kit.api.wrappers.Tile;
import com.kit.core.Session;
import com.kit.core.model.*;
import com.kit.plugins.clan.backend.ClanInfo;
import com.kit.plugins.clan.backend.ClanRank;
import com.kit.plugins.map.WorldMapPlugin;
import com.kit.plugins.map.marker.ClanMemberMarker;
import com.kit.plugins.map.marker.Marker;
import com.kit.socket.event.*;
import com.kit.api.event.EventHandler;
import com.kit.api.event.LoginEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NotificationsUtil;
import com.kit.api.wrappers.Player;
import com.kit.api.wrappers.Skill;
import com.kit.api.wrappers.Tile;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.core.model.Container;
import com.kit.plugins.clan.backend.ClanRank;
import com.kit.plugins.clan.backend.ClanService;
import com.kit.plugins.clan.backend.ClanInfo;
import com.kit.plugins.map.WorldMapPlugin;
import com.kit.plugins.map.marker.ClanMemberMarker;
import com.kit.plugins.map.marker.Marker;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ClanPlugin extends Plugin {

    public static final Gson GSON = new Gson();

    public static final Color LOW_HEALTH_COLOR = new Color(220, 1, 17);
    public static final Color MEDIUM_HEALTH_COLOR = new Color(255, 237, 54);
    public static final Color HIGH_HEALTH_COLOR = new Color(27, 220, 28);

    private class AutoJoinClan {
        @SerializedName("loginName")
        private String loginName;
        @SerializedName("clanId")
        private long clanId;

        public AutoJoinClan(String loginName, long clanId) {
            this.loginName = loginName;
            this.clanId = clanId;
        }
    }

    private final com.kit.core.model.Container<AutoJoinClan> autoJoinContainer = new com.kit.core.model.Container<AutoJoinClan>("clan_auto_join") {
        @Override
        public Type getElementsType() {
            return new TypeToken<List<AutoJoinClan>>() {
            }.getType();
        }
    };

    @Option(label = "Send my levels to my clan", type = Option.Type.TOGGLE, value = "true")
    private boolean sendLevels;
    @Option(label = "Send my location to my clan", type = Option.Type.TOGGLE, value = "true")
    private boolean sendLocation;
    @Option(label = "Display the health of my clan members when nearby", type = Option.Type.TOGGLE, value = "true")
    private boolean displayMembersHealth;
    @Option(label = "Display the location of my clan members on the map", type = Option.Type.TOGGLE, value = "true")
    private boolean displayMembersLocation;

    private Map<String, ClanMemberInfo> clanMembers = new HashMap<>();
    private Map<String, Player> nearbyClanMembers = new HashMap<>();
    private Map<String, ClanMemberMarker> clanMemberMarkers = new HashMap<>();
    private Map<String, ClanMemberInfo> ingameNameToInfo = new HashMap<>();

    private ClanInfo currentClan;

    private Map<Skill, Integer> lastCurrentLevels = new HashMap<>();
    private Map<Skill, Integer> lastBaseLevels = new HashMap<>();
    private int lastLoginIndex = -1;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private Tile lastLocation;

    private final ClanSidebarWidget clanSidebarWidget;
    private final ClanMemberOverlay clanMemberOverlay;

//    private WorldMapPlugin worldMap;

    public ClanPlugin(PluginManager manager) {
        super(manager);
        this.clanSidebarWidget = new ClanSidebarWidget(this);
        this.clanMemberOverlay = new ClanMemberOverlay(this);
    }

    @Override
    public String getName() {
        return "Clan";
    }

    @Override
    public void start() {
        ui.registerSidebarWidget(clanSidebarWidget);
        Session.get().getEventBus().register(clanMemberOverlay);
//        worldMap = (WorldMapPlugin) getManager().getPlugins().stream().filter(p -> p.getClass().equals(WorldMapPlugin.class))
//                .findFirst().get();
    }

    @Override
    public void stop() {
        ui.deregisterSidebarWidget(clanSidebarWidget);
        Session.get().getEventBus().deregister(clanMemberOverlay);
    }

    @EventHandler
    public void onClientAuthenticated(AuthenticateEvent evt) {
        if (evt.getStatus() == AuthenticateEvent.Status.SUCCESS && Session.get().isLoggedIn() &&
                currentClan != null && Session.get().player != null) {
            updateAll();
        }
    }

    private void updateAll() {
        String username = Session.get().getClient().getUsername();
        String ingameName = Session.get().player.getName();
        if (username == null || username.trim().length() == 0 ||
                ingameName == null || ingameName.trim().length() == 0) {
            return;
        }
        for (Skill skill : Skill.values()) {
            int newCurrent = Session.get().skills.getLevel(skill);
            int newBase = Session.get().skills.getBaseLevel(skill);

            Session.get().getSocketClient().getSocket().emit(
                    PlayerLevelEvent.EVENT_NAME, GSON.toJson(new PlayerLevelEvent(
                            newCurrent, newBase, username,
                            Session.get().player.getName(), skill.name()))
            );
        }
        if (Session.get().player.getTile() != null) {
            Session.get().getSocketClient().getSocket().emit(
                    PlayerLocationEvent.EVENT_NAME, GSON.toJson(new PlayerLocationEvent(
                                    username,
                                    Session.get().player.getName(),
                                    Session.get().player.getTile().getX(),
                                    Session.get().player.getTile().getY(),
                                    Session.get().player.getTile().getZ()
                            )
                    ));
        }
        ClanRank rank = getCurrentClan().getRank();
        Session.get().getSocketClient().getSocket().emit(ClanRankEvent.EVENT_NAME, GSON.toJson(
                new ClanRankEvent(
                        rank.getClanId(),
                        rank.getUserId(),
                        username,
                        ingameName,
                        ClanRankEvent.Status.valueOf(rank.getStatus().name()),
                        rank.getLastWorld(),
                        ClanRankEvent.Rank.valueOf(rank.getRank().name())
                )
        ));
    }

    public Color getHealthColor(ClanSkillEvent health) {
        if (health == null) {
            return null;
        }
        double healthPercentage = ((double) health.getCurrentLevel() / (double) health.getBaseLevel()) * 100.0D;
        if (healthPercentage <= 20) {
            return LOW_HEALTH_COLOR;
        } else if (healthPercentage <= 50) {
           return MEDIUM_HEALTH_COLOR;
        } else {
            return HIGH_HEALTH_COLOR;
        }
    }

    @EventHandler
    public void onClanSkill(ClanSkillEvent evt) {
        if (currentClan == null || evt.getLoginNameToken() == null) {
            return;
        }
        ClanMemberInfo info = clanMembers
                .computeIfAbsent(evt.getLoginNameToken(), k -> new ClanMemberInfo(new HashMap<>(), null, currentClan.getRank()));
        info.getSkills().put(evt.getSkill(), evt);
    }

    @EventHandler
    public void onClanRank(ClanRankEvent evt) {
        if (currentClan == null || evt.getLoginNameToken() == null) {
            return;
        }
        ClanMemberInfo info = clanMembers.get(evt.getLoginNameToken());
        if (info == null) {
            info = new ClanMemberInfo(new HashMap<>(), null, currentClan.getRank());
            clanMembers.put(evt.getLoginNameToken(), info);
            ingameNameToInfo.put(evt.getIngameName(), info);
        }
        info.setRank(new ClanRank(currentClan.getClanId(),
                evt.getUserId(), evt.getLoginNameToken(), evt.getIngameName(), ClanRank.Status.valueOf(evt.getStatus().name()),
                evt.getLastWorld(), ClanRank.Rank.valueOf(evt.getRank().name()))
        );
        currentClan.getRanksMap().put(evt.getLoginNameToken(), info.getRank());
        clanSidebarWidget.refreshAll();
    }

    @EventHandler
    public void onClanLocation(ClanLocationEvent evt) {
        if (currentClan == null || evt.getLoginNameToken() == null) {
            return;
        }
        ClanMemberInfo info = clanMembers
                .computeIfAbsent(evt.getLoginNameToken(), k -> new ClanMemberInfo(new HashMap<>(), null, currentClan.getRank()));
        info.setLastPosition(new Tile(evt.getX(), evt.getY(), evt.getPlane()));
    }


    @Schedule(60000)
    public void sendMe() {
        if (!Session.get().isLoggedIn() || Session.get().player == null ||
                !Session.get().getSocketClient().isAuthenticated() || getCurrentClan() == null) {
            return;
        }
        String username = Session.get().getClient().getUsername();
        String ingameName = Session.get().player.getName();
        if (username == null || username.trim().length() == 0 ||
                ingameName == null || ingameName.trim().length() == 0) {
            return;
        }
        ClanRank rank = getCurrentClan().getRank();
        Session.get().getSocketClient().getSocket().emit(ClanRankEvent.EVENT_NAME, GSON.toJson(
                new ClanRankEvent(
                        rank.getClanId(),
                        rank.getUserId(),
                        username,
                        ingameName,
                        ClanRankEvent.Status.valueOf(rank.getStatus().name()),
                        rank.getLastWorld(),
                        ClanRankEvent.Rank.valueOf(rank.getRank().name())
                )
        ));
    }

    @Schedule(500)
    public void updateClanMembers() {
        if (Session.get().getClient().getLoginIndex() < 30 || getCurrentClan() == null) {
//            List<Marker> markers =
//                    worldMap.getMarkers().stream()
//                            .filter(m -> m.getClass().equals(ClanMemberMarker.class)).collect(Collectors.toList());
//            markers.forEach(m -> worldMap.removeMarker(m));
            nearbyClanMembers.clear();
            clanMemberMarkers.clear();
            clanMembers.clear();
            ingameNameToInfo.clear();
        }
        if (!isDisplayMembersLocation() || Session.get().player == null) {
            return;
        }
        Set<String> ingameNames = ingameNameToInfo.keySet();
        for (Player player : Session.get().players.find(ingameNames
                .toArray(new String[ingameNames.size()])).onMinimap().asList()) {
            if (player.getName() != null && !player.getName().equals(Session.get().player.getName())) {
                nearbyClanMembers.put(ingameNameToInfo.get(player.getName()).getRank().getLoginNameToken(), player);
            }
        }
        for (Map.Entry<String, ClanMemberInfo> clanMemberInfoEntry : clanMembers.entrySet()) {
            if (clanMemberInfoEntry.getValue().getRank().getIngameName().equals(Session.get().player.getName())) {
                continue;
            }
            ClanRank rank = clanMemberInfoEntry.getValue().getRank();
            if (rank != null) {
                ClanMemberMarker marker = clanMemberMarkers.get(clanMemberInfoEntry.getKey());
                if (rank.getStatus() != ClanRank.Status.IN_GAME &&
                        marker != null) {
//                    worldMap.removeMarker(marker);
                    clanMemberMarkers.remove(clanMemberInfoEntry.getKey());
                } else if (marker == null) {
//                    marker = new ClanMemberMarker(worldMap, clanMemberInfoEntry.getValue(), this);
//                    worldMap.addMarker(marker);
                    clanMemberMarkers.put(clanMemberInfoEntry.getKey(), marker);
                }
            }
        }
    }

    @Schedule(500)
    public void updateSkills() {
        String username = Session.get().getClient().getUsername();
        if (!isSendLevels() || !Session.get().isLoggedIn() ||
                Session.get().player == null || getCurrentClan() == null ||
                username == null || username.trim().length() == 0 ||
                !Session.get().getSocketClient().isAuthenticated()) {
            return;
        }
        for (Skill skill : Skill.values()) {
            int newCurrent = Session.get().skills.getLevel(skill);
            int newBase = Session.get().skills.getBaseLevel(skill);
            Integer lastCurrent = lastCurrentLevels.get(skill);
            Integer lastBase = lastBaseLevels.get(skill);
            if (lastCurrent == null || lastBase == null) {
                lastCurrentLevels.put(skill, newCurrent);
                lastBaseLevels.put(skill, newBase);
            } else if (newCurrent == lastCurrent.intValue() && newBase == lastBase.intValue()) {
                return;
            }

            Session.get().getSocketClient().getSocket().emit(
                    PlayerLevelEvent.EVENT_NAME, GSON.toJson(new PlayerLevelEvent(
                            newCurrent, newBase, username,
                            Session.get().player.getName(), skill.name()))
            );
            lastCurrentLevels.put(skill, newCurrent);
            lastBaseLevels.put(skill, newBase);
        }
    }

    @Schedule(500)
    public void updateLocation() {
        String username = Session.get().getClient().getUsername();
        if (!isSendLocation() || !Session.get().isLoggedIn() ||
                Session.get().player == null || currentClan == null ||
                username == null || username.trim().length() == 0 ||
                !Session.get().getSocketClient().isAuthenticated()) {
            return;
        }
        if (Session.get().player.getTile() != null && !Session.get().player.getTile().equals(lastLocation)) {
            Session.get().getSocketClient().getSocket().emit(
                    PlayerLocationEvent.EVENT_NAME, GSON.toJson(new PlayerLocationEvent(
                                    username,
                                    Session.get().player.getName(),
                                    Session.get().player.getTile().getX(),
                                    Session.get().player.getTile().getY(),
                                    Session.get().player.getTile().getZ()
                            )
                    ));
            lastLocation = Session.get().player.getTile();
        }
    }

    private void autoJoinClan(String loginName, String ingameName) {
        AutoJoinClan autoJoinClan = autoJoinContainer.getAll().stream().filter(l ->
                l.loginName.equals(loginName))
                .findFirst().orElse(null);
        if (autoJoinClan != null) {
            long clanId = autoJoinClan.clanId;
            if (clanId > -1) {
                setCurrentClan(ClanService.join(
                        loginName,
                        ingameName,
                        null,
                        clanId,
                        true,
                        Session.get().getClient().getLoginIndex() >= 30 ? ClanRank.Status.IN_GAME : ClanRank.Status.OFFLINE,
                        Session.get().worlds.getCurrent()));
            } else {
                NotificationsUtil.showNotification("Clan", "Unable to auto join clan");
            }
        }
    }

    @EventHandler
    public void onLogin(LoginEvent evt) {
        if (!Session.get().getSocketClient().isAuthenticated()) {
            clanSidebarWidget.refreshAll();
            return;
        }
        String ingameName = Session.get().player != null ? Session.get().player.getName() : null;
        String loginName = Session.get().getClient().getUsername();
        if (loginName != null && loginName.trim().length() > 0) {
            clanSidebarWidget.setLoading(true);
            autoJoinClan(loginName, ingameName);
            clanSidebarWidget.setLoading(false);
        }
        if (getCurrentClan() != null && ingameName != null) {
            ClanRank rank = getCurrentClan().getRank();
            rank.setStatus(ClanRank.Status.IN_GAME);
            rank.setLastWorld(Session.get().worlds.getCurrent());
            Session.get().getSocketClient().getSocket().emit(ClanRankEvent.EVENT_NAME, GSON.toJson(
                    new ClanRankEvent(
                            rank.getClanId(),
                            rank.getUserId(),
                            loginName,
                            ingameName,
                            ClanRankEvent.Status.valueOf(rank.getStatus().name()),
                            rank.getLastWorld(),
                            ClanRankEvent.Rank.valueOf(rank.getRank().name())
                    )
            ));
            clanSidebarWidget.refreshAll();
        }
    }

    @Schedule(1000)
    public void onLogout() {
        int newLoginIndex = Session.get().getClient().getLoginIndex();
        if (newLoginIndex == 10 && newLoginIndex != lastLoginIndex) {
            setCurrentClan(null);
        }
        if (!Session.get().getSocketClient().isAuthenticated() ||
                newLoginIndex == lastLoginIndex) {
            return;
        }
        lastLoginIndex = newLoginIndex;
    }

    public ClanInfo getCurrentClan() {
        return currentClan;
    }

    public void setCurrentClan(ClanInfo currentClan) {
        if (currentClan == null && this.currentClan != null) {
            Session.get().getSocketClient().getSocket().emit(LeaveClanEvent.EVENT_NAME,
                    GSON.toJson(new LeaveClanEvent(getCurrentClan().getClanId())));
        }
        boolean refresh = this.currentClan == null && currentClan != null ||
                this.currentClan != null && currentClan == null ||
                (this.currentClan != null && !this.currentClan.equals(currentClan));
        this.currentClan = currentClan;
        if (currentClan != null && Session.get().getClient().getLoginIndex() >= 30) {
            Session.get().getSocketClient().getSocket().emit(JoinClanEvent.EVENT_NAME, GSON.toJson(
                    new JoinClanEvent(currentClan.getClanId(), Session.get().client().getUsername())
            ));
            executorService.submit(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    logger.error("Error sleeping", e);
                }
                updateAll();
                ;
            });
        }
        if (refresh || Session.get().getClient().getLoginIndex() < 30) {
            clanSidebarWidget.refreshAll();
        }
    }

    public void removeAutoJoin(ClanInfo info, String username) {
        AutoJoinClan remove = null;
        for (AutoJoinClan autoJoinClan : autoJoinContainer.getAll()) {
            if (autoJoinClan.loginName.equals(username) && autoJoinClan.clanId == info.getClanId()) {
                remove = autoJoinClan;
                break;
            }
        }
        if (remove != null) {
            autoJoinContainer.remove(remove);
            autoJoinContainer.save();
        }
    }

    public void addAutoJoinClan(ClanInfo info, String username) {
        for (AutoJoinClan autoJoinClan : autoJoinContainer.getAll()) {
            if (autoJoinClan.loginName.equals(username) && autoJoinClan.clanId == info.getClanId()) {
                return;
            }
        }
        autoJoinContainer.add(new AutoJoinClan(username, info.getClanId()));
        autoJoinContainer.save();
    }

    public boolean isDisplayMembersHealth() {
        return displayMembersHealth;
    }

    public Map<String, ClanMemberInfo> getClanMembers() {
        return clanMembers;
    }

    public Map<String, ClanMemberMarker> getClanMemberMarkers() {
        return clanMemberMarkers;
    }

    public Map<String, Player> getNearbyClanMembers() {
        return nearbyClanMembers;
    }

    public boolean isDisplayMembersLocation() {
        return displayMembersLocation;
    }

    public boolean isSendLevels() {
        return sendLevels;
    }

    public boolean isSendLocation() {
        return sendLocation;
    }
}

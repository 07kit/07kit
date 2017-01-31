package com.kit.plugins.clan;

import com.kit.Application;
import com.kit.api.wrappers.World;
import com.kit.gui.component.*;
import com.kit.plugins.map.marker.ClanMemberMarker;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.painter.BusyPainter;
import com.kit.Application;
import com.kit.api.util.NotificationsUtil;
import com.kit.api.wrappers.World;
import com.kit.core.Session;
import com.kit.http.SuccessResponse;
import com.kit.plugins.clan.backend.ClanInfo;
import com.kit.plugins.clan.backend.ClanService;
import com.kit.plugins.clan.backend.ClanRank;
import com.kit.plugins.clan.backend.UpdateRankRequest;
import com.kit.socket.event.ClanSkillEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ClanSidebarWidget extends JPanel implements SidebarWidget {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Image NORMAL_ICON = IconFontSwing.buildImage(FontAwesome.SMILE_O, 20, Color.GRAY);
    private final Image TOGGLED_ICON = IconFontSwing.buildImage(FontAwesome.SMILE_O, 20, Color.WHITE);

    private static final Color F2P_COLOR = new Color(237, 237, 237);
    private static final Color P2P_COLOR = new Color(255, 229, 54);
    private final Image F2P_ICON = IconFontSwing.buildImage(FontAwesome.STAR, 15, F2P_COLOR);
    private final Image P2P_ICON = IconFontSwing.buildImage(FontAwesome.STAR, 15, P2P_COLOR);

    private final ClanPlugin clanPlugin;
    private MateTabbedPane tabbedPane;
    private MateTabbedPane.Tab lobbyTab;
    private MateTabbedPane.Tab homeTab;
    private MateTabbedPane.Tab manageTab;
    private JPanel loadingPanel;
    private MateButton leaveBtn;
    private boolean loading;

    public ClanSidebarWidget(ClanPlugin clanPlugin) {
        this.clanPlugin = clanPlugin;
        setLayout(new BorderLayout());
        setBackground(Application.COLOUR_SCHEME.getDark());

        loadingPanel = createLoadingContainer();

        tabbedPane = new MateTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        refreshAll();
    }

    public void setLoading(boolean loading) {
        if (loading && !this.loading) {
            remove(tabbedPane);
            if (leaveBtn != null) {
                remove(leaveBtn);
                leaveBtn = null;
            }
            add(loadingPanel);
        } else if (!loading && this.loading) {
            remove(loadingPanel);
            add(tabbedPane, BorderLayout.CENTER);

            refreshAll();
        }
        this.loading = loading;
        revalidate();
        repaint();
    }

    private JPanel createLoadingContainer() {
        return new LoadingContainer();
    }

    private MateScrollPane createHome() {
        JPanel homePanel = createHomePanel();

        MateScrollPane homeScrollPane = new MateScrollPane(homePanel);
        homeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        homeScrollPane.getVerticalScrollBar().setUnitIncrement(75);

        return homeScrollPane;
    }

    private MateScrollPane createLobby() {
        JPanel lobbyPanel = createLobbyPanel();

        MateScrollPane lobbyScrollPane = new MateScrollPane(lobbyPanel);
        lobbyScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        lobbyScrollPane.getVerticalScrollBar().setUnitIncrement(75);

        return lobbyScrollPane;
    }

    private MateScrollPane createManage() {
        JPanel managePanel = createManagePanel();

        MateScrollPane manageScrollPane = new MateScrollPane(managePanel);
        manageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        manageScrollPane.getVerticalScrollBar().setUnitIncrement(75);

        return manageScrollPane;
    }

    public synchronized void refreshAll() {
        SwingUtilities.invokeLater(() -> {
            if (clanPlugin.getCurrentClan() == null) {
                if (leaveBtn != null) {
                    remove(leaveBtn);
                    leaveBtn = null;
                }
                lobbyTab = null;
                manageTab = null;
                tabbedPane.removeAll();

                homeTab = tabbedPane.addTab("Home", createHome());
            } else {
                if (homeTab != null) {
                    tabbedPane.removeTab(homeTab);
                    homeTab = null;
                }
                if (lobbyTab != null) {
                    lobbyTab.setComponent(createLobby());
                    if (tabbedPane.getSelected() != null && tabbedPane.getSelected().equals(lobbyTab)) {
                        lobbyTab.select();
                    }
                } else {
                    lobbyTab = tabbedPane.addTab("Lobby", createLobby());
                }
                if (clanPlugin.getCurrentClan().getRank().getRank() == ClanRank.Rank.OWNER) {
                    if (manageTab != null) {
                        manageTab.setComponent(createManage());
                        if (tabbedPane.getSelected() != null && tabbedPane.getSelected().equals(manageTab)) {
                            manageTab.select();
                        }
                    } else {
                        manageTab = tabbedPane.addTab("Manage", createManage());
                    }
                }
            }
            if (clanPlugin.getCurrentClan() != null && leaveBtn == null) {
                leaveBtn = new MateButton("Leave");
                leaveBtn.addActionListener(e -> {
                    setLoading(true);
                    clanPlugin.setCurrentClan(null);
                    setLoading(false);
                });
                add(leaveBtn, BorderLayout.SOUTH);
            }
            if (tabbedPane.getSelected() == null) {
                tabbedPane.getAt(0).select();
            }
            setLoading(false);
            revalidate();
            repaint();
        });
    }

    public JPanel createManagePanel() {
        JPanel container = new JPanel();
        container.setLayout(new MigLayout("insets 0, gap rel 0"));
        container.setBackground(Application.COLOUR_SCHEME.getDark());

        JLabel pendingUsers = new JLabel("Pending");
        pendingUsers.setForeground(Application.COLOUR_SCHEME.getText());
        pendingUsers.setFont(pendingUsers.getFont().deriveFont(16.0f).deriveFont(Font.BOLD));
        container.add(pendingUsers, "span, growx, pushx, gapleft 70, gaptop 20, gapbottom 10");

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(Application.COLOUR_SCHEME.getText());
        container.add(separator, "gapleft 10, gaptop 0, gapright 10, pushx, growx, span, wrap");

        if (clanPlugin.getCurrentClan() != null) {
            java.util.List<ClanRank> ranks = new ArrayList<>(clanPlugin.getCurrentClan().getRanksMap().values())
                    .stream().filter(r -> r.getRank() == ClanRank.Rank.PENDING).collect(Collectors.toList());

            if (ranks.size() == 0) {
                JLabel noPendingUsers = new JLabel("No pending users :)");
                noPendingUsers.setForeground(Application.COLOUR_SCHEME.getText());
                noPendingUsers.setFont(pendingUsers.getFont().deriveFont(12.0f));
                container.add(noPendingUsers, "span, growx, pushx, gapleft 50, gaptop 10, gapbottom 10");
            } else {
                for (int i = 0; i < ranks.size(); i++) {
                    container.add(new ClanRankApprovalWidget(ranks.get(i), i), "span, growx, pushx");
                }
            }
        }

        container.add(Box.createVerticalStrut(20), "span, growx, pushx");
        return container;
    }

    public JPanel createLobbyPanel() {
        JPanel container = new JPanel();
        container.setLayout(new MigLayout("insets 0, gap rel 0"));
        container.setBackground(Application.COLOUR_SCHEME.getDark());

        if (clanPlugin.getCurrentClan() != null) {
            java.util.List<ClanMemberInfo> infos = new ArrayList<>(clanPlugin.getClanMembers().values())
                    .stream().filter(r -> r.getRank().getRank() != ClanRank.Rank.PENDING
                            && r.getRank().getRank() != ClanRank.Rank.BANNED && r.getRank().getRank() != ClanRank.Rank.REJECTED)
                    .collect(Collectors.toList());
            for (int i = 0; i < infos.size(); i++) {
                container.add(new ClanRankWidget(infos.get(i), i),
                        "span, growx, pushx");
            }
        }

        container.add(Box.createVerticalStrut(20), "span, growx, pushx");
        return container;
    }

    public JPanel createHomePanel() {
        JPanel container = new JPanel();
        container.setLayout(new MigLayout("insets 0, gap rel 0"));
        container.setBackground(Application.COLOUR_SCHEME.getDark());

        if (Session.get().getClient().getLoginIndex() < 30) {
            JLabel lbl = new JLabel("You must be logged in.");
            lbl.setForeground(Application.COLOUR_SCHEME.getText());
            container.add(lbl, "span, growx, pushx, gapleft 30, gaptop 20");
            return container;
        }

        container.add(Box.createVerticalStrut(20), "span, growx, pushx");

        MateCheckBox autoJoin = new MateCheckBox("Auto join?");
        autoJoin.setBackground(Application.COLOUR_SCHEME.getDark());
        MateTextField joinCreateTextField = new MateTextField("Enter clan name");
        MateButton joinButton = new MateButton("Join");
        joinCreateTextField.setMinimumSize(new Dimension(120, 25));
        joinCreateTextField.setBackground(Application.COLOUR_SCHEME.getDark());
        joinCreateTextField.setHorizontalAlignment(SwingConstants.CENTER);
        joinCreateTextField.setForeground(Application.COLOUR_SCHEME.getText());
        joinCreateTextField.setBorder(BorderFactory.createLineBorder(Application.COLOUR_SCHEME.getBright()));
        joinCreateTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    joinButton.doClick();
                }
            }
        });
        container.add(joinCreateTextField, "span, growx, pushx");

        container.add(Box.createVerticalStrut(10), "span, growx, pushx");

        joinButton.addActionListener(e -> {
            String username = Session.get().getClient().getUsername();
            setLoading(true);
            (new SwingWorker<ClanInfo, Void>() {

                @Override
                protected void done() {
                    try {
                        ClanInfo info = get();
                        if (info != null) {
                            if (autoJoin.isSelected()) {
                                clanPlugin.addAutoJoinClan(info, username);
                            } else {
                                clanPlugin.removeAutoJoin(info, username);
                            }
                            clanPlugin.setCurrentClan(info);
                        }
                        joinCreateTextField.setText("");
                        setLoading(false);
                    } catch (InterruptedException | ExecutionException e1) {
                        logger.error("Error joining clan", e1);
                        NotificationsUtil.showNotification("Clan", "Error joining clan");
                        setLoading(false);
                    }
                }

                @Override
                protected ClanInfo doInBackground() throws Exception {
                    if (Session.get().getClient().getLoginIndex() != 30 ||
                            Session.get().player == null || Session.get().player.getName() == null ||
                            username == null || username.trim().length() == 0) {
                        NotificationsUtil.showNotification("Clan", "You must be logged in to join a clan");
                        joinCreateTextField.setText("");
                        setLoading(false);
                        return null;
                    }
                    return ClanService.join(
                            username,
                            Session.get().player.getName(),
                            joinCreateTextField.getText().trim(),
                            -1,
                            false,
                            Session.get().isLoggedIn() ? ClanRank.Status.IN_GAME : ClanRank.Status.OFFLINE,
                            Session.get().worlds.getCurrent()
                    );
                }
            }).execute();
        });

        joinButton.setMinimumSize(new Dimension(90, 20));
        joinButton.setPreferredSize(new Dimension(90, 20));
        joinButton.setMaximumSize(new Dimension(90, 20));
        container.add(joinButton, "gapleft 10, growx, pushx, gapright 4");

        MateButton createButton = new MateButton("Create");
        createButton.addActionListener(e -> {
            setLoading(true);
            String username = Session.get().getClient().getUsername();
            (new SwingWorker<ClanInfo, Void>() {

                @Override
                protected void done() {
                    try {
                        ClanInfo info = get();
                        if (info != null) {
                            if (autoJoin.isSelected()) {
                                clanPlugin.addAutoJoinClan(info, username);
                            } else {
                                clanPlugin.removeAutoJoin(info, username);
                            }

                            clanPlugin.setCurrentClan(info);
                        } else {
                            setLoading(false);
                        }
                    } catch (InterruptedException | ExecutionException e1) {
                        logger.error("Error joining clan", e1);
                        NotificationsUtil.showNotification("Clan", "Error joining clan");
                        setLoading(false);
                    }
                }

                @Override
                protected ClanInfo doInBackground() throws Exception {
                    if (Session.get().getClient().getLoginIndex() != 30 ||
                            Session.get().player == null || Session.get().player.getName() == null ||
                            username == null || username.trim().length() == 0) {
                        NotificationsUtil.showNotification("Clan", "You must be logged in to create a clan");
                        setLoading(false);
                        return null;
                    }
                    return ClanService.create(
                            username,
                            Session.get().player.getName(),
                            joinCreateTextField.getText().trim(),
                            Session.get().isLoggedIn() ? ClanRank.Status.IN_GAME : ClanRank.Status.OFFLINE,
                            Session.get().worlds.getCurrent()
                    );
                }
            }).execute();
        });

        createButton.setMinimumSize(new Dimension(90, 20));
        createButton.setPreferredSize(new Dimension(90, 20));
        createButton.setMaximumSize(new Dimension(90, 20));
        container.add(createButton, "growx, pushx, gapright 10");


        container.add(Box.createVerticalStrut(10), "span, growx, pushx");

        container.add(autoJoin, "gapleft 10, span, growx, pushx");

        return container;
    }

    @Override
    public Component getContent() {
        return this;
    }

    @Override
    public String getTitle() {
        return "Clan";
    }

    @Override
    public Image getIcon(boolean toggled) {
        return toggled ? TOGGLED_ICON : NORMAL_ICON;
    }

    private class ClanRankWidget extends JPanel {

        private World world;
        private ClanMemberInfo info;
        private int idx;

        public ClanRankWidget(ClanMemberInfo info, int idx) {
            this.info = info;
            if (info.getRank().getStatus() == ClanRank.Status.IN_GAME) {
                this.world = Session.get().worlds.getAll().stream().filter(w -> w.getId() == info.getRank().getLastWorld())
                        .findFirst().orElse(null);
            }
            this.idx = idx;

            setLayout(new MigLayout("insets 0, gap rel 0"));
            setBackground(idx % 2 == 0 ? Application.COLOUR_SCHEME.getBright() : Application.COLOUR_SCHEME.getDark());
            setMinimumSize(new Dimension(210, 45));

            JLabel nameLabel = new JLabel(info.getRank().getIngameName());
            nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 13f));
            nameLabel.setForeground(Application.COLOUR_SCHEME.getText());
            add(nameLabel, "gapleft 10, pushx, span, growx");

            JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
            separator.setForeground(Application.COLOUR_SCHEME.getText());
            add(separator, "gapleft 10, gaptop 0, gapright 10, pushx, growx, span, wrap");

            JLabel worldLabel = new JLabel("World:");
            worldLabel.setForeground(Application.COLOUR_SCHEME.getText());
            JLabel worldIdLabel = new JLabel(String.valueOf(world != null ? (world.getId() - 300) : "N/A"));
            worldLabel.setForeground(Application.COLOUR_SCHEME.getText());
            if (world != null) {
                worldIdLabel.setForeground(world.isMembers() ? P2P_COLOR : F2P_COLOR);
            } else {
                worldIdLabel.setForeground(Application.COLOUR_SCHEME.getText());
            }
            add(worldLabel, "gapleft 10");
            add(worldIdLabel, "pushx, span, growx");

            JLabel rankPrefixLabel = new JLabel("Rank:");
            rankPrefixLabel.setForeground(Application.COLOUR_SCHEME.getText());
            JLabel rankLabel = new JLabel(info.getRank().getRank().pretty());
            rankLabel.setForeground(Application.COLOUR_SCHEME.getText());
            add(rankPrefixLabel, "gapleft 10");
            add(rankLabel, "pushx, span, growx");

            JLabel statusPrefixLabel = new JLabel("Status:");
            statusPrefixLabel.setForeground(Application.COLOUR_SCHEME.getText());
            JLabel statusLabel = new JLabel(info.getRank().getStatus().getPrettyName());
            statusLabel.setForeground(Application.COLOUR_SCHEME.getText());
            add(statusPrefixLabel, "gapleft 10");
            add(statusLabel, "pushx, span, growx");

            JLabel healthPrefixLabel = new JLabel("HP:");
            JLabel healthLabel = new JLabel("N/A");
            healthPrefixLabel.setForeground(Application.COLOUR_SCHEME.getText());
            healthLabel.setForeground(Application.COLOUR_SCHEME.getText());
            ClanSkillEvent health = info.getSkills().get(ClanSkillEvent.Skill.HITPOINTS);
            if (health != null) {
                Color healthColor = clanPlugin.getHealthColor(health);
                if (healthColor != null) {
                    healthPrefixLabel.setForeground(Application.COLOUR_SCHEME.getText());
                }
                healthLabel.setText(health.getCurrentLevel() + "/" + health.getBaseLevel());
            }
            add(rankPrefixLabel, "gapleft 10, gapbottom 5");
            add(rankLabel, "pushx, span, growx, gapbottom 5");

            if (info.getRank() != null &&
                    info.getRank().getUserId() != Session.get().getUserAccount().getId()) {
                MatePopupMenu popupMenu = new MatePopupMenu();
                if (info.getRank().getStatus() == ClanRank.Status.IN_GAME) {
                    if (world != null) {
                        MateMenuItem hop = new MateMenuItem("Hop to");
                        hop.addActionListener(e -> {
                            if (clanPlugin.getCurrentClan() == null) {
                                NotificationsUtil.showNotification("Clan", "You're not currently in a clan");
                            } else if (Session.get().isLoggedIn()) {
                                NotificationsUtil.showNotification("Clan", "You must be logged in");
                            } else if (info.getRank().getStatus() != ClanRank.Status.IN_GAME) {
                                NotificationsUtil.showNotification("Clan", info.getRank().getIngameName() + " is not logged in");
                            } else {
                                Session.get().worlds.switchTo(world);
                            }
                        });
                        popupMenu.add(hop);
                    }
                    MateMenuItem locate = new MateMenuItem("Locate on map");
                    locate.addActionListener(e -> {
                        if (clanPlugin.getCurrentClan() == null) {
                            NotificationsUtil.showNotification("Clan", "You're not currently in a clan");
                        } else if (Session.get().isLoggedIn()) {
                            NotificationsUtil.showNotification("Clan", "You must be logged in");
                        } else if (info.getRank().getStatus() != ClanRank.Status.IN_GAME) {
                            NotificationsUtil.showNotification("Clan", info.getRank().getIngameName() + " is not logged in");
                        } else {
                            ClanMemberMarker marker = clanPlugin.getClanMemberMarkers().get(info.getRank().getLoginNameToken());
                            if (marker == null) {
                                NotificationsUtil.showNotification("Clan", "Unable to locate player on map");
                            } else {
                                marker.moveTo();
                            }
                        }
                    });
                    popupMenu.add(locate);
                }
                setComponentPopupMenu(popupMenu);
            }
        }
    }

    private class ClanRankApprovalWidget extends JPanel {

        private ClanRank rank;
        private int idx;

        public ClanRankApprovalWidget(ClanRank rank, int idx) {
            this.rank = rank;
            this.idx = idx;

            setLayout(new MigLayout("insets 0, gap rel 0"));
            setBackground(idx % 2 == 0 ? Application.COLOUR_SCHEME.getBright() : Application.COLOUR_SCHEME.getDark());
            setMinimumSize(new Dimension(210, 30));

            JLabel nameLabel = new JLabel(rank.getIngameName());
            nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 13f));
            nameLabel.setForeground(Application.COLOUR_SCHEME.getText());
            add(nameLabel, "gapleft 10, pushx, span, growx");

            JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
            separator.setForeground(Application.COLOUR_SCHEME.getText());
            add(separator, "gapleft 10, gaptop 0, gapright 10, pushx, growx, span, wrap");

            MateButton approveButton = new MateButton("Approve");
            approveButton.setMinimumSize(new Dimension(90, 20));
            approveButton.setPreferredSize(new Dimension(90, 20));
            approveButton.setMaximumSize(new Dimension(90, 20));
            approveButton.addActionListener(e -> {
                setLoading(true);
                (new SwingWorker<SuccessResponse, Void>() {
                    String username = Session.get().getClient().getUsername();

                    @Override
                    protected void done() {
                        try {
                            if (get() != null) {
                                refreshAll();
                            }
                        } catch (InterruptedException | ExecutionException e1) {
                            logger.error("Error approving member", e1);
                            NotificationsUtil.showNotification("Clan", "Error approving member.");
                            setLoading(false);
                        }
                    }

                    @Override
                    protected SuccessResponse doInBackground() throws Exception {
                        if (!Session.get().isLoggedIn() ||
                                username == null ||
                                username.trim().length() == 0) {
                            NotificationsUtil.showNotification("Clan", "You need to be logged in to do that.");
                            setLoading(false);
                            return null;
                        }
                        rank.setRank(ClanRank.Rank.FRIEND);
                        return ClanService.updateMemberRank(new UpdateRankRequest(
                                clanPlugin.getCurrentClan().getClanId(),
                                username,
                                rank.getUserId(),
                                rank.getLoginNameToken(),
                                rank.getRank()
                        ));
                    }
                }).execute();

            });
            add(approveButton, "gapleft 10, growx, pushx, gapright 4, gapbottom 5");

            MateButton rejectButton = new MateButton("Reject");
            rejectButton.setMinimumSize(new Dimension(90, 20));
            rejectButton.setPreferredSize(new Dimension(90, 20));
            rejectButton.setMaximumSize(new Dimension(90, 20));
            rejectButton.addActionListener(e -> {
                setLoading(true);
                (new SwingWorker<SuccessResponse, Void>() {
                    @Override
                    protected void done() {
                        try {
                            if (get() != null) {
                                refreshAll();
                            }
                        } catch (InterruptedException | ExecutionException e1) {
                            logger.error("Error rejecting member", e1);
                            NotificationsUtil.showNotification("Clan", "Error rejecting member.");
                            setLoading(false);
                        }
                    }

                    String username = Session.get().getClient().getUsername();

                    @Override
                    protected SuccessResponse doInBackground() throws Exception {
                        if (!Session.get().isLoggedIn() ||
                                username == null ||
                                username.trim().length() == 0) {
                            NotificationsUtil.showNotification("Clan", "You need to be logged in to do that.");
                            setLoading(false);
                            return null;
                        }
                        rank.setRank(ClanRank.Rank.REJECTED);
                        return ClanService.updateMemberRank(new UpdateRankRequest(
                                clanPlugin.getCurrentClan().getClanId(),
                                username,
                                rank.getUserId(),
                                rank.getLoginNameToken(),
                                rank.getRank()
                        ));
                    }
                }).execute();
            });
            add(rejectButton, "growx, pushx, gapright 10, gapbottom 5");
        }
    }
}

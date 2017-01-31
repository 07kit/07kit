package com.kit.plugins.quickhop;

import com.kit.Application;
import com.kit.api.wrappers.World;
import com.kit.gui.component.MateScrollPane;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;
import com.kit.Application;
import com.kit.api.wrappers.World;
import com.kit.core.Session;
import com.kit.gui.component.MateTextField;
import com.kit.gui.component.SidebarWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class QuickHopSidebarWidget extends JPanel implements SidebarWidget {

    private static final Color F2P_COLOR = new Color(237, 237, 237);
    private static final Color P2P_COLOR = new Color(255, 229, 54);
    private static final int GAME_PORT = 43594;

    private final Image NORMAL_ICON = IconFontSwing.buildImage(FontAwesome.GLOBE, 20, Color.GRAY);
    private final Image TOGGLED_ICON = IconFontSwing.buildImage(FontAwesome.GLOBE, 20, Color.WHITE);
    private final Image F2P_ICON = IconFontSwing.buildImage(FontAwesome.STAR, 15, F2P_COLOR);
    private final Image P2P_ICON = IconFontSwing.buildImage(FontAwesome.STAR, 15, P2P_COLOR);

    private final List<WorldWidget> worlds = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final JPanel container;
    private final JTextField searchArea;
    private final Box spacing;

    private final QuickHopPlugin quickHopPlugin;

    public QuickHopSidebarWidget(QuickHopPlugin quickHopPlugin) {
        this.quickHopPlugin = quickHopPlugin;
        setLayout(new BorderLayout());

        spacing = Box.createVerticalBox();

        container = new JPanel();
        container.setLayout(new MigLayout("insets 0, gap rel 0"));
        container.setBackground(Application.COLOUR_SCHEME.getDark());

        searchArea = new MateTextField("Enter search query");
        searchArea.setMinimumSize(new Dimension(210, 35));
        searchArea.setBackground(Application.COLOUR_SCHEME.getDark());
        searchArea.setHorizontalAlignment(SwingConstants.CENTER);
        searchArea.setForeground(Application.COLOUR_SCHEME.getText());
        searchArea.setBorder(BorderFactory.createLineBorder(Application.COLOUR_SCHEME.getBright()));
        searchArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    filterListing();
                }
            }
        });
        container.add(searchArea, "span, growx, pushx");

        int idx = 0;
        for (World world : Session.get().worlds.getAll()) {
            WorldWidget widget = new WorldWidget(world, idx);
            container.add(widget, "push, growx, span");
            worlds.add(widget);
            idx++;
        }
        container.add(spacing, "span, grow, pushy, wrap push");

        MateScrollPane scrollPane = new MateScrollPane(container);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(75);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void filterListing() {
        String query = searchArea.getText().toLowerCase();
        for (WorldWidget worldWidget : worlds) {
            container.remove(worldWidget);
        }
        worlds.clear();
        container.revalidate();
        container.repaint();

        int idx = 0;
        for (World world : Session.get().worlds.getAll()) {
            if (query.length() == 0 || world.getActivity().toLowerCase().contains(query) || String.valueOf(world.getId() - 300).contains(query) || world.getCountry().toLowerCase().contains(query)) {
                WorldWidget widget = new WorldWidget(world, idx);
                container.add(widget, "push, growx, span");
                worlds.add(widget);
                idx++;
            }
        }

        container.remove(spacing);
        container.add(spacing, "span, grow, pushy, wrap push");

        container.revalidate();
        container.repaint();
    }

    public void update() {
        for (WorldWidget worldWidget : worlds) {
            long startTime = System.nanoTime();
            try (Socket socket = new Socket(worldWidget.world.getDomain(), GAME_PORT)) {
                byte[] payload = new byte[]{15, 0, 0, 0, 0};
                socket.getOutputStream().write(payload, 0, payload.length);
                socket.getInputStream().read();
                long endTime = System.nanoTime();
                int ping = (int) ((endTime - startTime) / 10e6);

                worldWidget.playersLabel.setText(worldWidget.world.getPlayers() + " players");
                worldWidget.pingLabel.setText(ping + " ms");
                worldWidget.repaint();
                quickHopPlugin.setPingingWorlds(false);
            } catch (IOException e) {
                logger.error("Error pinging worlds", e);
                quickHopPlugin.setPingingWorlds(false);
            }
        }
    }

    @Override
    public Component getContent() {
        return this;
    }

    @Override
    public String getTitle() {
        return "QuickHop";
    }

    @Override
    public Image getIcon(boolean toggled) {
        return toggled ? TOGGLED_ICON : NORMAL_ICON;
    }

    private class WorldWidget extends JPanel {
        private final JLabel playersLabel;
        private final JLabel pingLabel;
        public final World world;

        private WorldWidget(World world, int idx) {
            this.world = world;

            Image worldFlag = Application.FLAG_IMAGES.get(world.getCountry()).getScaledInstance(20, 10, Image.SCALE_SMOOTH);
            Image typeFlag = world.isMembers() ? P2P_ICON : F2P_ICON;

            setLayout(new MigLayout("insets 0, gap rel 0"));
            setBackground(idx % 2 == 0 ? Application.COLOUR_SCHEME.getBright() : Application.COLOUR_SCHEME.getDark());
            setMinimumSize(new Dimension(210, 75));

            //row 1
            JLabel typeLbl = new JLabel(new ImageIcon(typeFlag));
            typeLbl.setForeground(Application.COLOUR_SCHEME.getText());
            add(typeLbl, "gapleft 10, gaptop 10");

            JLabel worldLabel = new JLabel("World " + (world.getId() - 300));
            worldLabel.setFont(worldLabel.getFont().deriveFont(Font.BOLD, 13f));
            worldLabel.setForeground(Application.COLOUR_SCHEME.getText());
            add(worldLabel, "gapleft 10, gaptop 10");

            playersLabel = new JLabel(world.getPlayers() + " players");
            playersLabel.setFont(playersLabel.getFont().deriveFont(13f));
            playersLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            playersLabel.setForeground(Application.COLOUR_SCHEME.getText());
            add(playersLabel, "gaptop 10, pushx, span, wrap");

            //row2
            JLabel flagLbl = new JLabel(new ImageIcon(worldFlag));
            flagLbl.setForeground(new Color(151, 151, 151, 100));
            add(flagLbl, "gapleft 6, gaptop 8");

            JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
            separator.setForeground(Application.COLOUR_SCHEME.getText());
            add(separator, "gapleft 10, gaptop 0, gapright 10, pushx, growx, span, wrap");

            //row3
            pingLabel = new JLabel("-- ms");
            pingLabel.setFont(pingLabel.getFont().deriveFont(Font.ITALIC, 10.0f));
            pingLabel.setForeground(Application.COLOUR_SCHEME.getText());
            add(pingLabel, "gapleft 5, pad 10 0 10 0");

            JLabel activityLabel = new JLabel(ellipsis(world.getActivity(), 20));
            activityLabel.setFont(activityLabel.getFont().deriveFont(Font.ITALIC, 11f));
            activityLabel.setForeground(Application.COLOUR_SCHEME.getText());
            add(activityLabel, "gapleft 10, span");

            addMouseListener(new MouseAdapter() {
                private long lastClick = 0;

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (System.currentTimeMillis() - lastClick <= 400) {
                        Session.get().worlds.switchTo(world);
                    }
                    lastClick = System.currentTimeMillis();
                }
            });
        }

    }

    public static String ellipsis(final String text, int length) {
        // The letters [iIl1] are slim enough to only count as half a character.
        length += Math.ceil(text.replaceAll("[^iIl]", "").length() / 2.0d);

        if (text.length() > length) {
            return text.substring(0, length - 3) + "...";
        }
        return text;
    }
}

package com.kit.plugins.grandexchange;

import com.kit.gui.component.MateScrollPane;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;
import com.kit.Application;
import com.kit.api.util.NotificationsUtil;
import com.kit.api.util.PriceLookup;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.PriceInfo;
import com.kit.api.wrappers.World;
import com.kit.core.Session;
import com.kit.gui.component.LoadingContainer;
import com.kit.gui.component.MateTextField;
import com.kit.gui.component.SidebarWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 */
public class GrandExchangeSearchSidebarWidget extends JPanel implements SidebarWidget {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final Color F2P_COLOR = new Color(237, 237, 237);
    private static final Color P2P_COLOR = new Color(255, 229, 54);
    private static final int LIMIT = 10;

    private final Image NORMAL_ICON = IconFontSwing.buildImage(FontAwesome.SEARCH, 20, Color.GRAY);
    private final Image TOGGLED_ICON = IconFontSwing.buildImage(FontAwesome.SEARCH, 20, Color.WHITE);
    private final List<PriceInfoWidget> priceInfos = new ArrayList<>();

    private final JScrollPane content;
    private JPanel container;
    private final JTextField searchArea;
    private final Box spacing;
    private final JLabel noResultsFoundLbl;
    private JPanel loadingPanel;
    private boolean loading;

    public GrandExchangeSearchSidebarWidget() {
        setLayout(new BorderLayout());

        loadingPanel = new LoadingContainer()
        ;
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

        noResultsFoundLbl = new JLabel("No results found");
        noResultsFoundLbl.setForeground(Application.COLOUR_SCHEME.getText());

        content = new MateScrollPane(container);
        content.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        content.getVerticalScrollBar().setUnitIncrement(75);
        add(content, BorderLayout.CENTER);
    }

    public void setLoading(boolean loading) {
        if (loading && !this.loading) {
            remove(content);
            add(loadingPanel);
        } else if (!loading && this.loading) {
            remove(loadingPanel);
            add(content, BorderLayout.CENTER);
        }
        this.loading = loading;
        revalidate();
        repaint();
    }


    private void filterListing() {
        String query = searchArea.getText().toLowerCase();
        SwingUtilities.invokeLater(() -> {
        container.remove(noResultsFoundLbl);
            for (PriceInfoWidget priceInfoWidget : priceInfos) {
                container.remove(priceInfoWidget);
            }
            setLoading(true);
            priceInfos.clear();
        });

        (new SwingWorker<List<PriceInfoWidget>, Void>() {

            int idx = 0;

            @Override
            protected void done() {
                try {
                    List<PriceInfoWidget> widgets = get();

                    SwingUtilities.invokeLater(() -> {
                        if (widgets == null || widgets.size() == 0) {
                            container.add(noResultsFoundLbl, "span, growx, pushx, gaptop 20, gapleft 50");
                        } else {
                            for (PriceInfoWidget widget : widgets) {
                                container.add(widget, "push, growx, span");
                            }
                        }
                        container.remove(spacing);
                        container.add(spacing, "span, grow, pushy, wrap push");

                        setLoading(false);
                    });
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("Error searching prices", e);
                    NotificationsUtil.showNotification("Grand Exchange", "Error searching prices");
                    setLoading(false);
                }
            }

            @Override
            protected List<PriceInfoWidget> doInBackground() throws Exception {
                return PriceLookup.search(query.trim(), LIMIT).stream().map(p -> {
                    PriceInfoWidget widget = new PriceInfoWidget(p, idx);
                    idx++;
                    return widget;
                }).collect(Collectors.toList());
            }
        }).execute();


    }

    @Override
    public Component getContent() {
        return this;
    }

    @Override
    public String getTitle() {
        return "Price Search";
    }

    @Override
    public Image getIcon(boolean toggled) {
        return toggled ? TOGGLED_ICON : NORMAL_ICON;
    }

    private class PriceInfoWidget extends JPanel {
        public final PriceInfo priceInfo;

        private PriceInfoWidget(PriceInfo priceInfo, int idx) {
            this.priceInfo = priceInfo;
            setLayout(new MigLayout("gap rel 0, insets 0"));
            setBorder(BorderFactory.createLineBorder(Application.COLOUR_SCHEME.getDark().darker()));
            setBackground(idx % 2 == 0 ? Application.COLOUR_SCHEME.getBright() : Application.COLOUR_SCHEME.getDark());

            try {
                URL imgURL = new URL("http://services.runescape.com/m=itemdb_oldschool/1502970342600_obj_sprite.gif?id=" + priceInfo.getItemId());
                JLabel spriteLabel = new JLabel(new ImageIcon(ImageIO.read(imgURL).getScaledInstance(36, 32, BufferedImage.SCALE_SMOOTH))); // TODO: lol placeholder
                add(spriteLabel, "gapleft 10, gaptop 5, gapbottom 5");
            } catch (IOException e) {
                e.printStackTrace();
            }

            JPanel panel = new JPanel();
            panel.setBackground(getBackground());
            panel.setLayout(new MigLayout("gap rel 0, insets 0"));

            JLabel nameLabel = new JLabel(priceInfo.getName());
            nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));
            nameLabel.setForeground(Application.COLOUR_SCHEME.getText());

            panel.add(nameLabel, "gapleft 10, pushx, growx, wrap");

            JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
            separator.setForeground(Application.COLOUR_SCHEME.getText());
            panel.add(separator, "gapleft 10, gaptop 0, gapright 10, pushx, growx, span, wrap");

            JLabel buyLabel = new JLabel("Buy: " + Utilities.simpleFormat(priceInfo.getBuyAverage(), 0));
            buyLabel.setFont(buyLabel.getFont().deriveFont(Font.BOLD, 11f));
            buyLabel.setForeground(Application.COLOUR_SCHEME.getText());
            buyLabel.setMinimumSize(new Dimension(60, 15));
            panel.add(buyLabel, "gapleft 10, span");

            JLabel sellLabel = new JLabel("Sell: " + Utilities.simpleFormat(priceInfo.getSellAverage(), 0));
            sellLabel.setFont(sellLabel.getFont().deriveFont(Font.BOLD, 11f));
            sellLabel.setForeground(Application.COLOUR_SCHEME.getText());
            sellLabel.setMinimumSize(new Dimension(60, 15));
            panel.add(sellLabel, "gapleft 10, span");

            JLabel overallLabel = new JLabel("Overall: " + Utilities.simpleFormat(priceInfo.getAverage(), 0));
            overallLabel.setFont(overallLabel.getFont().deriveFont(Font.BOLD, 11f));
            overallLabel.setForeground(Application.COLOUR_SCHEME.getText());
            overallLabel.setMinimumSize(new Dimension(60, 15));
            panel.add(overallLabel, "gapleft 10, span");

            add(panel, "pushx, growx, span, gaptop 5, gapbottom 5");
        }

    }
}

package com.kit.plugins.grandexchange;

import com.kit.Application;
import net.miginfocom.swing.MigLayout;
import com.kit.Application;
import com.kit.api.service.CacheService;
import com.kit.api.util.ItemCompositesUtil;
import com.kit.api.wrappers.ItemComposite;
import com.kit.game.engine.IGrandExchangeOffer;
import com.kit.gui.component.MateProgressBar;
import com.kit.gui.icons.Icons;
import com.kit.Application;
import com.kit.api.util.ItemCompositesUtil;
import com.kit.api.wrappers.ItemComposite;
import com.kit.game.engine.IGrandExchangeOffer;
import com.kit.gui.component.MateProgressBar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static javax.swing.SwingConstants.CENTER;

/**
 */
public class GrandExchangeOfferWidget extends JPanel {

    private static final String ITEM_IMG_URL = "http://cdn.rsbuddy.com/items/";
    private final Component marginTop = Box.createVerticalStrut(10);
    private final MateProgressBar progressBar;
    private final IGrandExchangeOffer offer;
    private JLabel spriteLabel;
    private final JLabel nameLabel;
    private final JPanel offerInfo;
    private int progress;

    public GrandExchangeOfferWidget(IGrandExchangeOffer offer, int index) {
        this.offer = offer;
        setLayout(new MigLayout("gap rel 0, insets 0"));
        setBorder(BorderFactory.createLineBorder(Application.COLOUR_SCHEME.getDark().darker()));
        setBackground(index % 2 == 0 ? Application.COLOUR_SCHEME.getBright() : Application.COLOUR_SCHEME.getDark());

        try {
            URL imgURL = new URL(getImageUrl(offer.getItemId()));
            this.spriteLabel = new JLabel(new ImageIcon(ImageIO.read(imgURL).getScaledInstance(36, 32, BufferedImage.SCALE_SMOOTH))); // TODO: lol placeholder
            add(spriteLabel, "gapleft 10, gaptop 5, gapbottom 5");
        } catch (IOException e) {
            e.printStackTrace();
        }

        offerInfo = new JPanel();
        offerInfo.setBackground(getBackground());
        offerInfo.setLayout(new MigLayout("gap rel 0, insets 0"));

        ItemComposite composite = CacheService.get().getItemComposite(offer.getItemId());
        this.nameLabel = new JLabel(composite.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));
        nameLabel.setForeground(Application.COLOUR_SCHEME.getText());

        offerInfo.add(nameLabel, "gapleft 10, pushx, growx, wrap");

        this.progressBar = new MateProgressBar();
        progressBar.setPreferredSize(new Dimension(130, 20));
        progressBar.setBackgroundColour(Color.RED.darker().darker());
        progressBar.setForegroundColour(Color.GREEN.darker());
        progressBar.setBorderColour(Color.WHITE.darker());
        progressBar.setBorderWidth(1);
        progressBar.setMinimum(0);
        progressBar.setMaximum(offer.getQuantity());
        progressBar.setValue(0);
        progressBar.setShowText(true);

        offerInfo.add(progressBar, "gapleft 10, gaptop 3, pushx, growx, span, gapright 10");

        add(offerInfo, "pushx, growx, span, gaptop 5, gapbottom 5");

        setProgress(offer.getTransferred());
    }

    public static String getImageUrl(int id) {
        return ITEM_IMG_URL + id + ".png";
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        update();
    }

    private void update() {
        progressBar.setValue(progress);
    }

    public Component getMarginTop() {
        return marginTop;
    }

    public void setIndex(int i) {
        setBackground(i % 2 == 0 ? Application.COLOUR_SCHEME.getBright() : Application.COLOUR_SCHEME.getDark());
        offerInfo.setBackground(getBackground());
        repaint();
    }
}

package com.kit.plugins.socialstream;

import net.miginfocom.swing.MigLayout;
import com.kit.Application;
import com.kit.http.TwitchStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StreamWidget extends JPanel {

    private TwitchStream stream;
    public final Pattern BANNER_SIZE_PATTERN = Pattern.compile("(.*)-([0-9]+)x([0-9]+)(\\..*)");
    public final int bannerSizeX = 50;
    public final int bannerSizeY = 50;
    private JPanel innerPanel;

    public StreamWidget(TwitchStream stream, SocialStreamPlugin socialStreamPlugin) {
        try {
            this.stream = stream;
            innerPanel = new JPanel();
            innerPanel.setLayout(new MigLayout("insets 0, gap rel 0"));

            setLayout(new MigLayout("insets 0, gap rel 0"));
            setMinimumSize(new Dimension(210, 85));

            innerPanel.setMaximumSize(new Dimension(130, 80));

            String bannerUrl = stream.getBannerUrl();
            if (bannerUrl == null) {
                bannerUrl = stream.getLogoUrl();
            }
            if (bannerUrl == null) {
                bannerUrl = "http://s.jtvnw.net/jtv_user_pictures/hosted_images/GlitchIcon_purple.png";
            }
            Matcher m = BANNER_SIZE_PATTERN.matcher(bannerUrl);
            if (m.matches()) {
                bannerUrl = m.group(1) + "-" + bannerSizeX + "x" + bannerSizeY + m.group(4);
            }
            Image bannerImage;
            if (!socialStreamPlugin.getWidget().getImageCache().containsKey(bannerUrl)) {
                bannerImage = ImageIO.read(new URL(bannerUrl)).getScaledInstance(bannerSizeX, bannerSizeY, Image.SCALE_SMOOTH);
                socialStreamPlugin.getWidget().getImageCache().put(bannerUrl, bannerImage);
            } else {
                bannerImage = socialStreamPlugin.getWidget().getImageCache().get(bannerUrl);
            }

            //row 1
            JLabel bannerLbl = new JLabel(new ImageIcon(bannerImage));
            bannerLbl.setForeground(Application.COLOUR_SCHEME.getText());
            add(bannerLbl, "gapleft 4, gaptop 4, gapright 4, gapbottom 4");

            JLabel streamNameLbl = new JLabel(stream.getName());
            streamNameLbl.setMaximumSize(new Dimension(130, 20));
            streamNameLbl.setFont(streamNameLbl.getFont().deriveFont(Font.BOLD, 10f));
            streamNameLbl.setForeground(Application.COLOUR_SCHEME.getText());
            innerPanel.add(streamNameLbl, "gapleft 0, gaptop 0, wrap, pushx");

            JLabel statusLbl = new JLabel("<html>" + stream.getStatus() + "</html>");
            statusLbl.setMaximumSize(new Dimension(130, 60));
            statusLbl.setFont(statusLbl.getFont().deriveFont(8f));
            statusLbl.setForeground(Application.COLOUR_SCHEME.getText());
            innerPanel.add(statusLbl, "gapleft 0, gaptop 4, wrap, pushx");

            add(innerPanel);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URL(stream.getStreamUrl()).toURI());
                    } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JPanel getInnerPanel() {
        return innerPanel;
    }
}
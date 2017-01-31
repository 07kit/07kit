package com.kit.gui.view;

import com.kit.Application;
import com.kit.gui.component.MateScrollPane;
import com.kit.gui.controller.GalleryController;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 */
public class GalleryView extends JPanel {
    private final GalleryController galleryController;
    private final JPanel selectedImagePanel;
    private final JScrollPane scrollPane;
    private final JPanel sidePanel;

    private GalleryItemView activeItem;

    public GalleryView(GalleryController galleryController) {
        this.galleryController = galleryController;

        setPreferredSize(new Dimension(1015, 530));
        setBackground(Application.COLOUR_SCHEME.getDark());

        selectedImagePanel = new JPanel(new BorderLayout());
        selectedImagePanel.setPreferredSize(new Dimension(762, 503));
        selectedImagePanel.setBackground(Application.COLOUR_SCHEME.getDark());

        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(Application.COLOUR_SCHEME.getBright());

        scrollPane = new MateScrollPane(sidePanel);
        scrollPane.setPreferredSize(new Dimension(250, 503));
        scrollPane.setBackground(Application.COLOUR_SCHEME.getBright());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(56);


        setLayout(new BorderLayout());
        add(selectedImagePanel, BorderLayout.EAST);
        add(scrollPane, BorderLayout.WEST);
    }

    public void reload() {
        sidePanel.removeAll();
        for (GalleryController.Screenshot screenshot : galleryController.getScreenshots()) {
            try {
                Image image = ImageIO.read(screenshot.getFile());
                GalleryItemView view = new GalleryItemView(screenshot, image);
                view.setMinimumSize(new Dimension(227, 150));
                view.setMaximumSize(new Dimension(227, 150));
                view.setPreferredSize(new Dimension(227, 150));
                sidePanel.add(view);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void show(GalleryItemView item) {
        activeItem = item;
        selectedImagePanel.removeAll();
        selectedImagePanel.add(new JLabel(new ImageIcon(item.image)));
        selectedImagePanel.revalidate();
    }

    public class GalleryItemView extends JPanel {
        public final GalleryController.Screenshot screenshot;
        public final Image image;

        public GalleryItemView(GalleryController.Screenshot screenshot, Image image) {
            this.screenshot = screenshot;
            this.image = image;

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    GalleryView.this.show(GalleryItemView.this);
                }
            });
        }

        @Override
        public void paint(Graphics graphics) {
            graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null);

            graphics.setColor(new Color(33, 33, 33, 180));
            graphics.fillRect(0, getHeight() - 40, getWidth(), 40);

            int nameWidth = graphics.getFontMetrics().stringWidth(screenshot.getName());
            graphics.setColor(Color.WHITE);
            graphics.drawString(screenshot.getName(), getWidth() / 2 - (nameWidth / 2), getHeight() - 24);

            int characterNameWidth = graphics.getFontMetrics().stringWidth(screenshot.getCharacterName());
            graphics.setColor(Color.WHITE);
            graphics.drawString(screenshot.getCharacterName(), getWidth() / 2 - (characterNameWidth / 2), getHeight() - 7);
        }
    }

}

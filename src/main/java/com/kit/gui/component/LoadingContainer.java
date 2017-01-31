package com.kit.gui.component;

import com.kit.Application;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.painter.BusyPainter;
import com.kit.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class LoadingContainer extends JPanel {

    private JLabel loadingLabel;

    public LoadingContainer() {
        setBackground(Application.COLOUR_SCHEME.getDark().brighter());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        getInsets().set(0, 10, 10, 10);


        setLayout(new GridBagLayout());
        JXBusyLabel loadingSpinner = new JXBusyLabel(new Dimension(38, 38));
        BusyPainter painter = new BusyPainter(
                new Rectangle2D.Float(0, 0, 8.0f, 8.0f),
                new Rectangle2D.Float(5.5f, 5.5f, 27.0f, 27.0f));
        painter.setTrailLength(4);
        painter.setPoints(8);
        painter.setFrame(-1);
        painter.setBaseColor(Application.COLOUR_SCHEME.getLight());
        painter.setHighlightColor(Color.WHITE);
        loadingSpinner.setBusyPainter(painter);
        loadingSpinner.setVisible(true);
        loadingSpinner.setBusy(true);

        add(loadingSpinner);
        GridBagConstraints c = new GridBagConstraints();

        loadingLabel = new JLabel("Loading..");
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setFont(loadingLabel.getFont().deriveFont(16f).deriveFont(Font.BOLD));
        add(loadingLabel);

        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(6, 6, 6, 6);
        add(loadingLabel, c);
    }

    public JLabel getLoadingLabel() {
        return loadingLabel;
    }
}

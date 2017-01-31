package com.kit.gui.component;

import com.kit.Application;
import com.kit.Application;
import com.kit.Application;

import javax.swing.*;
import java.awt.*;

/**
 */
public class MateProgressBar extends JPanel {
    private static final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    private Color backgroundColour = Application.COLOUR_SCHEME.getDark();
    private Color foregroundColour = Application.COLOUR_SCHEME.getBright();
    private Color borderColour = Application.COLOUR_SCHEME.getHighlight();
    private int borderWidth = 2;
    private float minimum;
    private float maximum;
    private float value;
    private boolean showText;
    private boolean showPercentage;

    public MateProgressBar() {
        setBackground(backgroundColour);
        setForeground(foregroundColour);
        setBorder();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // Draw background
        g2d.setColor(backgroundColour);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        // Draw progress
        float percentage = value / maximum;
        int progressWidth = (int) (percentage * getWidth());
        g2d.setColor(foregroundColour);
        g2d.fillRect(0, 0, progressWidth, getHeight());
        // Draw text
        if (showText) {
            g2d.setColor(Application.COLOUR_SCHEME.getText());
            g2d.setFont(FONT.deriveFont((float) (0.6 * getHeight())));
            String text = String.format("%d / %d", (int) value, (int) maximum);
            if (showPercentage) {
                text = String.format("%d %%", (int) (value / maximum * 100F));
            }
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            g2d.drawString(text, (getWidth() / 2) - (textWidth / 2), getHeight() - (getHeight() / 3));
        }
        // Draw border
        getBorder().paintBorder(this, g2d, 0, 0, getWidth(), getHeight());
        g2d.dispose();
    }

    public float getMinimum() {
        return minimum;
    }

    public void setMinimum(float minimum) {
        this.minimum = minimum;
        repaint();
    }

    public float getMaximum() {
        return maximum;
    }

    public void setMaximum(float maximum) {
        this.maximum = maximum;
        repaint();
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
        repaint();
    }

    public Color getBackgroundColour() {
        return backgroundColour;
    }

    public void setBackgroundColour(Color backgroundColour) {
        this.backgroundColour = backgroundColour;
        setBackground(backgroundColour);
        repaint();
    }

    public Color getForegroundColour() {
        return foregroundColour;
    }

    public void setForegroundColour(Color foregroundColour) {
        this.foregroundColour = foregroundColour;
        setForeground(foregroundColour);
        repaint();
    }

    public Color getBorderColour() {
        return borderColour;
    }

    public void setBorderColour(Color borderColour) {
        this.borderColour = borderColour;
        setBorder();
        repaint();
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        setBorder();
        repaint();
    }

    private void setBorder() {
        setBorder(BorderFactory.createLineBorder(borderColour, borderWidth));
    }

    public boolean isShowText() {
        return showText;
    }

    public void setShowText(boolean showText) {
        this.showText = showText;
    }

    public boolean isShowPercentage() {
        return showPercentage;
    }

    public void setShowPercentage(boolean showPercentage) {
        this.showPercentage = showPercentage;
    }
}

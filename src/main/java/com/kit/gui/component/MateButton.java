package com.kit.gui.component;

import com.kit.Application;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class MateButton extends JButton {

	public MateButton() {
		init();
	}

	public MateButton(Icon icon) {
		super(icon);
		init();
	}

	public MateButton(String text) {
		super(text);
		init();
	}

	public MateButton(Action a) {
		super(a);
		init();
	}

	public MateButton(String text, Icon icon) {
		super(text, icon);
		init();
	}
	private void init() {
        setBorder(new MatteBorder(1, 1, 1, 1, Application.COLOUR_SCHEME.getLight()));
        setFocusPainted(false);
        
	}

	public void drawCenteredString(Graphics2D g, String text, Font font) {
		FontMetrics metrics = g.getFontMetrics(font);
		Color before = g.getColor();
		g.setColor(Application.COLOUR_SCHEME.getText());
		int x = (getWidth() - metrics.stringWidth(text)) / 2;
		int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
		g.setFont(font);
		g.drawString(text, x, y);
		g.setColor(before);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g.create();
		Color background;
		if (getModel().isPressed()) {
			background = Application.COLOUR_SCHEME.getClicked();
		} else if (getModel().isRollover()) {
			background = Application.COLOUR_SCHEME.getSelected();
		} else {
			background = Application.COLOUR_SCHEME.getHighlight();
		}
		g2.setColor(background);
		g2.fillRect(0, 0, getWidth(), getHeight());

		drawCenteredString(g2, getText(), g2.getFont().deriveFont(Font.BOLD));
		g2.dispose();
		//super.paintComponent(g);
	}
}

package com.kit.gui.component;

import com.kit.Application;
import com.kit.Application;

import java.awt.*;

public class MateSeperator extends Component {

	int width;
	int height;

	public MateSeperator(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Application.COLOUR_SCHEME.getText());
		g.drawLine(0, 0, width, height);
	}
}

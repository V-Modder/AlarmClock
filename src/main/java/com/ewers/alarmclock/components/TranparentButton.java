package com.ewers.alarmclock.components;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;

public class TranparentButton extends JButton {

	private static final long serialVersionUID = 1L;

	@Override
	public void paint(Graphics g) {
		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		// g.getFont()
		Rectangle2D lineSize = g.getFontMetrics().getStringBounds(this.getText(), 0, this.getText().length(), g);
		g.setColor(this.getForeground());
		g.drawString(this.getText(), (int) ((this.getWidth() - lineSize.getWidth()) / 2), (int) ((this.getHeight() - lineSize.getHeight() - lineSize.getY()) / 2));
	}
}

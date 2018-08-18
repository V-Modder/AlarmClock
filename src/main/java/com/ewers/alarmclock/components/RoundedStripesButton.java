package com.ewers.alarmclock.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;

public class RoundedStripesButton extends JButton {

	private static final long serialVersionUID = 1L;
	private Color stripeColor;
	private int stripeCount;
	private int stripeThikness;

	@Override
	protected void paintComponent(Graphics g) {
		if (stripeCount != 0 && stripeThikness != 0) {
			this.paintStripes(g);
		}
	}

	private void paintStripes(Graphics g) {
		int neededSpaces = (this.getHeight() - this.stripeThikness) / (this.stripeCount + 1);
		Color backup = g.getColor();
		g.setColor(this.stripeColor);
		for (int i = 0; i < this.stripeCount; i++) {
			Graphics2D graphics2d = (Graphics2D) g;
			RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(10, neededSpaces * (i + 1), this.getWidth() - 20, stripeThikness, 10, 10);
			graphics2d.draw(roundedRectangle);
			graphics2d.fill(roundedRectangle);
		}
		g.setColor(backup);
	}

	public Color getStripeColor() {
		return stripeColor;
	}

	public void setStripeColor(Color stripeColor) {
		this.stripeColor = stripeColor;
	}

	public int getStripeCount() {
		return stripeCount;
	}

	public void setStripeCount(int stripeCount) {
		this.stripeCount = stripeCount;
	}

	public int getStripeThikness() {
		return stripeThikness;
	}

	public void setStripeThikness(int stripeThikness) {
		this.stripeThikness = stripeThikness;
	}
}

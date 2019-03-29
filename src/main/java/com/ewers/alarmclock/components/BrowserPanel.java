package com.ewers.alarmclock.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BrowserPanel extends RoundedPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Brow

	public BrowserPanel() {
		this.setBounds(401, 50, 393, 400);
		this.setShady(false);
		this.setBackground(new Color(0, 0, 0, 125));
		this.setLayout(null);
		this.reinitComponents();
	}

	private void reinitComponents() {
		this.removeAll();
		this.revalidate();
		this.repaint();

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}

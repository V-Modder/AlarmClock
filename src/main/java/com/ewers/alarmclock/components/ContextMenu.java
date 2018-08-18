package com.ewers.alarmclock.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class ContextMenu extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String ACTION_COMMAND_ALARM = "alarm";
	private static final String ACTION_COMMAND_EXIT = "exit";

	public ContextMenu() {
		this.setBounds(650, 0, 150, 480);
		this.setMaximumSize(new Dimension(100, 480));
		this.setBackground(Color.DARK_GRAY);
		this.setVisible(false);
		this.setLayout(null);

		TranparentButton alarm = new TranparentButton();
		alarm.setText("Wecker");
		alarm.setBounds(0, 15, 150, 40);
		alarm.setBackground(new Color(0, 0, 0, 1));
		alarm.setForeground(Color.WHITE);
		alarm.setFont(new Font("Arial", Font.BOLD, 16));
		alarm.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		alarm.addActionListener(this);
		alarm.setActionCommand(ACTION_COMMAND_ALARM);
		this.add(alarm);

		TranparentButton exit = new TranparentButton();
		exit.setText("Beenden");
		exit.setBounds(0, 55, 150, 40);
		exit.setBackground(new Color(0, 0, 0, 1));
		exit.setForeground(Color.WHITE);
		exit.setFont(new Font("Arial", Font.BOLD, 16));
		exit.setBorder(BorderFactory.createEmptyBorder());
		exit.addActionListener(this);
		exit.setActionCommand(ACTION_COMMAND_EXIT);
		this.add(exit);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_EXIT)) {
			System.exit(0);
		} else if (e.getActionCommand().equals(ACTION_COMMAND_ALARM)) {
			this.setVisible(false);
			JLayeredPane layeredPane = (JLayeredPane) this.getParent();
			JPanel settingsPanel = (JPanel) this.getComponentByName(layeredPane.getComponents(), "settingsPanel");
			settingsPanel.setVisible(!settingsPanel.isVisible());
		}
	}

	private Component getComponentByName(Component[] components, String filter) {
		for (Component component : components) {
			if (filter.equals(component.getName()))
				return component;
		}

		return null;
	}
}

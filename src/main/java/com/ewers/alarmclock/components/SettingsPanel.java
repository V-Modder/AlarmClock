package com.ewers.alarmclock.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import com.ewers.alarmclock.config.Alarm;
import com.ewers.alarmclock.config.Configuration;
import com.ewers.alarmclock.config.Time;

import virtualkeyboard.gui.DialogVirtualKeyboardReal;

public class SettingsPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	public static final String ACTION_COMMAND_ADD = "action_command_add";
	public static final String ACTION_COMMAND_REMOVE = "action_command_remove";
	public static final String ACTION_COMMAND_EXIT = "action_command_exit";

	private MyTableModel model;
	private JTable table;

	public SettingsPanel() {
		this.setBounds(50, 50, 700, 380);
		this.setBackground(Color.DARK_GRAY);
		this.setVisible(false);
		this.setLayout(null);

		JButton addButton = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("green_cross_16.png")));
		addButton.setBounds(10, 20, 30, 30);
		addButton.setBackground(new Color(0, 0, 0, 1));
		addButton.addActionListener(this);
		addButton.setActionCommand(ACTION_COMMAND_ADD);
		this.add(addButton);

		JButton deleteButton = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("red_cross_16.png")));
		deleteButton.setBounds(10, 60, 30, 30);
		deleteButton.setBackground(new Color(0, 0, 0, 1));
		deleteButton.addActionListener(this);
		deleteButton.setActionCommand(ACTION_COMMAND_REMOVE);
		this.add(deleteButton);

		// img = ImageHelper.resize(img, 25, 25);
		JButton exitButton = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("disk_16.png")));
		exitButton.setBounds(10, 100, 30, 30);
		exitButton.setBackground(new Color(0, 0, 0, 1));
		exitButton.addActionListener(this);
		exitButton.setActionCommand(ACTION_COMMAND_EXIT);
		this.add(exitButton);

		this.model = new MyTableModel();

		this.table = new JTable(this.model);

		JTextField distanceText = new JTextField();
		distanceText.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				showVirtualKeyBoard(evt);
			}
		});
		DefaultCellEditor defaultCellEditor2 = new DefaultCellEditor(distanceText);
		defaultCellEditor2.setClickCountToStart(1);
		this.table.getColumnModel().getColumn(1).setCellEditor(defaultCellEditor2);
		JTextField field = new JTextField();
		field.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				showVirtualKeyBoard(evt);
			}
		});
		DefaultCellEditor defaultCellEditor = new DefaultCellEditor(field);
		defaultCellEditor.setClickCountToStart(1);
		this.table.getColumnModel().getColumn(0).setCellEditor(defaultCellEditor);

		this.table.setRowSelectionAllowed(true);
		this.table.setForeground(Color.black);
		this.table.setRowHeight(30);

		JScrollPane scrollPane = new JScrollPane(this.table);
		scrollPane.setBounds(50, 10, 630, 360);
		this.add(scrollPane);
		this.fillTable();
	}

	private void showVirtualKeyBoard(MouseEvent event) {
		DialogVirtualKeyboardReal dlg = new DialogVirtualKeyboardReal(null, false, (JTextComponent) event.getSource());
		dlg.setBackground(Color.DARK_GRAY);
		dlg.setLocaleL(Locale.getDefault());
	}

	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals(ACTION_COMMAND_EXIT)) {
			if (this.saveConfig()) {
				this.setVisible(false);
			}
		} else if (actionEvent.getActionCommand().equals(ACTION_COMMAND_ADD)) {
			Object[] row = new Object[9];
			row[0] = "";
			row[1] = new Time(0, 0);
			row[2] = new Boolean(false);
			row[3] = new Boolean(false);
			row[4] = new Boolean(false);
			row[5] = new Boolean(false);
			row[6] = new Boolean(false);
			row[7] = new Boolean(false);
			row[8] = new Boolean(false);
			this.model.addRow(row);
		} else if (actionEvent.getActionCommand().equals(ACTION_COMMAND_REMOVE)) {
			int selectedRow = this.table.getSelectedRow();
			if (selectedRow != -1) {
				this.model.removeRow(this.table.getSelectedRow());
			}
		}
	}

	private void fillTable() {
		List<Alarm> alarms = Configuration.getInstance().getAlarms();
		for (Alarm alarm : alarms) {
			Object[] row = new Object[9];
			row[0] = alarm.getName();
			row[1] = alarm.getTime();
			row[2] = alarm.onMonday();
			row[3] = alarm.onTuesday();
			row[4] = alarm.onWendsday();
			row[5] = alarm.onThursday();
			row[6] = alarm.onFriday();
			row[7] = alarm.onSaturday();
			row[8] = alarm.onSunday();
			this.model.addRow(row);
		}
	}

	private boolean saveConfig() {

		Configuration.getInstance().getAlarms().clear();
		try {
			for (int y = 0; y < this.model.getRowCount(); y++) {
				Alarm alarm = new Alarm();
				alarm.setName((String) this.model.getValueAt(y, 0));

				if (this.model.getValueAt(y, 1) instanceof Time) {
					alarm.setTime((Time) this.model.getValueAt(y, 1));
				} else {
					String s = (String) this.model.getValueAt(y, 1);
					if (s.isEmpty() || !s.contains(":")) {
						s = "00:00";
					}
					alarm.setTime(new Time(s));
				}
				alarm.setMonday((Boolean) this.model.getValueAt(y, 2));
				alarm.setTuesday((Boolean) this.model.getValueAt(y, 3));
				alarm.setWendsday((Boolean) this.model.getValueAt(y, 4));
				alarm.setThursday((Boolean) this.model.getValueAt(y, 5));
				alarm.setFriday((Boolean) this.model.getValueAt(y, 6));
				alarm.setSaturday((Boolean) this.model.getValueAt(y, 7));
				alarm.setSunday((Boolean) this.model.getValueAt(y, 8));
				Configuration.getInstance().getAlarms().add(alarm);
			}

			Configuration.getInstance().writeToFile();
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Could not write config to file", "Save error", JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}
}

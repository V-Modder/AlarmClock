package com.ewers.alarmclock.components;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class SwingCalendar extends JFrame {

	private static final long serialVersionUID = 1L;
	DefaultTableModel model;
	Calendar cal;
	JLabel label;

	SwingCalendar() {
		this.cal = new GregorianCalendar();
		this.cal.setFirstDayOfWeek(Calendar.MONDAY);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Swing Calandar");
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.setVisible(true);

		label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.CENTER);

		List<String> columns = new ArrayList<>();
		for (int i = 2; i <= 7; i++) {
			cal.set(Calendar.DAY_OF_WEEK, i);
			columns.add(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.GERMAN));
		}
		cal.set(Calendar.DAY_OF_WEEK, 1);
		columns.add(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.GERMAN));
		cal.setTime(new Date());
		model = new DefaultTableModel(null, columns.toArray(new String[0]));
		JTable table = new JTable(model);
		JScrollPane pane = new JScrollPane(table);

		this.add(panel, BorderLayout.NORTH);
		this.add(pane, BorderLayout.CENTER);

		this.updateMonth();

	}

	void updateMonth() {
		int currentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.GERMAN);
		int year = cal.get(Calendar.YEAR);
		label.setText(month + " " + year);

		int startDay = cal.get(Calendar.DAY_OF_WEEK);
		int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);

		model.setRowCount(0);
		model.setRowCount(weeks + 2);
		// int x = Calendar.MONDAY;
		int i = startDay + 5;
		for (int day = 1; day <= numberOfDays; day++) {
			model.setValueAt(day, i / 7, i % 7);
			i = i + 1;
		}

	}

	public static void main(String[] arguments) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		@SuppressWarnings("unused")
		SwingCalendar sc = new SwingCalendar();
	}

}
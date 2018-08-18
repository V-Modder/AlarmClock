package com.ewers.alarmclock.components;

import javax.swing.table.DefaultTableModel;

class MyTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public MyTableModel() {
		this.columnIdentifiers.add("Name");
		this.columnIdentifiers.add("Uhrzeit");
		this.columnIdentifiers.add("Montag");
		this.columnIdentifiers.add("Dienstag");
		this.columnIdentifiers.add("Mittwoch");
		this.columnIdentifiers.add("Donnerstag");
		this.columnIdentifiers.add("Freitag");
		this.columnIdentifiers.add("Samstag");
		this.columnIdentifiers.add("Sonntag");
	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for each
	 * cell. If we didn't implement this method, then the last column would contain
	 * text ("true"/"false"), rather than a check box.
	 */
	public Class<? extends Object> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
}
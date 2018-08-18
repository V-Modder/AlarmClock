package com.ewers.alarmclock.components;

import java.awt.Component;

import com.alee.extended.date.WebCalendar;

public class MyWebCalender extends WebCalendar {

	private static final long serialVersionUID = 1L;

	public MyWebCalender() {
		this.hideButton(this.next);
		this.hideButton(this.nextSkip);
		this.hideButton(this.previous);
		this.hideButton(this.previousSkip);
	}

	private void hideButton(Component comp) {
		comp.setVisible(false);
		comp.setEnabled(false);
	}
}

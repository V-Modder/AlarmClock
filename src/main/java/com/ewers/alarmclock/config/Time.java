package com.ewers.alarmclock.config;

public class Time {

	// @JsonProperty("hours")
	private int hours;
	// @JsonProperty("minutes")
	private int minutes;

	public Time() {
	}

	public Time(int hours, int minutes) {
		this.hours = hours;
		this.minutes = minutes;
	}

	public Time(String timeStr) {
		this.setTime(timeStr);
	}

	public void setTime(int hours, int minutes) {
		this.hours = hours;
		this.minutes = minutes;
	}

	public void setTime(String time) {
		if (time.isEmpty()) {
			this.hours = 0;
			this.minutes = 0;
			return;
		}
		String[] splitted = time.split(":");
		this.hours = Integer.parseInt(splitted[0]);
		this.minutes = Integer.parseInt(splitted[1]);
	}

	public int getHours() {
		return this.hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return this.minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public String toString() {
		return this.hours + ":" + this.minutes;
	}
}

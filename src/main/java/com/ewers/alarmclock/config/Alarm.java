package com.ewers.alarmclock.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Alarm {

	private String name;
	private Time time;
	@JsonProperty("monday")
	private boolean monday;
	@JsonProperty("tuesday")
	private boolean tuesday;
	@JsonProperty("wendsday")
	private boolean wendsday;
	@JsonProperty("thursday")
	private boolean thursday;
	@JsonProperty("friday")
	private boolean friday;
	@JsonProperty("saturday")
	private boolean saturday;
	@JsonProperty("sunday")
	private boolean sunday;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public boolean onMonday() {
		return monday;
	}

	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	public boolean onTuesday() {
		return tuesday;
	}

	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	public boolean onWendsday() {
		return wendsday;
	}

	public void setWendsday(boolean wendsday) {
		this.wendsday = wendsday;
	}

	public boolean onThursday() {
		return thursday;
	}

	public void setThursday(boolean thirsday) {
		this.thursday = thirsday;
	}

	public boolean onFriday() {
		return friday;
	}

	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	public boolean onSaturday() {
		return saturday;
	}

	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}

	public boolean onSunday() {
		return sunday;
	}

	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}

}

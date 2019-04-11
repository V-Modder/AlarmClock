package com.ewers.alarmclock.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Configuration {

	private static Configuration CONFIGURATION;
	private static final String CONFIG_FILE = "settings.json";
	private static String PATH;

	public static Configuration getInstance() {
		if (CONFIGURATION == null) {
			PATH = new File(System.getProperty("user.home"), ".Alarmclock").getAbsolutePath();
			CONFIGURATION = readFromFileOrNew();
		}
		return CONFIGURATION;
	}

	public static Configuration readFromFileOrNew() {
		ObjectMapper mapper = new ObjectMapper();
		Configuration config = null;
		try {
			File cfg = new File(PATH, CONFIG_FILE);
			if (cfg.exists()) {
				config = mapper.readValue(cfg, Configuration.class);
			} else {
				config = new Configuration();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return config;
	}

	private List<Alarm> alarms;

	public Configuration() {
		this.alarms = new ArrayList<Alarm>();
	}

	public void writeToFile() {
		File dir = new File(PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(PATH, CONFIG_FILE), this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Alarm> getAlarms() {
		return alarms;
	}

	public void setAlarms(List<Alarm> alarms) {
		this.alarms = alarms;
	}
}

package com.ewers.alarmclock.components;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import com.ewers.alarmclock.weatherunderground.CurrentObservation;
import com.ewers.alarmclock.weatherunderground.Forecastday_;
import com.ewers.alarmclock.weatherunderground.WeatherLoader;
import com.ewers.alarmclock.weatherunderground.WeatherUnderground;

public class WeatherPanel extends RoundedPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String WUNDERGROUND_URL = "http://api.wunderground.com/api/b9feb13d126dea6a/forecast/conditions/lang:DL/q/pws:IBORKEN26.json";
	private static final String WEATHER_TEMPLATE = "<html><div class=WordSection1 style=\"font-family:trebuchet ms,helvetica,sans-serif;\"><p style='font-size:<SIZE>.0pt'><TEMP_MAX> °C | <TEMP_MIN> °C</p><p style='font-size:<SIZE>.0pt'>&nbsp;&nbsp;&nbsp;&nbsp;<POP>%</p></div></html>";
	private static final String WEEKDAY_TEMPLATE = "<html><p style=\"font-family:trebuchet ms,helvetica,sans-serif;font-size:<SIZE>.0pt\"><WEEKDAY></p></html>";
	private static final String CONDITION_TEMPLATE = "<html><p style=\"font-family:trebuchet ms,helvetica,sans-serif;font-size:<SIZE>.0pt\"><CONDITION></p></html>";

	private Timer timer;

	public WeatherPanel() {
		this.setBounds(401, 50, 393, 400);
		this.setShady(false);
		this.setBackground(new Color(0, 0, 0, 125));
		this.setLayout(null);
		this.timer = new Timer(3600000, this);
		this.reinitComponents();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.timer) {
			this.reinitComponents();
		}
	}

	private void reinitComponents() {
		this.removeAll();
		this.revalidate();
		this.repaint();

		WeatherLoader weatherLoader = null;
		try {
			weatherLoader = new WeatherLoader(WUNDERGROUND_URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		weatherLoader.loadData();
		WeatherUnderground weatherUnderground = weatherLoader.getWeather();
		List<Forecastday_> forecastday_s = weatherUnderground.getForecast().getSimpleforecast().getForecastday();

		this.reinitToday(forecastday_s.get(0), weatherUnderground.getCurrentObservation());

		for (int i = 1; i < forecastday_s.size(); i++) {
			this.reinitDay(forecastday_s.get(i), new Point(10 + (((i - 1) * 140)), 240));
		}
	}

	private void reinitToday(Forecastday_ day, CurrentObservation current) {

		String text = WEEKDAY_TEMPLATE.replace("<SIZE>", "36");
		text = text.replace("<WEEKDAY>", "Heute");

		JLabel dayName = new JLabel();
		dayName.setBounds(20, 0, 200, 40);
		dayName.setBackground(new Color(0, 0, 0, 1));
		dayName.setForeground(Color.WHITE);
		dayName.setText(text);
		this.add(dayName);

		JLabel todayIcon = null;
		try {
			todayIcon = new JLabel(this.imageByName(day.getIcon() + ".gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		todayIcon.setBounds(170, 45, 50, 50);
		todayIcon.setBackground(new Color(0, 0, 0, 1));
		this.add(todayIcon);

		text = WEATHER_TEMPLATE.replace("<SIZE>", "30");
		text = text.replace("<TEMP_MAX>", current.getTemp_c().toString());
		text = text.replace("<TEMP_MIN>", day.getLow().getCelsius());
		text = text.replace("<POP>", day.getPop().toString());
		JLabel todayData = new JLabel();
		todayData.setBounds(120, 100, 250, 200);
		todayData.setBackground(new Color(0, 0, 0, 1));
		todayData.setForeground(Color.WHITE);
		todayData.setVerticalAlignment(SwingConstants.TOP);
		todayData.setHorizontalAlignment(SwingConstants.LEFT);
		todayData.setText(text);
		this.add(todayData);

		JLabel todayDrop = null;
		try {
			todayDrop = new JLabel(this.imageByName("pop_icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		todayDrop.setBounds(120, 150, 12, 12);
		this.add(todayDrop);

		text = CONDITION_TEMPLATE.replace("<SIZE>", "30");
		text = text.replace("<CONDITION>", day.getConditions());
		JLabel condition = new JLabel();
		condition.setBounds(120, 170, 200, 40);
		condition.setBackground(new Color(0, 0, 0, 1));
		condition.setForeground(Color.WHITE);
		condition.setText(text);
		this.add(condition);

	}

	private void reinitDay(Forecastday_ day, Point leftUpperCorner) {

		String text = WEEKDAY_TEMPLATE.replace("<SIZE>", "16");
		text = text.replace("<WEEKDAY>", day.getDate().getWeekday());

		JLabel dayName = new JLabel();
		dayName.setBounds(leftUpperCorner.x, leftUpperCorner.y, 200, 20);
		dayName.setBackground(new Color(0, 0, 0, 1));
		dayName.setForeground(Color.WHITE);
		dayName.setText(text);
		this.add(dayName);

		JLabel iconLabel = null;
		try {
			iconLabel = new JLabel(this.imageByName(day.getIcon() + ".gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		iconLabel.setBounds(leftUpperCorner.x + 20, leftUpperCorner.y + 30, 50, 50);
		iconLabel.setBackground(new Color(0, 0, 0, 1));
		this.add(iconLabel);

		text = WEATHER_TEMPLATE.replace("<SIZE>", "16");
		text = text.replace("<TEMP_MAX>", day.getHigh().getCelsius());
		text = text.replace("<TEMP_MIN>", day.getLow().getCelsius());
		text = text.replace("<POP>", day.getPop().toString());

		JLabel data = new JLabel();
		data.setBounds(leftUpperCorner.x, leftUpperCorner.y + 90, 200, 200);
		data.setBackground(new Color(0, 0, 0, 1));
		data.setForeground(Color.WHITE);
		data.setVerticalAlignment(SwingConstants.TOP);
		data.setHorizontalAlignment(SwingConstants.LEFT);
		data.setText(text);
		this.add(data);

		JLabel dropLabel = null;
		try {
			dropLabel = new JLabel(this.imageByName("pop_icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		dropLabel.setBounds(leftUpperCorner.x, leftUpperCorner.y + 114, 12, 12);
		this.add(dropLabel);

		text = CONDITION_TEMPLATE.replace("<SIZE>", "16");
		text = text.replace("<CONDITION>", day.getConditions());
		JLabel condition = new JLabel();
		condition.setBounds(leftUpperCorner.x, leftUpperCorner.y + 130, 200, 20);
		condition.setBackground(new Color(0, 0, 0, 1));
		condition.setForeground(Color.WHITE);
		condition.setText(text);
		this.add(condition);
	}

	private ImageIcon imageByName(String name) throws IOException {
		return new ImageIcon(this.getClass().getClassLoader().getResource("wunderground/" + name));
	}

}

package com.ewers.alarmclock.weatherunderground;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;

public class WeatherLoader {

	private URL link;
	private WeatherUnderground wu;

	public WeatherLoader(String url) throws MalformedURLException {
		this.link = new URL(url);
	}

	public void loadData() {
		String data = this.excutePost();
		Gson gson = new Gson();
		WeatherUnderground obj = gson.fromJson(data, WeatherUnderground.class);
		this.wu = obj;
	}

	private String excutePost() {
		HttpURLConnection connection = null;
		try {
			// Create connection
			connection = (HttpURLConnection) this.link.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Language", "de-DE");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if
															// not Java 5+
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public WeatherUnderground getWeather() {
		return this.wu;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		try {
			WeatherLoader w = new WeatherLoader("http://api.wunderground.com/api/8525b3ba614c185a/forecast/lang:DL/q/zmw:00000.15.10406.json");
			w.loadData();
			WeatherUnderground wu = w.getWeather();
			String sss = "f";
		} 
		catch (IOException e) {
		}
	}
}

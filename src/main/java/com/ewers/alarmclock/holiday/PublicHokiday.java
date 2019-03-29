package com.ewers.alarmclock.holiday;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class PublicHokiday {

	private static final String requestUrl = "https://ipty.de/feiertag/api.php?";

	public enum Do {
		isFeiertag, isFeiertagInfo
	};

	public enum DateConstants {
		today
	};

	public enum Location {
		BW, BY, BE, BB, HB, HH, HE, MV, NI, NW, RP, SL, SN, ST, SH, TH
	}

	public static boolean isPublicHolidayToday() {
		HashSet<Location> locations = new HashSet<Location>();
		locations.add(Location.NW);
		try {
			String answer = getWebRequestAnswer(buildURL(Do.isFeiertag, DateConstants.today, locations));
			return answer.equals("1");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return false;
	}

	private static URL buildURL(Do function, Object date, Set<Location> locations) throws MalformedURLException {
		String url = requestUrl;
		url += "do=" + function.toString();

		url += "&datum=";
		if (date instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
			url += sdf.format(date);
		} else {
			url += date;
		}

		if (locations != null) {
			url += "&loc=" + StringUtils.join(locations, ",");
		}

		return new URL(url);
	}

	private static String getWebRequestAnswer(URL url) {
		HttpURLConnection connection = null;
		try {

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Language", "de-DE");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
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
}

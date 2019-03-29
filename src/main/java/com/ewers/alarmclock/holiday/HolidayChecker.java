package com.ewers.alarmclock.holiday;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;

public class HolidayChecker {

	private ExchangeService exchangeService;

	public HolidayChecker(String emailAddress, String password) throws Exception {
		exchangeService = new ExchangeService();
		ExchangeCredentials credentials = new WebCredentials(emailAddress, password);
		exchangeService.setCredentials(credentials);
		exchangeService.autodiscoverUrl(emailAddress, new RedirectionUrlCallback());
	}

	public boolean isHolydayToday() throws Exception {
		return this.isHoliday(new Date());
	}

	public boolean isHoliday(Date date) throws Exception {

		boolean isHoliday = false;
		Date startDate = this.getStartDate(date);
		Date endDate = this.getEndDate(date);
		CalendarFolder cf = CalendarFolder.bind(exchangeService, WellKnownFolderName.Calendar);
		FindItemsResults<Appointment> findResults = cf.findAppointments(new CalendarView(startDate, endDate));
		for (Appointment appt : findResults.getItems()) {
			appt.load(PropertySet.FirstClassProperties);
			if (appt.getSubject().contains("Urlaub")) {
				isHoliday = true;
			}
		}

		isHoliday = isHoliday | PublicHokiday.isPublicHolidayToday();

		return isHoliday;
	}

	private Date getStartDate(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 1);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	private Date getEndDate(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	static class RedirectionUrlCallback implements IAutodiscoverRedirectionUrl {
		public boolean autodiscoverRedirectionUrlValidationCallback(String redirectionUrl) {
			return redirectionUrl.toLowerCase().startsWith("https://");
		}
	}

}

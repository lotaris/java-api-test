package com.lotaris.api.test.client.authentication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utils for sign API request during testing
 * 
 * @author Valentin Delaye <valentin.delaye@lotaris.com>
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public class AuthenticationHeaderUtils {
	private static final String UTC_TIMEZONE = "UTC";
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	/**
	 * Get the current UTC timestamp as string (ISO 8601)
	 */
	public static String getUtcTimestampAsString() {
		Calendar cal = Calendar.getInstance();
		DateFormat dfm = new SimpleDateFormat(DATE_FORMAT);
		dfm.setTimeZone(TimeZone.getTimeZone(UTC_TIMEZONE));
		return dfm.format(cal.getTime());
	}
	
	/**
	 * Get ISO 8601 timestamp for the given date
	 */
	public static String getUtcTimestampAsString(Date date) {
		DateFormat dfm = new SimpleDateFormat(DATE_FORMAT);
		dfm.setTimeZone(TimeZone.getTimeZone(UTC_TIMEZONE));
		return dfm.format(date);
	}
	
	/**
	 * Get the current UTC timestamp
	 */
	public static Date getUtcTimestamp() {
		return Calendar.getInstance(TimeZone.getTimeZone(UTC_TIMEZONE)).getTime();
	}
}

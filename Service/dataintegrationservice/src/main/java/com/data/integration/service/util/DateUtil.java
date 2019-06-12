package com.data.integration.service.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * DateUtil
 * 
 * @author Aniket
 *
 */
public class DateUtil {

	/**
	 * ISO Format
	 */
	final static String ISO_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * Time Zone UTC
	 */
	final static TimeZone UTC = TimeZone.getTimeZone("UTC");

	/**
	 * SimpleDateFormat
	 * 
	 */
	 

	/**
	 * return timestamp by converting given date to UTC format.
	 * 
	 * @param date
	 *            Date
	 * @return timestamp
	 */
	public static Timestamp getUTC(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				ISO_FORMAT);
		simpleDateFormat.setTimeZone(UTC);
		return Timestamp.valueOf(simpleDateFormat.format(date));
	}

	/**
	 * Return Current dateTime in UTC format
	 * 
	 * @return timestamp
	 */
	public static Timestamp now() {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				ISO_FORMAT);
		simpleDateFormat.setTimeZone(UTC);
		return Timestamp.valueOf(simpleDateFormat.format(date));
	}

}

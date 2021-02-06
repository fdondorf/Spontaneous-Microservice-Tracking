package org.spontaneous.service.tracking.general.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DateUtil {

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public static LocalDateTime parse(String dateString) {
		if (dateString == null) return null;
		return LocalDateTime.parse(dateString, formatter);
	};
	
	public static String parse(LocalDateTime localDateTime) {
		if (localDateTime == null) return null;
		return localDateTime.format(formatter);
	};
	
	public static String parse(Long timestamp) {
		return parse(getDateTimeFromTimestamp(timestamp));
	};
	
	public static Long parseToLong(String dateString) {
		return getDateTimeFromTimestamp(parse(dateString));
	};
	
	public static LocalDateTime getDateTimeFromTimestamp(Long timestamp) {
	    if (timestamp == null) {
	      return null;
	    }
	    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone
	        .getDefault().toZoneId());
	}
	
	public static Long getDateTimeFromTimestamp(LocalDateTime dateTime) {
	    if (dateTime == null) {
	      return null;
	    }
	    ZonedDateTime zdt = dateTime.atZone(TimeZone
		        .getDefault().toZoneId());
	    
	    return zdt.toInstant().toEpochMilli();
	}
}

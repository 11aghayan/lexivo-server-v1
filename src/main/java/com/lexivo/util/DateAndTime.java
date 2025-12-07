package com.lexivo.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public abstract class DateAndTime {
	public static long getSecondsInMinutes(long minutes) {
		return minutes * 60;
	}

	public static long getMinutesInDays(int days) {
		return (long) days * 24 * 60;
	}

	public static String getFormattedDateAndTimeFromMs(long milliseconds) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
				.withZone(ZoneId.of("UTC"));
		return formatter.format(Instant.ofEpochMilli(milliseconds));
	}
}

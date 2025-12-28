package com.lexivo.schema;

import com.lexivo.exceptions.InvalidLogCategoryException;
import com.lexivo.util.DateAndTime;

public record Log(long createdAt, String userEmail, Category category,
				  String[] stackTrace, String[] messages) {


	@Override
	public String toString() {
		return "Log{" +
				"createdAt=" + DateAndTime.getFormattedDateAndTimeFromMs(createdAt) +
				", category=" + category +
				", userEmail='" + userEmail + '\'' +
				'}';
	}

	public enum Category {
		INFO,
		WARNING,
		EXCEPTION;

		public static Category fromString(String s) throws InvalidLogCategoryException {
			return switch (s) {
				case "INFO" -> INFO;
				case "WARNING" -> WARNING;
				case "EXCEPTION" -> EXCEPTION;
				default -> throw new InvalidLogCategoryException(s);
			};
		}

		public String getColor() {
			return switch (this) {
				case INFO -> Color.INFO;
				case WARNING -> Color.WARNING;
				case EXCEPTION -> Color.EXCEPTION;
			};
		}
	}

	public static class Color {
		public static final String INFO = "\u001B[34m";
		public static final String EXCEPTION = "\u001B[31m";
		public static final String WARNING = "\u001B[33m";
		public static final String DEFAULT = "\u001B[39m";
	}
}

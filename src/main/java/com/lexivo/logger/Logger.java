package com.lexivo.logger;

import com.lexivo.db.Db;
import com.lexivo.schema.Log;
import com.lexivo.util.DateAndTime;
import com.lexivo.util.Email;

import java.util.Arrays;

public class Logger {
	private Log createLog(Log.Category category, String userEmail, Throwable t, String... messages) {
		long now = System.currentTimeMillis();
		String[] stackTrace = t == null ? null : Arrays.stream(t.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new);
		Log log = new Log(now, userEmail, category, stackTrace, messages);
		Db.logs().addLog(log);
		return log;
	}

	private void logContent(Log log) {
		System.out.print(" " + DateAndTime.getFormattedDateAndTimeFromMs(log.createdAt()));
		for (var message : log.messages()) {
			System.out.print(" | " + message);
		}
		System.out.println();
	}

	private void logStackTrace(String[] stackTrace) {
		if (stackTrace != null) {
			System.out.println("[Throwable.stackTrace]:");
			for (var elm : stackTrace) {
				System.out.println(" - " + elm);
			}
		}
	}

	public void log(Throwable t, Log.Category category, String userEmail, String[] messages) {
		Log log = createLog(category, userEmail, t, messages);
		if (category == Log.Category.EXCEPTION) {
			Email.sendEmailToAdmin("Lexivo server exception", "Exception thrown at " + DateAndTime.getFormattedDateAndTimeFromMs(log.createdAt()) + ".\nUser related: " + (userEmail != null));
		}
		String delimiter = "-".repeat(Math.max(0, category.toString().length() + 4));
		System.out.print(category.getColor());
		System.out.println(delimiter);
		System.out.print("| " + category + " |");
		logContent(log);
		System.out.println(delimiter);
		logStackTrace(log.stackTrace());
		resetColor();
	}

	public void info(String userEmail, String[] messages) {
		log(null, Log.Category.INFO, userEmail, messages);
	}

	public void info(String... messages) {
		info(null, messages);
	}

	public void warning(String userEmail, String[] messages) {
		log(null, Log.Category.WARNING, userEmail, messages);
	}

	public void warning(String... messages) {
		warning(null, messages);
	}

	public void exception(Throwable t, String userEmail, String[] messages) {
		log(t, Log.Category.EXCEPTION, userEmail, messages);
	}

	public void exception(String userEmail, String[] messages) {
		exception(null, userEmail, messages);
	}

	public void exception(Throwable t, String[] messages) {
		exception(t, null, messages);
	}

	public void exception(String... messages) {
		exception(null, null, messages);
	}

	private void resetColor() {
		System.out.print(Log.Color.DEFAULT);
	}
}

package com.lexivo.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public abstract class PasswordUtil {
	public static final String[] PASSWORD_REQUIREMENTS = {"Password must have", "8-32 characters", "at least one upper case letter", "at least on lower case letter", "at least one number"};
	private static String getRequestBodyString(HttpExchange exchange) throws IOException {
		InputStream requestBodyStream = exchange.getRequestBody();
		return new String(requestBodyStream.readAllBytes(), StandardCharsets.UTF_8);
	}

	public static boolean doesPasswordMeetRequirements(String password) {
		return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,32}$");
	}
}

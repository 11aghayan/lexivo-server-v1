package com.lexivo.util;

import java.security.SecureRandom;

public abstract class Randomizer {
	private static final char[] chars = {
			'a','b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x','y', 'z',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D','E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	};
	public static String getEmailConfirmationCode() {
		SecureRandom random = new SecureRandom();
		int origin = 1_000_000;
		return String.valueOf(random.nextInt(origin, origin * 10));
	}

	public static String generateUserPassword() {
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				password.append(chars[random.nextInt(0, chars.length)]);
			}
			if (i < 2) {
				password.append('-');
			}
		}
		return password.toString();
	}
}

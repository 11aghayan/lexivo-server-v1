package com.lexivo.util;

import com.lexivo.schema.User;
import org.mindrot.jbcrypt.BCrypt;

public abstract class AuthUtil {
	public static boolean isUserPasswordCorrect(User user, String password) {
		if (user == null) return false;

		boolean passwordCorrect = false;

		try {
			passwordCorrect = BCrypt.checkpw(password, user.getPasswordHash());
		} catch (RuntimeException ignore) {}

		return passwordCorrect;
	}
}

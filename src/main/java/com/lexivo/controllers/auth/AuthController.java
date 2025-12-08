package com.lexivo.controllers.auth;

import com.lexivo.controllers.Controller;

public abstract class AuthController {
	public static Controller login(String path) {
		return new LoginController(path);
	}

	public static Controller signup(String path) {
		return new SignupController(path);
	}
}

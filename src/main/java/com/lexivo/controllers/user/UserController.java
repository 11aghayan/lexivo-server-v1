package com.lexivo.controllers.user;

import com.lexivo.controllers.Controller;

public abstract class UserController extends Controller {
	public UserController(String routeBasePath) {
		super(routeBasePath);
	}

	public static Controller confirmEmail(String path) {
		return new ConfirmEmailController(path);
	}

	public static Controller recoverPassword(String path) {
		return new RecoverPasswordController(path);
	}
}

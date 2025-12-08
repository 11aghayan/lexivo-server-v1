package com.lexivo.routes;

import com.lexivo.controllers.user.UserController;
import com.lexivo.enums.UserRole;
import com.lexivo.filters.AuthVerifier;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpServer;

public class UserRoute implements RouteWithServer {
	private final String confirmEmailPath;
	private final String recoverPasswordPath;

	public UserRoute(String basePath) {
		basePath = basePath + "/user";
		this.confirmEmailPath = basePath + "/confirm_email";
		this.recoverPasswordPath = basePath + "/recover_password";
	}

	@Override
	public void withServer(HttpServer server) {
		new Route(server, confirmEmailPath, UserController.confirmEmail(confirmEmailPath).getClass());
		new Route(server, recoverPasswordPath, UserController.recoverPassword(recoverPasswordPath).getClass(), new Filter[]{new AuthVerifier(UserRole.USER)});
	}
}

package com.lexivo.routes;

import com.lexivo.controllers.auth.AuthController;
import com.sun.net.httpserver.HttpServer;

public class AuthRoute implements RouteWithServer {
	private final String loginPath;
	private final String signupPath;

	public AuthRoute(String basePath) {
		basePath = basePath + "/auth";
		this.loginPath = basePath + "/login";
		this.signupPath = basePath + "/signup";
	}

	@Override
	public void withServer(HttpServer server) {
		new Route(server, loginPath, AuthController.login(loginPath).getClass());
		new Route(server, signupPath, AuthController.login(signupPath).getClass());
	}
}
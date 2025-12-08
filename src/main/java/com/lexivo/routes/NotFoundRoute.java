package com.lexivo.routes;

import com.lexivo.controllers.NotFoundController;
import com.sun.net.httpserver.HttpServer;

public class NotFoundRoute implements RouteWithServer{
	@Override
	public void withServer(HttpServer server) {
		new Route(server, "/", NotFoundController.class);
	}
}

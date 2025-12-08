package com.lexivo.routes;

import com.sun.net.httpserver.HttpServer;

public interface RouteWithServer {
	void withServer(HttpServer server);
}

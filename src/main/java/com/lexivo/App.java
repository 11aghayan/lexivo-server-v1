package com.lexivo;

import com.lexivo.controllers.AuthController;
import com.lexivo.controllers.NotFoundController;
import com.lexivo.route.Route;
import com.lexivo.schema.Log;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

public class App {
    public static final String BASE_URL = "/api/v1";
    public static void main(String[] args) {
        try {
            int PORT;
            String envPort = System.getenv("PORT");
            PORT = envPort == null ? 8001 : Integer.parseInt(envPort);

            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            initRoutes(server);

            server.setExecutor(null);

            server.start();

            Log.info(List.of("Server running on port " + PORT));
        }
        catch (Exception e) {
            Log.exception(List.of("Exception in App", Arrays.stream(e.getStackTrace()).toList().toString(), e.getMessage()));
        }
	}

    private static void initRoutes(HttpServer server) {
        new Route(server, BASE_URL + "/auth", AuthController.class);
        new Route(server, "/", NotFoundController.class);
    }
}

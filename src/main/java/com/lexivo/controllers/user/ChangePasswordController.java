package com.lexivo.controllers.user;

import com.lexivo.controllers.Controller;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.SQLException;

public class ChangePasswordController extends Controller {
	public ChangePasswordController(String path) {
		super(path);
	}

	@Override
	protected void post(HttpExchange exchange) throws IOException, SQLException {
		super.post(exchange);
	}
}

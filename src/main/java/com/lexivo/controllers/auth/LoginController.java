package com.lexivo.controllers.auth;

import com.lexivo.controllers.Controller;
import com.lexivo.db.Db;
import com.lexivo.schema.User;
import com.lexivo.util.HttpResponseStatus;
import com.lexivo.util.JsonUtil;
import com.lexivo.util.JwtUtil;
import com.lexivo.util.RequestDataCheck;
import com.sun.net.httpserver.HttpExchange;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginController extends Controller {
	public LoginController(String routeBasePath) {
		super(routeBasePath);
	}

	@Override
	protected void post(HttpExchange exchange) throws IOException {
		User requestBody = RequestDataCheck.getCheckedRequestBody(exchange, List.of("email", "password"), User.class);

		if (requestBody == null) return;

		String email = requestBody.getEmail();
		email = email == null ? null : email.trim();
		String password = requestBody.getPassword();

		User user = Db.users().getByEmail(email);
		if (user == null) {
			sendBadRequestResponse(exchange, "Incorrect credentials");
			return;
		}

		boolean passwordCorrect = false;
		try {
			passwordCorrect = BCrypt.checkpw(password, user.getPasswordHash());
		} catch (RuntimeException ignore) {}

		if (!passwordCorrect) {
			sendBadRequestResponse(exchange, "Incorrect credentials");
			return;
		}

		if (!user.isConfirmed()) {
			sendConfirmationCodeEmailAndResponse(exchange, user, HttpResponseStatus.FORBIDDEN);
			return;
		}

		Map<String, String> jsonMap = new HashMap<>();
		jsonMap.put(JwtUtil.KEY_ACCESS_TOKEN, JwtUtil.createAccessToken(email));
		jsonMap.put(JwtUtil.KEY_REFRESH_TOKEN, JwtUtil.createRefreshToken(email));

		sendJsonResponse(exchange, HttpResponseStatus.OK, JsonUtil.toJson(jsonMap));
	}
}

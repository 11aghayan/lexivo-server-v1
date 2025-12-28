package com.lexivo.handlers.auth;

import com.lexivo.db.Db;
import com.lexivo.enums.UserRole;
import com.lexivo.logger.Logger;
import com.lexivo.schema.User;
import com.lexivo.util.*;
import org.jandle.api.annotations.HttpRequestHandler;
import org.jandle.api.http.Handler;
import org.jandle.api.http.Request;
import org.jandle.api.http.RequestMethod;
import org.jandle.api.http.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@HttpRequestHandler(method = RequestMethod.POST, path = "/auth/login")
public class UserLoginHandler implements Handler {
	private final Logger logger = new Logger();

	@Override
	public void handle(Request request, Response response) throws IOException {
		var requestBody = request.getBodyJson();
		String email = (String) requestBody.get("email");
		email = email == null ? null : email.trim();
		String password = (String) requestBody.get("password");
		try {
			if (!credentialsProvided(email, password, response)) return;

			User user = Db.users().getByEmail(email);

			if (!AuthUtil.isUserPasswordCorrect(user, password)) {
				StandardResponse.incorrectCredentials(response);
				return;
			}

			if (!user.isConfirmed()) {
				StandardResponse.sendConfirmationCode(response, user);
				return;
			}

			Map<String, String> userData = new HashMap<>();
			userData.put(JwtUtil.KEY_ACCESS_TOKEN, JwtUtil.createHMAC256Token(email, UserRole.USER, 5));
			userData.put(JwtUtil.KEY_REFRESH_TOKEN, JwtUtil.createHMAC256Token(email, UserRole.USER, DateAndTime.getMinutesInDays(30)));
			userData.put("email", user.getEmail());
			userData.put("name", user.getName());

			response
					.status(HttpResponseStatus.OK)
					.sendJson(userData);
		}
		catch (IOException e) {
			logger.exception(e, email, new String[]{e.getMessage()});
			response.sendStatus(HttpResponseStatus.SERVER_SIDE_ERROR);
		}
	}

	private boolean credentialsProvided(String email, String password, Response response) throws IOException {
		if (email == null || email.isEmpty()) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "Email is not provided");
			return false;
		}

		if (password == null || password.isBlank()) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "Password is not provided");
			return false;
		}

		return true;
	}
}

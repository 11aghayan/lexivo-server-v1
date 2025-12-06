package com.lexivo.controllers;

import com.lexivo.db.Db;
import com.lexivo.schema.User;
import com.lexivo.util.*;
import com.sun.net.httpserver.HttpExchange;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthController extends Controller {
	private static final Map<String, List<Long>> emailConfirmationCodeMap = new HashMap<>();
	private final int emailConfirmationCodeValidDurationMs = 10 * 60 * 1000;
	private final String messageConfirmEmail = "Confirm your email within 10 minutes";

	public AuthController(String routeBasePath) {
		super(routeBasePath);
	}

	@Override
	protected void post(HttpExchange exchange) throws IOException, SQLException {
		String path = exchange.getRequestURI().getPath();
		if (pathsEqual(routeBasePath + "/login", path)) {
			login(exchange);
			return;
		}

		if (pathsEqual(routeBasePath + "/signup", path)){
			signup(exchange);
			return;
		}

		if (pathsEqual(routeBasePath + "/confirm_email", path)){
			confirmEmail(exchange);
			return;
		}

		sendRouteDoesNotExistResponse(exchange);
	}

	private void login(HttpExchange exchange) throws IOException, SQLException {
		User requestBody = RequestDataCheck.getCheckedRequestBody(exchange, List.of("email", "password"), User.class);

		if (requestBody == null) return;

		String email = requestBody.getEmail();
		String password = requestBody.getPassword();

		User user = Db.user().getByEmail(email);
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
			emailConfirmationCodeMap.put(email, Randomizer.getEmailConfirmationNumberAndDateList());
			boolean sent = Email.sendConfirmationCode(email, emailConfirmationCodeMap.get(email).getFirst().toString());

			if (!sent) {
				sendServerSideErrorResponse(exchange);
				return;
			}

			sendOkWithMessage(exchange, messageConfirmEmail);
			return;
		}

		Map<String, String> jsonMap = new HashMap<>();
		jsonMap.put(JwtUtil.KEY_ACCESS_TOKEN, JwtUtil.createAccessToken(email));
		jsonMap.put(JwtUtil.KEY_REFRESH_TOKEN, JwtUtil.createRefreshToken(email));

		sendJsonResponse(exchange, HttpResponseStatus.OK, JsonUtil.toJson(jsonMap));
	}

	private void signup(HttpExchange exchange) throws IOException, SQLException {
		User requestBody = RequestDataCheck.getCheckedRequestBody(exchange, List.of("name", "email", "password"), User.class);
		if (requestBody == null) return;

		String name = requestBody.getName();
		String email = requestBody.getEmail();
		String password = requestBody.getPassword();

		User user = Db.user().getByEmail(email);
		if (user != null) {
			sendBadRequestResponse(exchange, "User with the given email already exists");
			return;
		}

		boolean passwordMeetsRequirements = RequestDataCheck.doesPasswordMeetRequirements(password);
		if (!passwordMeetsRequirements) {
			sendBadRequestResponse(exchange, "Password must have", "8-32 characters", "at least one upper case letter", "at least on lower case letter", "at least one number");
			return;
		}

		String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

		boolean success = Db.user().addUser(new User(email, name, passwordHash, false));

		if (!success) {
			sendServerSideErrorResponse(exchange);
			return;
		}


		emailConfirmationCodeMap.put(email, Randomizer.getEmailConfirmationNumberAndDateList());

		boolean sent = Email.sendConfirmationCode(email, emailConfirmationCodeMap.get(email).getFirst().toString());

		if (!sent) {
			sendServerSideErrorResponse(exchange);
			return;
		}

		sendOkWithMessage(exchange, messageConfirmEmail);
	}

	private void confirmEmail(HttpExchange exchange) throws IOException, SQLException {
		removeRedundantEmailConfirmationCodeMapEntries();

		@SuppressWarnings("unchecked")
		Map<String, ?> requestBody = RequestDataCheck.getCheckedRequestBody(exchange, List.of("email", "confirmation_code"), Map.class);

		if (requestBody == null) return;

		String email = (String) requestBody.get("email");
		double confirmationCode = (double) requestBody.get("confirmation_code");

		List<Long> emailConfirmationCodeAndDate = emailConfirmationCodeMap.get(email);
		long now = Instant.now().toEpochMilli();

		if (emailConfirmationCodeAndDate == null || emailConfirmationCodeAndDate.getFirst() != confirmationCode || now - emailConfirmationCodeAndDate.getLast() > emailConfirmationCodeValidDurationMs) {
			sendBadRequestResponse(exchange, "Wrong confirmation code");
			return;
		}

		boolean success = Db.user().confirmUser(email);
		if(!success) {
			sendServerSideErrorResponse(exchange);
			return;
		}

		emailConfirmationCodeMap.remove(email);

		sendJsonResponse(exchange, HttpResponseStatus.OK, JsonUtil.toJson(Map.of("message", "User email successfully confirmed")));
	}

	private void recoverPassword() throws IOException, SQLException {
//		TODO:
	}

	private void removeRedundantEmailConfirmationCodeMapEntries() {
		if (emailConfirmationCodeMap.size() < 10) return;

		long now = Instant.now().toEpochMilli();

		for (var key : emailConfirmationCodeMap.keySet()) {
			if (now - emailConfirmationCodeMap.get(key).getLast() > emailConfirmationCodeValidDurationMs) {
				emailConfirmationCodeMap.remove(key);
			}
		}
	}
}

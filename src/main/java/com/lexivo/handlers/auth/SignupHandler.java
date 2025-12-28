package com.lexivo.handlers.auth;

import com.lexivo.db.Db;
import com.lexivo.logger.Logger;
import com.lexivo.schema.User;
import com.lexivo.util.*;
import org.jandle.api.annotations.HttpRequestHandler;
import org.jandle.api.http.Handler;
import org.jandle.api.http.Request;
import org.jandle.api.http.RequestMethod;
import org.jandle.api.http.Response;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;

@HttpRequestHandler(method = RequestMethod.POST, path = "/auth/signup")
public class SignupHandler implements Handler {
	private final Logger logger = new Logger();

	@Override
	public void handle(Request request, Response response) throws IOException {
		var requestBody = request.getBodyJson();

		String name = (String) requestBody.get("name");
		String email = (String) requestBody.get("email");
		String password = (String) requestBody.get("password");

		try {
			if (!credentialsProvided(response, name, email, password)) return;

			User user = Db.users().getByEmail(email);
			if (user != null) {
				StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "User with the given email already exists");
				return;
			}

			boolean passwordMeetsRequirements = PasswordUtil.doesPasswordMeetRequirements(password);
			if (!passwordMeetsRequirements) {
				StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, PasswordUtil.PASSWORD_REQUIREMENTS);
				return;
			}

			String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
			user = new User(email, name, passwordHash, false);
			boolean userAdded = Db.users().addUser(user);

			if (!userAdded) {
				response.sendStatus(HttpResponseStatus.SERVER_SIDE_ERROR);
				return;
			}

			logger.info(user.getEmail(), new String[]{});

			StandardResponse.sendConfirmationCode(response, user);
		}
		catch (IOException | SQLException e) {
			logger.exception(e, email, new String[]{e.getMessage()});
			response.sendStatus(HttpResponseStatus.SERVER_SIDE_ERROR);
		}
	}

	private boolean credentialsProvided(Response response, String name, String email, String password) throws IOException {
		if (email == null || email.isBlank()) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "Email is missing");
			return false;
		}

		if (name == null || name.isBlank()) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "Name is missing");
			return false;
		}

		if (password == null || password.isBlank()) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "Password is missing");
			return false;
		}

		return true;
	}
}

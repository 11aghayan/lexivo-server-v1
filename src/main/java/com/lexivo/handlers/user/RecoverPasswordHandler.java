package com.lexivo.handlers.user;

import com.lexivo.db.Db;
import com.lexivo.logger.Logger;
import com.lexivo.schema.Log;
import com.lexivo.util.*;
import org.jandle.api.annotations.HttpRequestHandler;
import org.jandle.api.http.Handler;
import org.jandle.api.http.Request;
import org.jandle.api.http.RequestMethod;
import org.jandle.api.http.Response;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;

@HttpRequestHandler(method = RequestMethod.POST, path = "/user/recover_password")
public class RecoverPasswordHandler implements Handler {
	private final Logger logger = new Logger();

	@Override
	public void handle(Request request, Response response) throws IOException {
		var requestBody = request.getBodyJson();
		String email = (String) requestBody.get("email");

		try {
			if (email == null || email.isBlank()) {
				StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "Email is missing");
			}
			String newPassword = Randomizer.generateUserPassword();
			String newPasswordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
			Db.users().updateUserPassword(email, newPasswordHash);

			Email.sendRecoveredPassword(email, newPassword);

			StandardResponse.jsonWithMessages(response, HttpResponseStatus.OK, "Check your email");
		}
		catch (IOException | SQLException e) {
			logger.exception(e, email, new String[]{e.getMessage()});
			response.sendStatus(HttpResponseStatus.SERVER_SIDE_ERROR);
		}
	}
}

package com.lexivo.util;

import com.lexivo.db.Db;
import com.lexivo.logger.Logger;
import com.lexivo.schema.EmailConfirmationCodeData;
import com.lexivo.schema.Log;
import com.lexivo.schema.User;
import org.jandle.api.http.Response;

import java.io.IOException;
import java.util.Map;

public abstract class StandardResponse {
	private static final Logger logger = new Logger();
	public static void incorrectCredentials(Response response) throws IOException {
		jsonWithMessages(response, HttpResponseStatus.UNAUTHORIZED, "Incorrect credentials");
	}

	public static void jsonWithMessages(Response response, int status, String... messages) throws IOException {
		response
				.status(status)
				.sendJson(Map.of("messages", messages));
	}

	public static void sendConfirmationCode(Response response, User user) throws IOException {
		EmailConfirmationCodeData codeData = new EmailConfirmationCodeData(user.getEmail(), Randomizer.getEmailConfirmationCode());
		boolean codeAdded = Db.emailConfirmationCodes().addConfirmationCode(codeData);
		if (!codeAdded) {
			logger.exception(user.getEmail(), new String[]{"Email confirmation code not added"});
			response.sendStatus(HttpResponseStatus.SERVER_SIDE_ERROR);
			return;
		};

		Email.sendConfirmationCode(user.getEmail(), codeData.getCode());
		jsonWithMessages(response, HttpResponseStatus.OK, "Confirm your email within 10 minutes");
	}
}

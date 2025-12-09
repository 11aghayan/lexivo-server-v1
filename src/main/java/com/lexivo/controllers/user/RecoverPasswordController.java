package com.lexivo.controllers.user;

import com.lexivo.controllers.Controller;
import com.lexivo.db.Db;
import com.lexivo.schema.AuthReqBody;
import com.lexivo.util.Email;
import com.lexivo.util.Randomizer;
import com.lexivo.util.RequestData;
import com.sun.net.httpserver.HttpExchange;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RecoverPasswordController extends Controller {
	public RecoverPasswordController(String path) {
		super(path);
	}

	@Override
	protected void post(HttpExchange exchange) throws IOException, SQLException {
		AuthReqBody requestBody = RequestData.getCheckedRequestBody(exchange, List.of("email"), AuthReqBody.class);

		if (requestBody == null) return;
		String email = requestBody.getEmail();
		String newPassword = Randomizer.generateUserPassword();
		String newPasswordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		Db.users().updateUserPassword(email, newPasswordHash);

		Email.sendRecoveredPassword(email, newPassword);

		sendOkResponse(exchange, "Check your email");
	}
}

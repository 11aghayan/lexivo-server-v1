package com.lexivo.handlers.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.lexivo.enums.UserRole;
import com.lexivo.util.HttpResponseStatus;
import com.lexivo.util.JwtUtil;
import com.lexivo.util.StandardResponse;
import org.jandle.api.annotations.HttpRequestHandler;
import org.jandle.api.http.Handler;
import org.jandle.api.http.Request;
import org.jandle.api.http.RequestMethod;
import org.jandle.api.http.Response;

import java.io.IOException;
import java.util.Map;

@HttpRequestHandler(method = RequestMethod.POST, path = "/auth/refresh_token")
public class RefreshTokenHandler implements Handler {

	@Override
	public void handle(Request request, Response response) throws IOException {
		var requestBody = request.getBodyJson();
		String refreshToken = (String) requestBody.get(JwtUtil.KEY_REFRESH_TOKEN);

		DecodedJWT decoded = JwtUtil.verifyJwtToken(refreshToken);

		try {
			if (decoded == null) {
				response.sendStatus(HttpResponseStatus.UNAUTHORIZED);
				return;
			}

			String accessToken = JwtUtil.createHMAC256Token(decoded.getClaim(JwtUtil.CLAIM_EMAIL).asString(), UserRole.USER, 5);

			response
					.status(HttpResponseStatus.OK)
					.sendJson(Map.of(JwtUtil.KEY_ACCESS_TOKEN, accessToken));
		}
		catch (Exception e) {
			StandardResponse.serverSideError(response, e);
		}
	}
}

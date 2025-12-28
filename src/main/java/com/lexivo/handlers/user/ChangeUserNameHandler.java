package com.lexivo.handlers.user;

import org.jandle.api.annotations.HttpRequestHandler;
import org.jandle.api.http.Handler;
import org.jandle.api.http.Request;
import org.jandle.api.http.RequestMethod;
import org.jandle.api.http.Response;

import java.io.IOException;

@HttpRequestHandler(method = RequestMethod.PUT, path = "/user/change_name")
public class ChangeUserNameHandler implements Handler {
	@Override
	public void handle(Request request, Response response) throws IOException {
//		TODO:
		response.sendStatus(405);
	}
}

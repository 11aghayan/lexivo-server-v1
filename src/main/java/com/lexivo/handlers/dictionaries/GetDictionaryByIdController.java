package com.lexivo.handlers.dictionaries;

import org.jandle.api.annotations.HttpRequestHandler;
import org.jandle.api.http.Handler;
import org.jandle.api.http.Request;
import org.jandle.api.http.RequestMethod;
import org.jandle.api.http.Response;

import java.io.IOException;

@HttpRequestHandler(method = RequestMethod.GET, path = "/dictionaries/{id}")
public class GetDictionaryByIdController implements Handler {
	@Override
	public void handle(Request request, Response response) throws IOException {
		//		TODO: Implement
		response.sendStatus(202);
	}
}

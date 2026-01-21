package com.lexivo.handlers.dict;

import com.lexivo.db.Db;
import com.lexivo.exceptions.UnauthorizedAccessException;
import com.lexivo.filters.AuthVerifierFilter;
import com.lexivo.util.HttpResponseStatus;
import com.lexivo.util.StandardResponse;
import org.jandle.api.annotations.HttpRequestFilters;
import org.jandle.api.annotations.HttpRequestHandler;
import org.jandle.api.http.Handler;
import org.jandle.api.http.Request;
import org.jandle.api.http.RequestMethod;
import org.jandle.api.http.Response;

import java.io.IOException;

@HttpRequestHandler(method = RequestMethod.DELETE, path = "/dict/{id}")
@HttpRequestFilters({AuthVerifierFilter.class})
public class DeleteDictHandler implements Handler {

	@Override
	public void handle(Request request, Response response) throws IOException {
		try {
			String dictId = request.getParam("id");
			String userEmail = (String) request.getAttribute("userEmail");

			Db.dict().delete(dictId, userEmail);

			response.sendStatus(HttpResponseStatus.OK);
		}
		catch (UnauthorizedAccessException e) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.UNAUTHORIZED);
		}
		catch (Exception e) {
			StandardResponse.serverSideError(response, e);
		}
	}
}

package com.lexivo.handlers.dict;

import com.lexivo.db.Db;
import com.lexivo.filters.AuthVerifierFilter;
import com.lexivo.filters.DictBodyValidationFilter;
import com.lexivo.schema.appschema.Dict;
import com.lexivo.util.HttpResponseStatus;
import com.lexivo.util.StandardResponse;
import org.jandle.api.annotations.HttpRequestFilters;
import org.jandle.api.annotations.HttpRequestHandler;
import org.jandle.api.http.Handler;
import org.jandle.api.http.Request;
import org.jandle.api.http.RequestMethod;
import org.jandle.api.http.Response;

import java.io.IOException;
import java.util.List;

@HttpRequestHandler(method = RequestMethod.POST, path = "/dict")
@HttpRequestFilters({ AuthVerifierFilter.class, DictBodyValidationFilter.class })
public class AddDictHandler implements Handler {

	@Override
	public void handle(Request request, Response response) throws IOException {
		try {
			Dict dict = (Dict) request.getAttribute("dict");
			String userEmail = (String) request.getAttribute("userEmail");

			Db.dict().add(List.of(dict), userEmail);

			response.sendStatus(HttpResponseStatus.OK);
		}
		catch (Exception e) {
			System.out.println(66);
			StandardResponse.serverSideError(response, e);
		}

	}
}

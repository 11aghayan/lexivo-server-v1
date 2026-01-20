package com.lexivo.filters;

import com.lexivo.exceptions.MissingIdException;
import com.lexivo.exceptions.InvalidDataException;
import com.lexivo.schema.appschema.Grammar;
import com.lexivo.util.HttpResponseStatus;
import com.lexivo.util.ReceivedDataUtil;
import com.lexivo.util.StandardResponse;
import org.jandle.api.http.Chain;
import org.jandle.api.http.Filter;
import org.jandle.api.http.Request;
import org.jandle.api.http.Response;

import java.io.IOException;

public class GrammarBodyValidationFilter implements Filter {
	@Override
	public void doFilter(Request request, Response response, Chain filterChain) throws IOException {
		try {
			var reqBody = request.getBodyJson();

			var grammarList = ReceivedDataUtil.getGrammarListFromJson(reqBody);
			if (grammarList.isEmpty()) throw new Exception();

			Grammar g = grammarList.getFirst();

			request.setAttribute("grammar", g);
			filterChain.next(request, response);
		}
		catch (MissingIdException | InvalidDataException e) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, e.getMessage());
		}
		catch (Exception e) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "invalid grammar data provided");
		}
	}
}

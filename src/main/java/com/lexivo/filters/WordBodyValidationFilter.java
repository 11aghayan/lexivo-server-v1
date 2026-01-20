package com.lexivo.filters;

import com.lexivo.exceptions.IncorrectEnumStringException;
import com.lexivo.exceptions.MissingIdException;
import com.lexivo.exceptions.InvalidDataException;
import com.lexivo.schema.appschema.Word;
import com.lexivo.util.HttpResponseStatus;
import com.lexivo.util.ReceivedDataUtil;
import com.lexivo.util.StandardResponse;
import org.jandle.api.http.Chain;
import org.jandle.api.http.Filter;
import org.jandle.api.http.Request;
import org.jandle.api.http.Response;

import java.io.IOException;
import java.util.List;

public class WordBodyValidationFilter implements Filter {
	@Override
	public void doFilter(Request request, Response response, Chain filterChain) throws IOException {
		try {
			var reqBody = request.getBodyJson();

			List<Word> wordList = ReceivedDataUtil.getWordListFromJson(List.of(reqBody));
			if (wordList.isEmpty()) throw new Exception();

			request.setAttribute("word", wordList.getFirst());
			filterChain.next(request, response);
		}
		catch (MissingIdException | IncorrectEnumStringException | InvalidDataException e) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, e.getMessage());
		}
		catch (Exception e) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "invalid data provided");
		}
	}
}

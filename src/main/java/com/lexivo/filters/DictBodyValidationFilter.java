package com.lexivo.filters;

import com.lexivo.exceptions.IncorrectEnumStringException;
import com.lexivo.exceptions.IncorrectLangException;
import com.lexivo.exceptions.MissingIdException;
import com.lexivo.exceptions.InvalidDataException;
import com.lexivo.schema.appschema.Dict;
import com.lexivo.util.HttpResponseStatus;
import com.lexivo.util.ReceivedDataUtil;
import com.lexivo.util.StandardResponse;
import org.jandle.api.http.Chain;
import org.jandle.api.http.Filter;
import org.jandle.api.http.Request;
import org.jandle.api.http.Response;

import java.io.IOException;

public class DictBodyValidationFilter implements Filter {
	@Override
	public void doFilter(Request request, Response response, Chain filterChain) throws IOException {
		try {
			var reqBody = request.getBodyJson();
			var id = (String) reqBody.get("id");
			Object langUnchecked = reqBody.get("lang");
			Object wordsUnchecked = reqBody.get("words");
			Object grammarListUnchecked = reqBody.get("grammarList");

			var words = ReceivedDataUtil.getWordListFromJson(wordsUnchecked);
			var grammarList = ReceivedDataUtil.getGrammarListFromJson(grammarListUnchecked);
			var lang = ReceivedDataUtil.getLangFromJson(langUnchecked);

			var d = new Dict(id, words, grammarList, lang);

			request.setAttribute("dict", d);
			filterChain.next(request, response);
		}
		catch (MissingIdException | InvalidDataException | IncorrectEnumStringException | IncorrectLangException e) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, e.getMessage());
		}
		catch(Exception e) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "invalid dict data provided");
		}
	}
}

package com.lexivo.filters;

import com.lexivo.exceptions.IncorrectEnumStringException;
import com.lexivo.exceptions.MissingIdException;
import com.lexivo.exceptions.MissingRequiredDataException;
import com.lexivo.exceptions.ValueOutOfRangeException;
import com.lexivo.schema.appschema.Word;
import com.lexivo.schema.appschema.enums.WordGender;
import com.lexivo.schema.appschema.enums.WordLevel;
import com.lexivo.schema.appschema.enums.WordType;
import com.lexivo.util.HttpResponseStatus;
import com.lexivo.util.StandardResponse;
import org.jandle.api.http.Chain;
import org.jandle.api.http.Filter;
import org.jandle.api.http.Request;
import org.jandle.api.http.Response;

import java.io.IOException;

public class WordBodyValidationFilter implements Filter {
	@Override
	public void doFilter(Request request, Response response, Chain filterChain) throws IOException {
		var bodyJson = request.getBodyJson();

		String id = (String) bodyJson.get("id");
		String type = (String) bodyJson.get("type");
		String level = (String) bodyJson.get("level");
		String gender = (String) bodyJson.get("gender");
		int practiceCountDown = (int) bodyJson.get("practiceCountdown");
		String ntv = (String) bodyJson.get("ntv");
		String ntvDetails = (String) bodyJson.get("ntvDetails");
		String plural = (String) bodyJson.get("plural");
		String past1 = (String) bodyJson.get("past1");
		String past2 = (String) bodyJson.get("past2");
		String desc = (String) bodyJson.get("desc");
		String descDetails = (String) bodyJson.get("descDetails");

		try {
			Word w = new Word(
					id,
					WordType.fromString(type),
					WordLevel.fromString(level),
					WordGender.fromString(gender),
					practiceCountDown,
					ntv,
					ntvDetails,
					plural,
					past1,
					past2,
					desc,
					descDetails
			);

			request.setAttribute("word", w);
			filterChain.next(request, response);
		}
		catch (MissingIdException | IncorrectEnumStringException | MissingRequiredDataException e) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, e.getMessage());
		}
		catch (ValueOutOfRangeException e) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "practiceCountdown should be an integer between range [" + e.minValue + "-" + e.maxValue + "]", "provided: " + e.providedValue);
		}
		catch (Exception e) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "invalid data provided");
		}
	}
}

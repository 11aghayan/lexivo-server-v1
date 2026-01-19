package com.lexivo.filters;

import com.lexivo.exceptions.MissingIdException;
import com.lexivo.exceptions.MissingRequiredDataException;
import com.lexivo.schema.appschema.Grammar;
import com.lexivo.schema.appschema.GrammarSubmenu;
import com.lexivo.util.HttpResponseStatus;
import com.lexivo.util.StandardResponse;
import org.jandle.api.http.Chain;
import org.jandle.api.http.Filter;
import org.jandle.api.http.Request;
import org.jandle.api.http.Response;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GrammarBodyValidationFilter implements Filter {
	@Override
	public void doFilter(Request request, Response response, Chain filterChain) throws IOException {
		try {
			var reqBody = request.getBodyJson();
			String id = (String) reqBody.get("id");
			String header = (String) reqBody.get("header");
			Object submenuListUnchecked = reqBody.get("submenuList");

			if (submenuListUnchecked == null) {
				StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "submenu list is missing");
				return;
			}
			if (!(submenuListUnchecked instanceof List)) {
				StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "submenu list is not a list");
				return;
			}

			var submenuList = getSubmenuList((List<?>) submenuListUnchecked);

			Grammar g = new Grammar(id, header, submenuList);

			request.setAttribute("grammar", g);
			filterChain.next(request, response);
		}
		catch (MissingIdException | MissingRequiredDataException e) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, e.getMessage());
		}
		catch (Exception e) {
			StandardResponse.jsonWithMessages(response, HttpResponseStatus.BAD_REQUEST, "invalid data provided");
		}
	}

	private List<GrammarSubmenu> getSubmenuList(List<?> submenuListUnchecked) throws MissingRequiredDataException, MissingIdException {
		List<GrammarSubmenu> submenuList = new LinkedList<>();

		for (var sm : submenuListUnchecked) {
			try {
				String smId = (String) ((Map<?, ?>) sm).get("id");
				String smHeader = (String) ((Map<?, ?>) sm).get("header");
				List<?> explanationsUnchecked = (List<?>) ((Map<?, ?>) sm).get("explanations");
				List<?> examplesUnchecked = (List<?>) ((Map<?, ?>) sm).get("examples");

				List<String> explanations = explanationsUnchecked == null ? null : explanationsUnchecked.stream()
						.map(String.class::cast)
						.toList();

				List<String> examples = examplesUnchecked == null
						? null
						: examplesUnchecked.stream()
						.map(String.class::cast)
						.toList();

				submenuList.add(new GrammarSubmenu(smId, smHeader, explanations, examples));
			}
			catch (ClassCastException e) {
				throw new MissingRequiredDataException("grammar submenu incorrect data provided");
			}
		}
		return submenuList;
	}
}

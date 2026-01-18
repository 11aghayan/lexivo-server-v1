package com.lexivo.schema.appschema;

import com.lexivo.exceptions.MissingIdException;
import com.lexivo.exceptions.MissingRequiredDataException;

import java.util.ArrayList;
import java.util.List;

public class GrammarSubmenu {
	public final String id;
	public final String header;
	private final List<String> explanations;
	private final List<String> examples;

	public GrammarSubmenu(String id, String header, List<String> explanations, List<String> examples) throws MissingIdException, MissingRequiredDataException {
		if (id == null || id.isBlank()) throw new MissingIdException();
		checkRequiredData(header, explanations);

		this.id = id;
		this.header = header;
		this.explanations = explanations;
		this.examples = examples;
	}

	public List<String> getExplanations() {
		return new ArrayList<>(explanations);
	}

	public List<String> getExamples() {
		return new ArrayList<>(examples);
	}

	private void checkRequiredData(String header, List<String> explanations) throws MissingRequiredDataException {
		if (header == null || header.isBlank())
			throw new MissingRequiredDataException("'header' is required");

		if (explanations.isEmpty())
			throw new MissingRequiredDataException("Each grammar submenu must have at least one explanation");
	}
}

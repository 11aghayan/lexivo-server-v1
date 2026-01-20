package com.lexivo.schema.appschema;

import com.lexivo.exceptions.MissingIdException;
import com.lexivo.exceptions.InvalidDataException;

import java.util.ArrayList;
import java.util.List;

public class GrammarSubmenu {
	public final String id;
	public final String header;
	private final List<String> explanations;
	private final List<String> examples;

	public GrammarSubmenu(String id, String header, List<String> explanations, List<String> examples) throws MissingIdException, InvalidDataException {
		if (id == null || id.isBlank()) throw new MissingIdException("grammar submenu id is missing");
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

	private void checkRequiredData(String header, List<String> explanations) throws InvalidDataException {
		if (header == null || header.isBlank())
			throw new InvalidDataException("'header' is required");

		if (explanations == null || explanations.isEmpty())
			throw new InvalidDataException("Each grammar submenu must have at least one explanation");
	}
}

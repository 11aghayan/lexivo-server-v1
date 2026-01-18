package com.lexivo.schema.appschema;

import com.lexivo.exceptions.MissingIdException;

import java.util.ArrayList;
import java.util.List;

public class GrammarSubmenu {
	public final String id;
	public final String header;
	private final List<String> explanations;
	private final List<String> examples;

	public GrammarSubmenu(String id, String header, List<String> explanations, List<String> examples) throws MissingIdException {
		if (id == null || id.isBlank()) throw new MissingIdException();

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
}

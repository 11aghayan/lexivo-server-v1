package com.lexivo.schema.appschema;

import com.lexivo.exceptions.MissingIdException;

import java.util.ArrayList;
import java.util.List;

public class Dict {
	public final String id;
	private final List<Word> words;
	private final List<Grammar> grammarList;
	public final Lang lang;

	public Dict(String id, List<Word> words, List<Grammar> grammarList, Lang lang) throws MissingIdException {
		if (id == null || id.isBlank()) throw new MissingIdException();

		this.id = id;
		this.words = words;
		this.grammarList = grammarList;
		this.lang = lang;
	}

	public List<Word> getWords() {
		return new ArrayList<>(words);
	}

	public List<Grammar> getGrammarList() {
		return new ArrayList<>(grammarList);
	}
}

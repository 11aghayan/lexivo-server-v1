package com.lexivo.schema.appschema.enums;

import com.lexivo.exceptions.IncorrectEnumStringException;

import java.util.Arrays;

public enum WordType {
	NOUN,
	ADJ_ADV,
	VERB,
	PRON_PREP,
	QUESTION_WORD,
	NUMERAL,
	PHRASE;

	public static WordType fromString(String s) throws IncorrectEnumStringException {
		if (s == null) throw new IncorrectEnumStringException(null);

		final String enumString = s.toUpperCase();
		var filteredList = Arrays.stream(values()).filter(v -> v.toString().equals(enumString)).toList();
		if (filteredList.isEmpty()) throw new IncorrectEnumStringException(enumString);

		return filteredList.getFirst();
	}
}

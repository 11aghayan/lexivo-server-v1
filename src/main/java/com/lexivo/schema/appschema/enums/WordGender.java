package com.lexivo.schema.appschema.enums;

import com.lexivo.exceptions.IncorrectEnumStringException;

import java.util.Arrays;

public enum WordGender {
	MASCULINE,
	FEMININE,
	NEUTER,
	PERSONAL,
	PLURAL,
	NO_GENDER;

	public static WordGender fromString(String s) throws IncorrectEnumStringException {
		if (s == null) return null;

		final String enumString = s.toUpperCase();
		var filteredList = Arrays.stream(values()).filter(v -> v.toString().equals(enumString)).toList();
		if (filteredList.isEmpty()) throw new IncorrectEnumStringException(enumString);

		return filteredList.getFirst();
	}
}

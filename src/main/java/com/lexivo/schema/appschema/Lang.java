package com.lexivo.schema.appschema;

import com.lexivo.exceptions.IncorrectLangException;
import com.lexivo.exceptions.MissingIdException;

public class Lang {
	public final String name;
	public final String nameNative;

	public Lang(String name, String nameNative) throws IncorrectLangException {
		if (name == null || name.isBlank()) throw new IncorrectLangException("name of lang is missing");
		if (nameNative == null || nameNative.isBlank()) throw new IncorrectLangException("nameNative of lang is missing");

		this.name = name;
		this.nameNative = nameNative;
	}
}

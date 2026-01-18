package com.lexivo.util;

import com.lexivo.logger.Logger;
import com.lexivo.schema.appschema.Dict;
import com.lexivo.schema.appschema.Grammar;
import com.lexivo.schema.appschema.GrammarSubmenu;
import com.lexivo.schema.appschema.Word;
import com.lexivo.schema.appschema.enums.WordGender;
import com.lexivo.schema.appschema.enums.WordType;

import java.util.*;

public abstract class ValidationUtil {
	private static final Logger logger = new Logger();
	private static final int NAME_LENGTH = 50;
	public static final String[] PASSWORD_REQUIREMENTS = {"Password must have", "8-32 characters", "at least one upper case letter", "at least on lower case letter", "at least one number"};
	public static final String[] NAME_REQUIREMENTS = {"Name must have", "1-" + NAME_LENGTH + " characters", "only ' '(empty space) characters are not allowed"};

	public static boolean isPasswordValid(String password) {
		return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,32}$");
	}

	public static boolean isNameValid(String name) {
		name = name.trim();
		return !name.isEmpty() && name.length() <= NAME_LENGTH;
	}

	public static String[] getMissingStrings(String[] values, List<String> errorMessages) {
		if (values.length != errorMessages.size()) {
			logger.exception("values.length != errorMessages.size()", "values.length = " + values.length, "errorMessages.size() = " + errorMessages.size());
			throw new IllegalArgumentException("values.size() != errorMessages.size()");
		}
		List<String> missingValues = new LinkedList<>();
		for (int i = 0; i < values.length; i++) {
			var value = values[i];
			if (value == null || value.isBlank()) {
				missingValues.add(errorMessages.get(i));
			}
		}
		return missingValues.toArray(String[]::new);
	}

	public static Map<String, List<String>> checkWords(Word[] words) {
		Map<String, List<String>> wordsWithIncorrectData = new HashMap<>();

		for (var word : words) {
			String mainVal = word.type == WordType.NOUN || word.gender == WordGender.PLURAL ? word.plural : word.ntv;

			if (mainVal == null || mainVal.isBlank())
				Objects.requireNonNull(wordsWithIncorrectData.putIfAbsent(word.id, new LinkedList<>())).add("'native' is required (for nouns with 'gender = plural' 'plural' is required)");

			if (word.desc == null)
				Objects.requireNonNull(wordsWithIncorrectData.putIfAbsent(word.id, new LinkedList<>())).add("'desc' is required");
		}

		return wordsWithIncorrectData.isEmpty() ? null : wordsWithIncorrectData;
	}

	public static Map<String, List<String>> checkGrammars(Grammar[] grammars) {
		Map<String, List<String>> grammarsWithIncorrectData = new HashMap<>();

		for (var grammar : grammars) {
			if (grammar.header == null || grammar.header.isBlank())
				Objects.requireNonNull(grammarsWithIncorrectData.putIfAbsent(grammar.id, new LinkedList<>())).add("'header' is required");

			if (!checksGrammarSubmenus(grammar.getSubmenuList()).isEmpty())
				Objects.requireNonNull(grammarsWithIncorrectData.putIfAbsent(grammar.id, new LinkedList<>())).add(grammar.getSubmenuList().toString());
		}

		return grammarsWithIncorrectData.isEmpty() ? null : grammarsWithIncorrectData;
	}

	private static List<String> checksGrammarSubmenus(List<GrammarSubmenu> grammarSubmenus) {
		List<String> submenusWithIncorrectData = new LinkedList<>();
		boolean headerError = false;
		boolean explanationError = false;

		for (var sm : grammarSubmenus) {
			if (!headerError && sm.header == null || sm.header.isBlank()) {
				submenusWithIncorrectData.add("'header' is required");
				headerError = true;
			}

			if (!explanationError && sm.getExplanations().isEmpty()) {
				submenusWithIncorrectData.add("each grammar submenu must have at least one explanation");
			}
		}

		return submenusWithIncorrectData;
	}
}

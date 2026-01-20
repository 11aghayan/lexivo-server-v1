package com.lexivo.util;

import com.lexivo.exceptions.*;
import com.lexivo.schema.appschema.Grammar;
import com.lexivo.schema.appschema.GrammarSubmenu;
import com.lexivo.schema.appschema.Lang;
import com.lexivo.schema.appschema.Word;
import com.lexivo.schema.appschema.enums.WordGender;
import com.lexivo.schema.appschema.enums.WordLevel;
import com.lexivo.schema.appschema.enums.WordType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class ReceivedDataUtil {
	private static final String ID_JOINING_ELM = "@";

	public static String createJoinedId(String... ids) {
		if (ids.length == 0) throw new RuntimeException("ids.length cannot be 0");
		if (ids.length == 1) return ids[0];

		return String.join(ID_JOINING_ELM, ids);
	}

	public static String[] separateJoinedId(String joinedId) {
		if (joinedId == null) return new String[]{};
		return joinedId.split(ID_JOINING_ELM);
	}

	public static List<Word> getWordListFromJson(Object wordsJson) throws InvalidDataException, IncorrectEnumStringException, MissingIdException {
		if (wordsJson == null) throw new InvalidDataException("words cannot be null");
		if (!(wordsJson instanceof List<?>)) throw new InvalidDataException("words is not a list");

		List<Word> wordList = new LinkedList<>();

		try {
			for (var itemNotCasted : (List<?>) wordsJson) {
				var item = (Map<?, ?>) itemNotCasted;

				String id = (String) item.get("id");
				String type = (String) item.get("type");
				String level = (String) item.get("level");
				String gender = (String) item.get("gender");
				int practiceCountDown = (int) item.get("practiceCountdown");
				String ntv = (String) item.get("ntv");
				String ntvDetails = (String) item.get("ntvDetails");
				String plural = (String) item.get("plural");
				String past1 = (String) item.get("past1");
				String past2 = (String) item.get("past2");
				String desc = (String) item.get("desc");
				String descDetails = (String) item.get("descDetails");

				var w = new Word(
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

				wordList.add(w);
			}

			return wordList;
		}
		catch (ValueOutOfRangeException e) {
			throw new InvalidDataException("practiceCountdown should be an integer between range [" + e.minValue + "-" + e.maxValue + "]; " + "provided: " + e.providedValue);
		}
		catch (Exception e) {
			throw new InvalidDataException("invalid word data provided");
		}
	}

	public static List<Grammar> getGrammarListFromJson(Object grammarListJson) throws InvalidDataException, MissingIdException {
		if (grammarListJson == null) throw new InvalidDataException("grammarList cannot be null");
		if (!(grammarListJson instanceof List<?>)) throw new InvalidDataException("grammarList is not a list");

		List<Grammar> grammarList = new LinkedList<>();

		try {
			for (var itemNotCasted : (List<?>) grammarListJson) {
				var item = (Map<?, ?>) itemNotCasted;

				String id = (String) item.get("id");
				String header = (String) item.get("header");
				Object submenuListJson = item.get("submenuList");

				var submenuList = getGrammarSubmenuListFromJson(submenuListJson);

				var g = new Grammar(id, header, submenuList);

				grammarList.add(g);
			}

			return grammarList;
		}
		catch(Exception e) {
			throw new InvalidDataException("invalid grammar data provided");
		}
	}

	public static List<GrammarSubmenu> getGrammarSubmenuListFromJson(Object submenuListJson) throws InvalidDataException, MissingIdException {
		if (submenuListJson == null) throw new InvalidDataException("submenu list of grammar is missing");
		if (!(submenuListJson instanceof List<?>)) throw new InvalidDataException("submenu list of grammar is not a list");

		List<GrammarSubmenu> submenuList = new LinkedList<>();

		for (var itemNotCasted : (List<?>) submenuListJson) {
			try {
				var sm = (Map<?,?>) itemNotCasted;

				String smId = (String) sm.get("id");
				String smHeader = (String) sm.get("header");
				List<?> explanationsUnchecked = (List<?>) sm.get("explanations");
				List<?> examplesUnchecked = (List<?>) sm.get("examples");

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
				throw new InvalidDataException("incorrect grammar submenu data provided");
			}
		}
		return submenuList;
	}

	public static Lang getLangFromJson(Object langJson) throws InvalidDataException, IncorrectLangException {
		if (langJson == null) throw new InvalidDataException("lang is missing");

		try {
			var lang = (Map<?,?>) langJson;
			var name = (String) lang.get("name");
			var nameNative = (String) lang.get("nameNative");

			return new Lang(name, nameNative);
		}
		catch (ClassCastException e) {
			throw new InvalidDataException("invalid lang data provided");
		}
	}
}

package com.lexivo.schema.appschema;

import com.lexivo.exceptions.MissingIdException;
import com.lexivo.exceptions.ValueOutOfRangeException;
import com.lexivo.schema.appschema.enums.WordGender;
import com.lexivo.schema.appschema.enums.WordLevel;
import com.lexivo.schema.appschema.enums.WordType;

public class Word {
	public final String id;
	public final WordType type;
	public final WordLevel level;
	public final WordGender gender;
	public final int practiceCountdown;
	public final String ntv;
	public final String ntvDetails;
	public final String plural;
	public final String past1;
	public final String past2;
	public final String desc;
	public final String descDetails;

	public Word(String id, WordType type, WordLevel level, WordGender gender, int practiceCountdown, String ntv, String ntvDetails, String plural, String past1, String past2, String desc, String descDetails) throws MissingIdException, ValueOutOfRangeException {
		if (id == null || id.isBlank()) throw new MissingIdException();
		if (practiceCountdown < 0) throw new ValueOutOfRangeException(practiceCountdown, 0, 7);

		this.id = id;
		this.type = type;
		this.level = level;
		this.gender = gender;
		this.practiceCountdown = practiceCountdown;
		this.ntv = ntv;
		this.ntvDetails = ntvDetails;
		this.plural = plural;
		this.past1 = past1;
		this.past2 = past2;
		this.desc = desc;
		this.descDetails = descDetails;
	}
}

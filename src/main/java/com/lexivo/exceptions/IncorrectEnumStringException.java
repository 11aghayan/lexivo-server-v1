package com.lexivo.exceptions;

public class IncorrectEnumStringException extends Exception {
	public final String providedValue;
	public IncorrectEnumStringException(String providedValue) {
		super("The enum value for the provided string value '" + providedValue + "' does not exist");
		this.providedValue = providedValue;
	}
}

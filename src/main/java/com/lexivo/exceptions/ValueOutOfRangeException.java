package com.lexivo.exceptions;

public class ValueOutOfRangeException extends Exception {
	public final Object providedValue;
	public ValueOutOfRangeException(Object providedValue) {
		super("Value " + providedValue.toString() + " is out of allowed range");
		this.providedValue = providedValue;
	}

	public ValueOutOfRangeException(Object providedValue, Object minValue, Object maxValue) {
		super("Value " + providedValue.toString() + " is out of range [" + minValue.toString() + "-" + maxValue.toString() + "]");
		this.providedValue = providedValue;
	}
}

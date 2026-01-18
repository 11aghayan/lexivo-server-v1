package com.lexivo.exceptions;

public class ValueOutOfRangeException extends Exception {
	public final Object providedValue;
	public final Object minValue;
	public final Object maxValue;

	public ValueOutOfRangeException(Object providedValue) {
		super("Value " + providedValue.toString() + " is out of allowed range");
		this.providedValue = providedValue;
		this.minValue = null;
		this.maxValue = null;
	}

	public ValueOutOfRangeException(Object providedValue, Object minValue, Object maxValue) {
		super("Value " + providedValue.toString() + " is out of range [" + minValue.toString() + "-" + maxValue.toString() + "]");
		this.providedValue = providedValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
}

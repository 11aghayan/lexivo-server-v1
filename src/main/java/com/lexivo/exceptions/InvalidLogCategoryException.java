package com.lexivo.exceptions;

public class InvalidLogCategoryException extends Exception {
	public InvalidLogCategoryException(String logCategoryString) {
		super("Invalid Log Category: " + logCategoryString);
	}
}

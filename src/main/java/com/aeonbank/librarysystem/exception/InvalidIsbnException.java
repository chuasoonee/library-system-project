package com.aeonbank.librarysystem.exception;

public class InvalidIsbnException extends RuntimeException {
	
	private static final long serialVersionUID = -5466194861144933765L;

	public InvalidIsbnException(String isbn) {
		super("ISBN: " + isbn + " is invalid");
	}
}

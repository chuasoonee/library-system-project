package com.aeonbank.librarysystem.exception;

public class InvalidBookException extends RuntimeException {

	private static final long serialVersionUID = 7451070238006924335L;
	
	public InvalidBookException(String isbn, String title, String author) {
		super("Book with ISBN: " + isbn + " already exists with different title: " + title + " or author: " + author);
	}
}

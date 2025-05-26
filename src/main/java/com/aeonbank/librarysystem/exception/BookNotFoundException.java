package com.aeonbank.librarysystem.exception;

public class BookNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7735025657161122995L;

	public BookNotFoundException(Long bookId) {
		super("Book with ID: " + bookId + " not found");
	}
}

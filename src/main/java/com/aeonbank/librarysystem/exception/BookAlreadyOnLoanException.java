package com.aeonbank.librarysystem.exception;

public class BookAlreadyOnLoanException extends RuntimeException {

	private static final long serialVersionUID = -8680348735909236077L;

	public BookAlreadyOnLoanException(Long bookId) {
		super("Book with ID: " + bookId + " is already on loan");
	}
}

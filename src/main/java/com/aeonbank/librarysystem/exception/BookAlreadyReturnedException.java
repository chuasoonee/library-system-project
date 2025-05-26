package com.aeonbank.librarysystem.exception;

import java.time.LocalDateTime;

public class BookAlreadyReturnedException extends RuntimeException {

	private static final long serialVersionUID = -5724725177450113591L;

	public BookAlreadyReturnedException(Long bookId, Long borrowerId, LocalDateTime loanDate,
			LocalDateTime returnedDate) {
		super("Book with ID: " + bookId + " loaned on " + loanDate.toString() + " has been returned on "
				+ returnedDate.toString() + " by borrowerId: " + borrowerId);
	}

	public BookAlreadyReturnedException(Long loanId) {
		super("Book with loanId: " + loanId + " has been returned");
	}
}

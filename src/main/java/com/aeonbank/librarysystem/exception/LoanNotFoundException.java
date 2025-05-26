package com.aeonbank.librarysystem.exception;

public class LoanNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7070095154589126459L;

	public LoanNotFoundException(Long loanId) {
		super("Loan with ID: " + loanId + " not found");
	}
}

package com.aeonbank.librarysystem.exception;

public class BorrowerNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 3088378725774824477L;

	public BorrowerNotFoundException(Long borrowerId) {
		super("Borrower with ID: " + borrowerId + " not found");
	}
}

package com.aeonbank.librarysystem.exception;

public class BorrowerAlreadyRegisteredException extends RuntimeException {

	private static final long serialVersionUID = -6904626305304222774L;

	public BorrowerAlreadyRegisteredException(String email) {
		super("Borrower with Email: " + email + " already exists");
	}
}

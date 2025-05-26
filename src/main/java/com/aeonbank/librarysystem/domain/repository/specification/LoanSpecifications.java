package com.aeonbank.librarysystem.domain.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.aeonbank.librarysystem.domain.model.Loan;
import com.aeonbank.librarysystem.domain.model.LoanStatus;

public class LoanSpecifications {

	public static Specification<Loan> withFilters(String borrowerName, String borrowerEmail, LoanStatus loanStatus) {
		return (root, query, cb) -> {

			Specification<Loan> spec = Specification.where(null);

			if (borrowerEmail != null && !borrowerEmail.isEmpty()) {
				spec = spec.and(borrowerEmailEqual(borrowerEmail));
			}
			if (borrowerName != null && !borrowerName.isEmpty()) {
				spec = spec.and(borrowerNameContains(borrowerName));
			}
			if (loanStatus != null) {
				if (loanStatus == LoanStatus.ON_LOAN) {
					spec = spec.and(returnedDateIsNull());
				} else {
					spec = spec.and(returnedDateIsNotNull());
				}
			}
			return spec.toPredicate(root, query, cb);
		};
	}
	
	private static Specification<Loan> borrowerNameContains(String borrowerName) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("borrower").get("name")),
				"%" + borrowerName.toLowerCase() + "%");
	}

	private static Specification<Loan> borrowerEmailEqual(String borrowerEmail) {
		return (root, query, cb) -> cb.equal(root.get("borrower").get("email"), borrowerEmail);
	}

	private static Specification<Loan> returnedDateIsNull() {
		return (root, query, cb) -> cb.isNull(root.get("returnedDate"));
	}
	
	private static Specification<Loan> returnedDateIsNotNull() {
		return (root, query, cb) -> cb.isNotNull(root.get("returnedDate"));
	}
}
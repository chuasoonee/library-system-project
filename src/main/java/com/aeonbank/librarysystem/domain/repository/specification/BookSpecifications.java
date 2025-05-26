package com.aeonbank.librarysystem.domain.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.aeonbank.librarysystem.domain.model.Book;
import com.aeonbank.librarysystem.domain.model.Loan;
import com.aeonbank.librarysystem.domain.model.LoanStatus;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class BookSpecifications {

	public static Specification<Book> withFilters(String isbn, String title, String author, LoanStatus loanStatus) {
		return (root, query, cb) -> {

			Specification<Book> spec = Specification.where(null);

			if (title != null && !title.isEmpty()) {
				spec = spec.and(titleContains(title));
			}
			if (author != null && !author.isEmpty()) {
				spec = spec.and(authorContains(author));
			}
			if (isbn != null && !isbn.isEmpty()) {
				spec = spec.and(isbnEqual(isbn));
			}
			if (loanStatus != null) {
				spec = spec.and(hasLoanStatus(loanStatus));
			}
			return spec.toPredicate(root, query, cb);
		};
	}

	private static Specification<Book> titleContains(String title) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
	}

	private static Specification<Book> authorContains(String author) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%");
	}

	private static Specification<Book> isbnEqual(String isbn) {
		return (root, query, cb) -> cb.equal(root.get("isbn"), isbn);
	}

	private static Specification<Book> hasLoanStatus(LoanStatus loanStatus) {
		return (root, query, cb) -> {
			Subquery<Long> subquery = query.subquery(Long.class);
			Root<Loan> loanRoot = subquery.from(Loan.class);
			subquery.select(cb.literal(1L));
			subquery.where(cb.and(cb.equal(loanRoot.get("book"), root), // Join on book
					cb.isNull(loanRoot.get("returnedDate")) // Unreturned loans
			));

			if (loanStatus == LoanStatus.ON_LOAN) {
				return cb.exists(subquery); // Books with active loans
			} else {
				return cb.not(cb.exists(subquery)); // Books without active loans
			}
		};
	}
}
package com.aeonbank.librarysystem.domain.model;

import java.time.LocalDateTime;

import com.aeonbank.librarysystem.exception.BookAlreadyReturnedException;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Loan extends BaseModel {

	@ManyToOne
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;

	@ManyToOne
	@JoinColumn(name = "borrower_id", nullable = false)
	private Borrower borrower;

	private LocalDateTime loanDate;
	private LocalDateTime returnedDate;

	public Loan(Book book, Borrower borrower) {
		this.book = book;
		this.borrower = borrower;
		this.loanDate = LocalDateTime.now();
	}

	public void returnBook() {
		if (returnedDate != null)
			throw new BookAlreadyReturnedException(book.getId(), borrower.getId(), loanDate, returnedDate);
		returnedDate = LocalDateTime.now();
	}
}
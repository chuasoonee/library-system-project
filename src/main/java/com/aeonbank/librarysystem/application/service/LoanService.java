package com.aeonbank.librarysystem.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aeonbank.librarysystem.domain.model.Book;
import com.aeonbank.librarysystem.domain.model.Borrower;
import com.aeonbank.librarysystem.domain.model.Loan;
import com.aeonbank.librarysystem.domain.model.LoanStatus;
import com.aeonbank.librarysystem.domain.repository.BookRepository;
import com.aeonbank.librarysystem.domain.repository.BorrowerRepository;
import com.aeonbank.librarysystem.domain.repository.LoanRepository;
import com.aeonbank.librarysystem.domain.repository.specification.LoanSpecifications;
import com.aeonbank.librarysystem.exception.BookAlreadyOnLoanException;
import com.aeonbank.librarysystem.exception.BookAlreadyReturnedException;
import com.aeonbank.librarysystem.exception.BookNotFoundException;
import com.aeonbank.librarysystem.exception.BorrowerNotFoundException;
import com.aeonbank.librarysystem.exception.LoanNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanService {

	private final LoanRepository loanRepository;
	private final BookRepository bookRepository;
	private final BorrowerRepository borrowerRepository;

	public Loan loanBook(Long borrowerId, Long bookId) {

		Borrower borrower = borrowerRepository.findById(borrowerId)
				.orElseThrow(() -> new BorrowerNotFoundException(borrowerId));

		Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

		boolean isOnLoan = loanRepository.findByBookIdAndReturnedDateIsNull(bookId).isPresent();
		if (isOnLoan) {
			throw new BookAlreadyOnLoanException(bookId);
		}

		return loanRepository.save(new Loan(book, borrower));
	}

	public void returnBook(Long loanId) {
		
		Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new LoanNotFoundException(loanId));
		if (loan.getReturnedDate() != null) {
			throw new BookAlreadyReturnedException(loanId);
		}
		loan.returnBook();
		loanRepository.save(loan);
	}

	public Page<Loan> getAllLoans(String borrowerName, String borrowerEmail, LoanStatus loanStatus, Pageable pageable) {

		Specification<Loan> spec = LoanSpecifications.withFilters(borrowerName, borrowerEmail, loanStatus);
		return loanRepository.findAll(spec, pageable);
	}
}

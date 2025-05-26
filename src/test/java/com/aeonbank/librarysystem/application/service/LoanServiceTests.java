package com.aeonbank.librarysystem.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.aeonbank.librarysystem.domain.model.Book;
import com.aeonbank.librarysystem.domain.model.Borrower;
import com.aeonbank.librarysystem.domain.model.Loan;
import com.aeonbank.librarysystem.domain.repository.BookRepository;
import com.aeonbank.librarysystem.domain.repository.BorrowerRepository;
import com.aeonbank.librarysystem.domain.repository.LoanRepository;
import com.aeonbank.librarysystem.exception.BookAlreadyOnLoanException;
import com.aeonbank.librarysystem.exception.BookAlreadyReturnedException;
import com.aeonbank.librarysystem.exception.BookNotFoundException;
import com.aeonbank.librarysystem.exception.BorrowerNotFoundException;
import com.aeonbank.librarysystem.exception.LoanNotFoundException;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTests {

	@Mock
	private LoanRepository loanRepository;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private BorrowerRepository borrowerRepository;

	@InjectMocks
	private LoanService loanService;

	private Borrower borrower;
	private Book book;
	private Loan onLoan;
	private Specification<Loan> anyLoanSpec() {
	    return ArgumentMatchers.any();
	}
	
	@BeforeEach
	public void setup() {
		borrower = new Borrower("Chua Soon Ee", "chuasoonee@yahoo.com");
		book = new Book("9781443456623", "The 5 AM Club", "Robin Sharma");
		onLoan = new Loan(book, borrower);
	}

	@Test
	public void LoanService_LoanBook_WhenBorrowerNotFound_ThrowException() {
		when(borrowerRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> loanService.loanBook(1L, 2L)).isInstanceOf(BorrowerNotFoundException.class)
				.hasMessageContaining("1");

		verify(loanRepository, never()).save(any(Loan.class));
	}

	@Test
	public void LoanService_LoanBook_WhenBookNotFound_ThrowException() {
		when(borrowerRepository.findById(anyLong())).thenReturn(Optional.of(borrower));
		when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> loanService.loanBook(1L, 2L)).isInstanceOf(BookNotFoundException.class)
				.hasMessageContaining("2");

		verify(loanRepository, never()).save(any(Loan.class));
	}

	@Test
	public void LoanService_LoanBook_WhenBookAlreadyOnLoan_ThrowException() {
		when(borrowerRepository.findById(anyLong())).thenReturn(Optional.of(borrower));
		when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
		when(loanRepository.findByBookIdAndReturnedDateIsNull(anyLong())).thenReturn(Optional.of(onLoan));

		assertThatThrownBy(() -> loanService.loanBook(1L, 2L)).isInstanceOf(BookAlreadyOnLoanException.class)
				.hasMessageContaining("2");

		verify(loanRepository, never()).save(any(Loan.class));
	}

	@Test
	public void LoanService_LoanBook_WhenConditionsAreMet_SaveLoan() {
		when(borrowerRepository.findById(anyLong())).thenReturn(Optional.of(borrower));
		when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
		when(loanRepository.findByBookIdAndReturnedDateIsNull(anyLong())).thenReturn(Optional.empty());
		when(loanRepository.save(any(Loan.class))).thenReturn(onLoan);

		Loan savedLoan = loanService.loanBook(1L, 2L);

		assertThat(savedLoan).isNotNull();
		assertThat(savedLoan.getBook()).isEqualTo(book);
		assertThat(savedLoan.getBorrower()).isEqualTo(borrower);
		verify(loanRepository, times(1)).save(any(Loan.class));
	}

	@Test
	public void LoanService_ReturnBook_WhenConditionsAreMet_UpdateLoan() {
		when(loanRepository.findById(anyLong())).thenReturn(Optional.of(onLoan));

		loanService.returnBook(1L);

		assertThat(onLoan.getReturnedDate()).isNotNull();
		verify(loanRepository, times(1)).save(onLoan);
	}

	@Test
	public void LoanService_ReturnBook_WhenLoanNotFound_ThrowException() {
		when(loanRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> loanService.returnBook(1L)).isInstanceOf(LoanNotFoundException.class)
				.hasMessageContaining("1");

		verify(loanRepository, never()).save(any(Loan.class));
	}

	@Test
	public void LoanService_ReturnBook_WhenBookAlreadyReturned_ThrowException() {
		onLoan.returnBook();
		when(loanRepository.findById(anyLong())).thenReturn(Optional.of(onLoan));

		assertThatThrownBy(() -> loanService.returnBook(1L)).isInstanceOf(BookAlreadyReturnedException.class)
				.hasMessageContaining("1");

		verify(loanRepository, never()).save(any(Loan.class));
	}

	@Test
	public void LoanService_GetLoans_ReturnFilteredLoans() {
		
		Pageable pageable = PageRequest.of(0, 10);
		Page<Loan> loans = new PageImpl<>(List.of(onLoan));

		when(loanRepository.findAll(anyLoanSpec(), any(Pageable.class))).thenReturn(loans);

		Page<Loan> result = loanService.getAllLoans("Chua Soon Ee", "chuasoonee@yahoo.com", null, pageable);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).containsExactly(onLoan);
	}
}
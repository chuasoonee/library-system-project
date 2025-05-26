package com.aeonbank.librarysystem.domain.repository;


import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.aeonbank.librarysystem.domain.model.Book;
import com.aeonbank.librarysystem.domain.model.Borrower;
import com.aeonbank.librarysystem.domain.model.Loan;
import com.aeonbank.librarysystem.domain.repository.specification.LoanSpecifications;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class LoanRepositoryTests {

	@Autowired
    private BookRepository bookRepository;
	@Autowired
	private BorrowerRepository borrowerRepository;
	@Autowired
	private LoanRepository loanRepository;
    
    private Loan onLoan;
    private Loan returnedLoan;
    private Book book;
    private Borrower borrower;

    @BeforeEach
    public void setup() {
    	
        borrower = new Borrower("Chua Soon Ee", "chuasoonee@yahoo.com");
        borrowerRepository.save(borrower);

        book = new Book("9781443456623", "The 5 AM Club", "Robin Sharma");
        bookRepository.save(book);
        
        onLoan = new Loan(book, borrower);
        loanRepository.save(onLoan);
        
		returnedLoan = new Loan(book, borrower);
        returnedLoan.returnBook(); // Returned loan
        loanRepository.save(returnedLoan);
    }

    @Test
    public void LoanRepository_FindByBookIdAndReturnedDateIsNull_ReturnsActiveLoan() {
        Optional<Loan> foundLoan = loanRepository.findByBookIdAndReturnedDateIsNull(book.getId());
        Assertions.assertThat(foundLoan).isPresent();
        Assertions.assertThat(foundLoan.get().getReturnedDate()).isNull();
    }

    @Test
    public void LoanRepository_FindByBookIdAndReturnedDateIsNull_ReturnsEmptyOptional() {
        Book anotherBook = new Book("1936594234", "The Power of Your Subconscious Mind", "Joseph Murphy");
        Optional<Loan> foundLoan = loanRepository.findByBookIdAndReturnedDateIsNull(anotherBook.getId());
        Assertions.assertThat(foundLoan).isEmpty();
    }

    @Test
    public void LoanRepository_Save_ReturnsSavedLoan() {
        Loan newLoan = new Loan(book, borrower);
        Loan savedLoan = loanRepository.save(newLoan);

        Assertions.assertThat(savedLoan).isNotNull();
        Assertions.assertThat(savedLoan.getId()).isNotNull();
        Assertions.assertThat(savedLoan.getId()).isGreaterThan(0);
    }

    @Test
    public void LoanRepository_FindById_ReturnsLoan() {
        Optional<Loan> foundLoan = loanRepository.findById(onLoan.getId());
        Assertions.assertThat(foundLoan).isPresent();
        Assertions.assertThat(foundLoan.get().getBook().getTitle()).isEqualTo("The 5 AM Club");
    }

    @Test
    public void LoanRepository_FindById_ReturnsEmptyOptional() {
        Optional<Loan> foundLoan = loanRepository.findById(999L); // Invalid loan ID
        Assertions.assertThat(foundLoan).isEmpty();
    }

    @Test
    public void LoanRepository_FindAllWithSpecification_ReturnsFilteredLoans() {
        Specification<Loan> spec = LoanSpecifications.withFilters("Chua Soon Ee", "chuasoonee@yahoo.com", null);
        Pageable pageable = PageRequest.of(0, 10);
        
        Page<Loan> loans = loanRepository.findAll(spec, pageable);

        Assertions.assertThat(loans).isNotNull();
        Assertions.assertThat(loans.getTotalElements()).isEqualTo(2); // Both loans for borrower "Chua Soon Ee"
    }
}
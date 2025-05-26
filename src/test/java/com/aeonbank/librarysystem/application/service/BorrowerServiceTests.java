package com.aeonbank.librarysystem.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aeonbank.librarysystem.domain.model.Borrower;
import com.aeonbank.librarysystem.domain.repository.BorrowerRepository;
import com.aeonbank.librarysystem.exception.BorrowerAlreadyRegisteredException;

@ExtendWith(MockitoExtension.class)
public class BorrowerServiceTests {

	@Mock
	private BorrowerRepository borrowerRepository;

	@InjectMocks
	private BorrowerService borrowerService;

	private Borrower borrower;

	@BeforeEach
	public void setup() {
		borrower = new Borrower("Chua Soon Ee", "chuasoonee@yahoo.com");
	}

	@Test
	public void BorrowerService_RegisterBorrower_WhenEmailNotRegistered_SaveBorrower() {
		// Arrange
		when(borrowerRepository.findByEmail(borrower.getEmail())).thenReturn(Optional.empty());
		when(borrowerRepository.save(any(Borrower.class))).thenReturn(borrower);

		// Act
		Borrower registeredBorrower = borrowerService.registerBorrower(borrower.getName(), borrower.getEmail());

		// Assert
		Assertions.assertThat(registeredBorrower).isNotNull();
		Assertions.assertThat(registeredBorrower.getEmail()).isEqualTo("chuasoonee@yahoo.com");
		verify(borrowerRepository, times(1)).save(any(Borrower.class));
	}

	@Test
	public void BorrowerService_GetAllBooks_ReturnAllBooks() {
		when(borrowerRepository.findAll())
				.thenReturn(List.of(borrower));

		List<Borrower> allBorrowers = borrowerService.getAllBorrowers();

		assertThat(allBorrowers).isNotNull();
		assertThat(allBorrowers.size()).isEqualTo(1);
	}
	
	@Test
	public void BorrowerService_RegisterBorrower_WhenEmailAlreadyRegistered_ThrowException() {
		// Arrange
		when(borrowerRepository.findByEmail(borrower.getEmail())).thenReturn(Optional.of(borrower));

		// Act & Assert
		Assertions.assertThatThrownBy(() -> borrowerService.registerBorrower(borrower.getName(), borrower.getEmail()))
				.isInstanceOf(BorrowerAlreadyRegisteredException.class).hasMessageContaining("chuasoonee@yahoo.com");

		verify(borrowerRepository, never()).save(any(Borrower.class));
	}
}
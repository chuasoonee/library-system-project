package com.aeonbank.librarysystem.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aeonbank.librarysystem.domain.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {

	Optional<Loan> findByBookIdAndReturnedDateIsNull(Long bookId);
}
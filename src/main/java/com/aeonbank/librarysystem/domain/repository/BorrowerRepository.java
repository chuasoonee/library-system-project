package com.aeonbank.librarysystem.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aeonbank.librarysystem.domain.model.Borrower;

public interface BorrowerRepository extends JpaRepository<Borrower, Long>, JpaSpecificationExecutor<Borrower> {
	
	Optional<Borrower> findByEmail(String email);
}
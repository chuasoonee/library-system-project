package com.aeonbank.librarysystem.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aeonbank.librarysystem.domain.model.Borrower;
import com.aeonbank.librarysystem.domain.repository.BorrowerRepository;
import com.aeonbank.librarysystem.exception.BorrowerAlreadyRegisteredException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowerService {

	private final BorrowerRepository borrowerRepository;
	
	public Borrower registerBorrower(String name, String email) {
		
		borrowerRepository.findByEmail(email)
			.ifPresent(borrower -> { 
				throw new BorrowerAlreadyRegisteredException(email);
			});
		
		return borrowerRepository.save(new Borrower(name, email));
	}
	
	public List<Borrower> getAllBorrowers() {

		return borrowerRepository.findAll();
	}
}
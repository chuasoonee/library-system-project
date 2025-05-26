package com.aeonbank.librarysystem.interfaces.rest.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aeonbank.librarysystem.application.service.BorrowerService;
import com.aeonbank.librarysystem.domain.model.Borrower;
import com.aeonbank.librarysystem.interfaces.rest.dto.RegisterBorrowerRequestDTO;
import com.aeonbank.librarysystem.interfaces.rest.dto.RegisterBorrowerResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BorrowerController {

	private final BorrowerService borrowerService;

	@Operation(summary = "Register a new borrower to the library", description = "Register a new borrower to the library")
	@PostMapping("/borrowers")
	public ResponseEntity<RegisterBorrowerResponseDTO> registerBorrower(
			@RequestBody @Valid RegisterBorrowerRequestDTO request) {

		Borrower borrower = borrowerService.registerBorrower(request.name(), request.email());
		return ResponseEntity.created(URI.create("/borrowers/" + borrower.getId()))
				.body(new RegisterBorrowerResponseDTO(borrower.getId(), borrower.getName(), borrower.getEmail()));
	}
}

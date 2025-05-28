package com.aeonbank.librarysystem.interfaces.rest.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aeonbank.librarysystem.application.service.BorrowerService;
import com.aeonbank.librarysystem.domain.model.Borrower;
import com.aeonbank.librarysystem.interfaces.rest.dto.GetBorrowerResponseDTO;
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

	@Operation(summary = "Get a list of all borrowers in the library", description = "Returns a list of all borrowers in the library")
	@GetMapping("/all")
	public ResponseEntity<List<GetBorrowerResponseDTO>> getAllBooks() {

		return ResponseEntity.ok(borrowerService.getAllBorrowers().stream()
				.map(borrower -> new GetBorrowerResponseDTO(borrower.getId(), borrower.getName(), borrower.getEmail()))
				.toList());
	}
}

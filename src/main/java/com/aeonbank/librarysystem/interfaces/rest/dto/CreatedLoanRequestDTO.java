package com.aeonbank.librarysystem.interfaces.rest.dto;

import jakarta.validation.constraints.NotNull;

public record CreatedLoanRequestDTO(
	@NotNull(message = "{bookId.notnull}") Long bookId,
	@NotNull(message = "{borrowerId.notnull}") Long borrowerId) {
}
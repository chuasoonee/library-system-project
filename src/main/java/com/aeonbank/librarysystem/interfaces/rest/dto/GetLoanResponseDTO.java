package com.aeonbank.librarysystem.interfaces.rest.dto;

import java.time.LocalDateTime;

public record GetLoanResponseDTO(
	Long loanId,
	Long bookId, String bookTitle, String bookIsbn,
	Long borrowerId, String borrowerName, String borrowerEmail,
	LocalDateTime loanDate, LocalDateTime returnedDate
) {}
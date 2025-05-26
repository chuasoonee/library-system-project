package com.aeonbank.librarysystem.interfaces.rest.dto;

public record RegisterBookResponseDTO(
	Long id,
	String isbn,
	String title,
	String author
) {}

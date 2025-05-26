package com.aeonbank.librarysystem.interfaces.rest.dto;

public record GetBookResponseDTO(
	Long id,
	String isbn,
	String title,
	String author,
	boolean isAvailable
) {}

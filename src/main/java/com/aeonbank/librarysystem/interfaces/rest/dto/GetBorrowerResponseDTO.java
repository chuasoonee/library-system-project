package com.aeonbank.librarysystem.interfaces.rest.dto;

public record GetBorrowerResponseDTO(
	Long id,
	String name,
	String email
) {}

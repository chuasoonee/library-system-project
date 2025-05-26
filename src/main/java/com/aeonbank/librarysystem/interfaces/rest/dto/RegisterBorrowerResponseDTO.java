package com.aeonbank.librarysystem.interfaces.rest.dto;

public record RegisterBorrowerResponseDTO(
	Long id,
	String name,
	String email
) {}

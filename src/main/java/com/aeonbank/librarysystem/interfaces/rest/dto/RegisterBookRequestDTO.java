package com.aeonbank.librarysystem.interfaces.rest.dto;

import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.ISBN.Type;

import jakarta.validation.constraints.NotBlank;

public record RegisterBookRequestDTO(
	@NotBlank(message = "{isbn.notblank}") @ISBN(type = Type.ANY, message = "{isbn.invalid}") String isbn,
	@NotBlank(message = "{title.notblank}")	String title,
	@NotBlank(message = "{author.notblank}") String author
) {}
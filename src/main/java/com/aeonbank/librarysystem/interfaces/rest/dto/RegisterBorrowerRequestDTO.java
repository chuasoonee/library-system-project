package com.aeonbank.librarysystem.interfaces.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterBorrowerRequestDTO(
	@NotBlank(message = "{name.notblank}") String name,
	@NotBlank(message = "{email.notblank}")	@Email(message = "{email.invalid}")	String email
) {}

//@Data
//public class RegisterBorrowerRequestDTO {
//
//	@NotBlank(message = "{name.notblank}")
//	private String name;
//	
//	@NotBlank(message = "{email.notblank}")
//	@Email(message = "{email.invalid}")
//	private String email;
//}

package com.aeonbank.librarysystem.interfaces.rest;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ApiError {

	@JsonIgnore
	@Value("{generic.error}")
	private String genericMessage;
	private HttpStatus status;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime timestamp;
	@Value("{generic.errorcode}")
	protected String errorCode;
	protected String message = genericMessage;

	public ApiError() {
		this.timestamp = LocalDateTime.now();
	}

	public ApiError(HttpStatus status, Throwable ex) {
		this();
		this.status = status;
		this.message = ex.getMessage();
	}

	public ApiError(HttpStatus status, String errorCode, Throwable ex) {
		this();
		this.status = status;
		this.errorCode = errorCode;
		this.message = ex.getMessage();
	}
}
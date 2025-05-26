package com.aeonbank.librarysystem.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.aeonbank.librarysystem.interfaces.rest.ApiError;
import com.aeonbank.librarysystem.interfaces.rest.ConstraintViolationApiError;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "trace")
@RestControllerAdvice(annotations = { RestController.class })
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<ApiError> handleRunTimeException(final Exception e) {
		var errorCode = "common.generic_error";
		logError(errorCode, e);
		return this.buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, errorCode, e));
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		var errorCode = "generic.method_argument_invalid_error";
		logError(errorCode, e);
		var apiError = new ConstraintViolationApiError(HttpStatus.BAD_REQUEST, errorCode, e);
		return new ResponseEntity<Object>(apiError, apiError.getStatus());
	}

	private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
	}

	private void logError(String errorCode, Exception ex) {
		log.error(errorCode + " : " + ex.toString());
	}
}

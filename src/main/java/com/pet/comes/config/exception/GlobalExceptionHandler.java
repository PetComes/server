package com.pet.comes.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pet.comes.response.DataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public DataResponse handleIllegalArgumentException(IllegalArgumentException exception) {
		log.error(exception.getMessage(), exception);
		return new DataResponse(Status.ILLEGAL_ARGUMENT, ResponseMessage.ILLEGAL_ARGUMENT, exception.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalStateException.class)
	public DataResponse handleIllegalStateException(IllegalStateException exception) {
		log.error(exception.getMessage(), exception);
		return new DataResponse(Status.ILLEGAL_STATE, ResponseMessage.ILLEGAL_STATE, exception.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public DataResponse handleUnexpectedException(Exception exception) {
		log.error(exception.getMessage(), exception);
		return new DataResponse(Status.ILLEGAL_STATE, ResponseMessage.ILLEGAL_STATE, exception.getMessage());
	}
}

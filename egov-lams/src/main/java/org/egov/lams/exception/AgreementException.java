package org.egov.lams.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AgreementException {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
		
		ErrorResponse errorResponse = new ErrorResponse();
		Error error=new Error();
		error.setCode(400);
		error.setMessage(ex.getMessage());
		errorResponse.setError(error);
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.OK);
	}
}

package org.egov.common;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.Error;
import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.ResponseInfo;
import org.egov.inv.model.ResponseInfo.StatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CustomControllerAdvice {

	private static final Logger LOG = LoggerFactory.getLogger(CustomControllerAdvice.class);

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public String handleMissingParamsError(Exception ex) {
		return ex.getMessage();
	}

 


	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidDataException.class)
	public ErrorRes  handleBindingErrors(InvalidDataException ex) {
		ErrorRes errRes = new ErrorRes();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(StatusEnum.FAILED);
		errRes.setResponseInfo(responseInfo);
		errRes.setErrors(ex.getValidationErrors());
		return errRes;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(org.apache.kafka.common.errors.TimeoutException.class)
	public  ErrorRes  handleThrowable(
			org.apache.kafka.common.errors.TimeoutException ex) {
		ErrorRes errRes = new ErrorRes();
		ex.printStackTrace();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(StatusEnum.FAILED);
		errRes.setResponseInfo(responseInfo);
		Error error = new  Error();

		error.setCode(ErrorCode.KAFKA_TIMEOUT_ERROR.getCode());
		error.setMessage(ErrorCode.KAFKA_TIMEOUT_ERROR.getMessage());
		error.setDescription(ErrorCode.KAFKA_TIMEOUT_ERROR.getDescription());
		List<Error> errors = new ArrayList<>();
		errors.add(error);
		errRes.setErrors(errors);
		return errRes;
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorRes handleServerError(Exception ex) {
		ex.printStackTrace();
		ErrorRes errRes = new ErrorRes();
		ex.printStackTrace();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(StatusEnum.FAILED);
		errRes.setResponseInfo(responseInfo);
		Error error = new  Error();
		error.setCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
		error.setMessage(ErrorCode.INTERNAL_SERVER_ERROR.getDescription());
		error.setDescription(ex.getMessage());
		List<Error> errors = new ArrayList<>();
		errors.add(error);
		errRes.setErrors(errors);
		return errRes;

	  
	 
	}
	
	 
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(InvalidDataAccessApiUsageException.class )
	public ErrorRes handleServerError(InvalidDataAccessApiUsageException ex) {
		ex.printStackTrace();
		ErrorRes errRes = new ErrorRes();
		ex.printStackTrace();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(StatusEnum.FAILED);
		errRes.setResponseInfo(responseInfo);
		Error error = new  Error();
		error.setCode(ErrorCode.SQL_ERROR.getCode());
		error.setMessage(ErrorCode.SQL_ERROR.getMessage());
		error.setDescription(ErrorCode.SQL_ERROR.getDescription());
		List<Error> errors = new ArrayList<>();
		errors.add(error);
		errRes.setErrors(errors);
		return errRes;

	  
	 
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(BadSqlGrammarException.class )
	public ErrorRes handleServerError(BadSqlGrammarException ex) {
		ex.printStackTrace();
		ErrorRes errRes = new ErrorRes();
		ex.printStackTrace();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(StatusEnum.FAILED);
		errRes.setResponseInfo(responseInfo);
		Error error = new  Error();
		error.setCode(ErrorCode.SQL_ERROR.getCode());
		error.setMessage(ErrorCode.SQL_ERROR.getMessage());
		error.setDescription(ErrorCode.SQL_ERROR.getDescription());
		List<Error> errors = new ArrayList<>();
		errors.add(error);
		errRes.setErrors(errors);
		return errRes;

	  
	 
	}

/*	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	public ErrorRes  handleThrowable(Exception ex) {
		ErrorRes errRes = new ErrorRes();
		ex.printStackTrace();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(StatusEnum.FAILED);
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();

		error.setCode(500);
		error.setMessage("Internal Server Error");
		error.setDescription(ex.getMessage());
		return errRes;
	}

	

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedAccessException.class)
	public ErrorResponse handleAuthenticationError(UnauthorizedAccessException ex) {
		ex.printStackTrace();
		ErrorResponse errRes = new ErrorResponse();

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.UNAUTHORIZED.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();

		error.setCode(404);
		error.setMessage("Un Authorized Access");
		error.setDescription(ex.getMessage());
		errRes.setError(error);
		return errRes;
	}
*/
}
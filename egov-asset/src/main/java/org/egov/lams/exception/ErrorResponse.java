package org.egov.lams.exception;

import org.egov.lams.model.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {

	 @JsonProperty("ResposneInfo")
	 private ResponseInfo resposneInfo;

	 @JsonProperty("Error")
	 private Error error;

	public ResponseInfo getResposneInfo() {
		return resposneInfo;
	}

	public void setResposneInfo(ResponseInfo resposneInfo) {
		this.resposneInfo = resposneInfo;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
	 
	 
	 
}

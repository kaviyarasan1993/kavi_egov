package org.egov.lams.exception;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {

	  @JsonProperty("code")
	  private Integer code = null;

	  @JsonProperty("message")
	  private String message = null;

	  @JsonProperty("description")
	  private String description = null;

	  @JsonProperty("filelds")
	  private List<Object> filelds = new ArrayList<Object>();

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Object> getFilelds() {
		return filelds;
	}

	public void setFilelds(List<Object> filelds) {
		this.filelds = filelds;
	}

	  
	
}

package org.egov.lams.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Test {
	 @JsonProperty("RequestInfo")
	  private RequestInfo requestInfo = null;
	 
	 private Map<Object,Object> map;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public Map<Object, Object> getMap() {
		return map;
	}

	public void setMap(Map<Object, Object> map) {
		this.map = map;
	}
	 
	 
}

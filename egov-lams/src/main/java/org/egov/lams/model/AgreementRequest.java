package org.egov.lams.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

public class AgreementRequest   {
  
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("Agreement")
  private Agreement agreement = null;

  public RequestInfo getRequestInfo() {
	  return requestInfo;
  }

  public void setRequestInfo(RequestInfo requestInfo) {
	this.requestInfo = requestInfo;
  }

  public Agreement getAgreement() {
	  return agreement;
  }
  public void setAgreement(Agreement agreement) {
	  this.agreement = agreement;
  }

}


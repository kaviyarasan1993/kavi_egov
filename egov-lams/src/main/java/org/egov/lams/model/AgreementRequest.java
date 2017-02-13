package org.egov.lams.model;


public class AgreementRequest   {
  
  private RequestInfo requestInfo = null;

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


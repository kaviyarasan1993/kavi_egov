package org.egov.lams.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AgreementResponse {
	
	@JsonProperty("ResposneInfo")
	private ResponseInfo resposneInfo;
	
	@JsonProperty("Agreements")
	private List<Agreement> agreement;
	
	public ResponseInfo getResposneInfo() {
		return resposneInfo;
	}
	public void setResposneInfo(ResponseInfo resposneInfo) {
		this.resposneInfo = resposneInfo;
	}
	public List<Agreement> getAgreement() {
		return agreement;
	}
	public void setAgreement(List<Agreement> agreement) {
		this.agreement = agreement;
	}
	
}

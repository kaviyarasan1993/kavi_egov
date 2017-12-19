package org.egov.lcms.notification.model;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseDetailsResponse {

	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("caseDetails")
	private List<CaseDetails> caseDetails;
}

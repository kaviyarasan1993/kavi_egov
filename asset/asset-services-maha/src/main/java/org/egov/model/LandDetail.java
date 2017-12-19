package org.egov.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandDetail {

	@NotNull
	private String code;
	
	@NotNull
	private Double area;
	
	@NotNull
	private String surveyNo;
}

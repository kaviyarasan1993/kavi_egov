package org.egov.lams.notification.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Objection {

	@JsonProperty("courtCaseNo")
	private String courtCaseNo;

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("courtCaseDate")
	private Date courtCaseDate;

	@JsonProperty("courtFixedRent")
	private Double courtFixedRent;

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("effectiveDate")
	private Date effectiveDate;
}
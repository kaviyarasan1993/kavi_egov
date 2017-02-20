package org.egov.lams.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Asset {

	private Long id;

	private Long category;

	private String name;

	private String code;

	private Long locality;

	private String street;

	private String zone;

	private Long ward;

	private String block;

	private Long electionward;
	
	private Long doorNo;
	
}

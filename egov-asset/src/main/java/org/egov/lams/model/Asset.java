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

	private Long street;

	private Long zone;

	private Long ward;

	private Long block;

	private Long electionward;
	
	private String doorNumber;
	
}

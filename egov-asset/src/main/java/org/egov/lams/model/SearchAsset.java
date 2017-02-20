package org.egov.lams.model;

import java.util.List;

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
public class SearchAsset {

	private List<Integer> id;

	private String assetCode;

	private String assetName;

	private Long assetCategory;

	private Long doorNo;
	
	private String ShoppingComplexNo;

	private String TenantId;

	private String zone;

	private String ward;

	private String block;

	private String locality;

	private String electionWard;
}

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

	private String doorNo;
	
	private String ShoppingComplexNo;

	private String TenantId;

	private Long zone;

	private Long ward;

	private Long block;

	private Long locality;

	private Long electionWard;
}

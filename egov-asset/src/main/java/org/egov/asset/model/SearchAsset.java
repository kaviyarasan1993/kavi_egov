package org.egov.asset.model;

import java.util.List;

import org.egov.asset.model.enums.StatusEnum;
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

	private List<Long> id;
	private String name;
	private String code;
	private Long assetCategory;
	private Long department;
	private StatusEnum status;

	private String tenantId;
	private Long pageSize;
	private Long pageNumber;
	private String sortBy;
	private String sortOrder;

}

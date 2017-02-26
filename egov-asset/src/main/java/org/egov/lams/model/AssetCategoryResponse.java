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
public class AssetCategoryResponse {
	
	private ResponseInfo responseInfo;
	private List <AssetCategory> assetCategory;

}

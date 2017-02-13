package org.egov.lams.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssetCategoryResponse {
	
	@JsonProperty("ResposneInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("asset_category")
	private List <AssetCategory> assetCategory;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public List<AssetCategory> getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(List<AssetCategory> assetCategory) {
		this.assetCategory = assetCategory;
	}

	@Override
	public String toString() {
		return "AssetCategoryResponse [responseInfo=" + responseInfo + ", assetCategory=" + assetCategory + "]";
	}

	
	

}

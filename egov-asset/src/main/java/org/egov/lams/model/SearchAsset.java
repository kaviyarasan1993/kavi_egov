package org.egov.lams.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchAsset {
	
	
	@JsonProperty("asset_code")
	private String assetCode;
	
	@JsonProperty("asset_name")
	private String assetName;
	
	@JsonProperty("asset_category")
	private String assetCategory;
	
	@JsonProperty("zone")
	private String zone;
	
	@JsonProperty("ward")
	private String ward;
	
	@JsonProperty("block")
	private String block;
	
	@JsonProperty("locality")
	private String locality;
	
	@JsonProperty("electionWard")
	private String electionWard;

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getElectionWard() {
		return electionWard;
	}

	public void setElectionWard(String electionWard) {
		this.electionWard = electionWard;
	}

	@Override
	public String toString() {
		return "SearchAsset [assetCode=" + assetCode + ", assetName=" + assetName + ", assetCategory=" + assetCategory
				+ ", zone=" + zone + ", ward=" + ward + ", block=" + block + ", locality=" + locality
				+ ", electionWard=" + electionWard + "]";
	}
	
	
	

}

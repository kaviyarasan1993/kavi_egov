package org.egov.lams.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RentIncrementType {

	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("asset_category")
	private String assetCategory;	
	
	@JsonProperty("fromdate")
	private Date fromDate;
	
	@JsonProperty("todate")
	private Date toDate;
	
	@JsonProperty("percentage")
	private Double percentage;
	
	@JsonProperty("flat_amount")
	private Double flatAmount;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public Double getFlatAmount() {
		return flatAmount;
	}

	public void setFlatAmount(Double d) {
		this.flatAmount = d;
	}

	@Override
	public String toString() {
		return "RentIncrementType [id=" + id + ", type=" + type + ", assetCategory=" + assetCategory + ", fromDate="
				+ fromDate + ", toDate=" + toDate + ", percentage=" + percentage + ", flatAmount=" + flatAmount + "]";
	}
	
	
	
	
}

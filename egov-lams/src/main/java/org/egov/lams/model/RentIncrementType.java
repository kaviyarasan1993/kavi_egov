package org.egov.lams.model;

import java.util.Date;

public class RentIncrementType {

	private Long id;

	private String type;

	private String assetCategory;

	private Date fromDate;

	private Date toDate;

	private Double percentage;

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

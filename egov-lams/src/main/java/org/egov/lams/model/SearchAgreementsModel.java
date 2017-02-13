package org.egov.lams.model;

import java.util.Date;

import org.egov.lams.model.enums.StatusEnum;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SearchAgreementsModel {

	
	// private RequestInfo requestInfo;
	
	
	 private String tenantId;
	 
	 
	 private Long agreementId;
	 
	
	 private String agreementNumber;
	 
	
	 private String tenderNumber;
	 
	 @JsonFormat(pattern="dd/MM/yyyy")
	 private Date fromDate;
	 
	 @JsonFormat(pattern="dd/MM/yyyy")
	 private Date toDate;
	 
	 private StatusEnum status;
	 
	 private String tinNumber;
	 
	 private String tradelicenseNumber;
	 
	 private Long assetCategory;
	 
	 private String shoppingComplexNo;
	 
	 private String assetCode;
	 
	 private Long locality;
	 
	 private Long revenueWard;
	 
	 private Long electionWard;
	 
	 private Long doorno;
	 
	 private String allotteeName;
	 
	 private Long mobilenumber;
	 
	 private String offSet;
	 
	 private String size;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Long getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(Long agreementId) {
		this.agreementId = agreementId;
	}

	public String getAgreementNumber() {
		return agreementNumber;
	}

	public void setAgreementNumber(String agreementNumber) {
		this.agreementNumber = agreementNumber;
	}

	public String getTenderNumber() {
		return tenderNumber;
	}

	public void setTenderNumber(String tenderNumber) {
		this.tenderNumber = tenderNumber;
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

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public String getTinNumber() {
		return tinNumber;
	}

	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}

	public String getTradelicenseNumber() {
		return tradelicenseNumber;
	}

	public void setTradelicenseNumber(String tradelicenseNumber) {
		this.tradelicenseNumber = tradelicenseNumber;
	}

	public Long getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(Long assetCategory) {
		this.assetCategory = assetCategory;
	}

	public String getShoppingComplexNo() {
		return shoppingComplexNo;
	}

	public void setShoppingComplexNo(String shoppingComplexNo) {
		this.shoppingComplexNo = shoppingComplexNo;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public Long getLocality() {
		return locality;
	}

	public void setLocality(Long locality) {
		this.locality = locality;
	}

	public Long getRevenueWard() {
		return revenueWard;
	}

	public void setRevenueWard(Long revenueWard) {
		this.revenueWard = revenueWard;
	}

	public Long getElectionWard() {
		return electionWard;
	}

	public void setElectionWard(Long electionWard) {
		this.electionWard = electionWard;
	}

	public Long getDoorno() {
		return doorno;
	}

	public void setDoorno(Long doorno) {
		this.doorno = doorno;
	}

	public String getAllotteeName() {
		return allotteeName;
	}

	public void setAllotteeName(String allotteeName) {
		this.allotteeName = allotteeName;
	}

	public Long getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(Long mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getOffSet() {
		return offSet;
	}

	public void setOffSet(String offSet) {
		this.offSet = offSet;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "FetchAgreementsModel [tenantId=" + tenantId + ", agreementId=" + agreementId + ", agreementNumber="
				+ agreementNumber + ", tenderNumber=" + tenderNumber + ", fromDate=" + fromDate + ", toDate=" + toDate
				+ ", status=" + status + ", tinNumber=" + tinNumber + ", tradelicenseNumber=" + tradelicenseNumber
				+ ", assetCategory=" + assetCategory + ", shoppingComplexNo=" + shoppingComplexNo + ", assetCode="
				+ assetCode + ", locality=" + locality + ", revenueWard=" + revenueWard + ", electionWard="
				+ electionWard + ", doorno=" + doorno + ", allotteeName=" + allotteeName + ", mobilenumber="
				+ mobilenumber + ", offSet=" + offSet + ", size=" + size + "]";
	}
	 
	 

	
	 
}

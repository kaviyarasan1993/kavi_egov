package org.egov.lams.model;

import java.util.Date;

import org.egov.lams.model.enums.NatureOfAllotmentEnum;
import org.egov.lams.model.enums.PaymentCycleEnum;
import org.egov.lams.model.enums.StatusEnum;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Agreement {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("agreement_number")
	private String agreementNumber;
	
	@JsonProperty("agreement_date")
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date agreementDate;
	
	@JsonProperty("allottee")
	private Allottee allottee;
	
	@JsonProperty("asset")
	private Asset asset;
	  
	@JsonProperty("tender_number")
	private String tenderNumber;
	
	@JsonProperty("tender_date")
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date tenderDate;
	
	@JsonProperty("council_number")
	private String councilNumber;
	
	@JsonProperty("council_date")
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date councilDate;
	
	@JsonProperty("bank_guarantee_amount")
	private Double bankGuaranteeAmount;
	
	@JsonProperty("bank_guarantee_date")
	private Date bankGuaranteeDate;
	
	@JsonProperty("security_deposit")
	private Double securityDeposit;
	
	@JsonProperty("security_deposit_date")
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date securityDepositDate;	
	
	@JsonProperty("status")
	private StatusEnum status;
	
	@JsonProperty("nature_of_allotment")
	private NatureOfAllotmentEnum natureOfAllotment;
	
	@JsonProperty("registration_free")
	private	Double registrationFree;
	
	@JsonProperty("case_no")
	private	String caseNo;
	
	@JsonProperty("commencement_date")
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date commencementDate;
	
	@JsonProperty("expiry_date")
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date expiryDate;
	
	@JsonProperty("order_details")
	private String orderDetails;
	
	@JsonProperty("rent")
	private Double rent;
	
	@JsonProperty("tradelicense_number")
	private String tradelicenseNumber;
	
	@JsonProperty("payment_cycle")
	private PaymentCycleEnum paymentCycle;
	
	@JsonProperty("rent_increment_method")
	private RentIncrementType rentIncrementMethod;
	
	@JsonProperty("order_no")
	private String orderNo;	
	
	@JsonProperty("order_date")
	@JsonFormat(pattern="dd/MM/yyyy")
	private	Date orderDate;	
	
	@JsonProperty("rr_reading_no")
	private	String rrReadingNo;
	
	@JsonProperty("remarks")
	private	String remarks;	
	
	@JsonProperty("solvency_certificate_no")
	private String solvencyCertificateNo;	
	
	@JsonProperty("solvency_certificate_date")
	@JsonFormat(pattern="dd/MM/yyyy")
	private	Date solvencyCertificateDate;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getAgreementNumber() {
		return agreementNumber;
	}

	public void setAgreementNumber(String agreementNumber) {
		this.agreementNumber = agreementNumber;
	}

	public Date getAgreementDate() {
		return agreementDate;
	}

	public void setAgreementDate(Date agreementDate) {
		this.agreementDate = agreementDate;
	}

	public Allottee getAllottee() {
		return allottee;
	}

	public void setAllottee(Allottee allottee) {
		this.allottee = allottee;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public String getTenderNumber() {
		return tenderNumber;
	}

	public void setTenderNumber(String tenderNumber) {
		this.tenderNumber = tenderNumber;
	}

	public Date getTenderDate() {
		return tenderDate;
	}

	public void setTenderDate(Date tenderDate) {
		this.tenderDate = tenderDate;
	}

	public String getCouncilNumber() {
		return councilNumber;
	}

	public void setCouncilNumber(String councilNumber) {
		this.councilNumber = councilNumber;
	}

	public Date getCouncilDate() {
		return councilDate;
	}

	public void setCouncilDate(Date councilDate) {
		this.councilDate = councilDate;
	}

	public Double getBankGuaranteeAmount() {
		return bankGuaranteeAmount;
	}

	public void setBankGuaranteeAmount(Double bankGuaranteeAmount) {
		this.bankGuaranteeAmount = bankGuaranteeAmount;
	}

	public Date getBankGuaranteeDate() {
		return bankGuaranteeDate;
	}

	public void setBankGuaranteeDate(Date bankGuaranteeDate) {
		this.bankGuaranteeDate = bankGuaranteeDate;
	}

	public Double getSecurityDeposit() {
		return securityDeposit;
	}

	public void setSecurityDeposit(Double securityDeposit) {
		this.securityDeposit = securityDeposit;
	}

	public Date getSecurityDepositDate() {
		return securityDepositDate;
	}

	public void setSecurityDepositDate(Date securityDepositDate) {
		this.securityDepositDate = securityDepositDate;
	}
	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	public NatureOfAllotmentEnum getNatureOfAllotment() {
		return natureOfAllotment;
	}

	public void setNatureOfAllotment(NatureOfAllotmentEnum natureOfAllotment) {
		this.natureOfAllotment = natureOfAllotment;
	}

	public Double getRegistrationFree() {
		return registrationFree;
	}

	public void setRegistrationFree(Double registrationFree) {
		this.registrationFree = registrationFree;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public Date getCommencementDate() {
		return commencementDate;
	}

	public void setCommencementDate(Date commencementDate) {
		this.commencementDate = commencementDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(String orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Double getRent() {
		return rent;
	}

	public void setRent(Double rent) {
		this.rent = rent;
	}

	public String getTradelicenseNumber() {
		return tradelicenseNumber;
	}

	public void setTradelicenseNumber(String tradelicenseNumber) {
		this.tradelicenseNumber = tradelicenseNumber;
	}

	public PaymentCycleEnum getPaymentCycle() {
		return paymentCycle;
	}

	public void setPaymentCycle(PaymentCycleEnum paymentCycle) {
		this.paymentCycle = paymentCycle;
	}

	public RentIncrementType getRentIncrementMethod() {
		return rentIncrementMethod;
	}

	public void setRentIncrementMethod(RentIncrementType rentIncrementMethod) {
		this.rentIncrementMethod = rentIncrementMethod;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getRrReadingNo() {
		return rrReadingNo;
	}

	public void setRrReadingNo(String rrReadingNo) {
		this.rrReadingNo = rrReadingNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSolvencyCertificateNo() {
		return solvencyCertificateNo;
	}

	public void setSolvencyCertificateNo(String solvencyCertificateNo) {
		this.solvencyCertificateNo = solvencyCertificateNo;
	}

	public Date getSolvencyCertificateDate() {
		return solvencyCertificateDate;
	}

	public void setSolvencyCertificateDate(Date solvencyCertificateDate) {
		this.solvencyCertificateDate = solvencyCertificateDate;
	}

	@Override
	public String toString() {
		return "Agreement [id=" + id + ", agreementNum=" + agreementNumber + ", agreementDate=" + agreementDate
				+ ", allottee=" + allottee + ", tenderNumber=" + tenderNumber + ", tenderDate=" + tenderDate
				+ ", councilNumber=" + councilNumber + ", councilDate=" + councilDate + ", bankGuaranteeAmount="
				+ bankGuaranteeAmount + ", bankGuaranteeDate=" + bankGuaranteeDate + ", securityDeposit="
				+ securityDeposit + ", securityDepositDate=" + securityDepositDate + ", status=" + status
				+ ", natureOfAllotment=" + natureOfAllotment + ", registrationFree=" + registrationFree + ", caseNo="
				+ caseNo + ", commencementDate=" + commencementDate + ", expiryDate=" + expiryDate + ", orderDetails="
				+ orderDetails + ", rent=" + rent + ", tradelicenseNumber=" + tradelicenseNumber + ", paymentCycle="
				+ paymentCycle + ", rentIncrementMethod=" + rentIncrementMethod + ", orderNo=" + orderNo
				+ ", orderDate=" + orderDate + ", rrReadingNo=" + rrReadingNo + ", remarks=" + remarks
				+ ", solvencyCertificateNo=" + solvencyCertificateNo + ", solvencyCertificateDate="
				+ solvencyCertificateDate + "]";
	}
	
		
}

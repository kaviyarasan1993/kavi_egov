package org.egov.lams.model;

import java.util.Date;

import org.egov.lams.model.enums.NatureOfAllotmentEnum;
import org.egov.lams.model.enums.PaymentCycleEnum;
import org.egov.lams.model.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Agreement {

	private Long id;

	private String agreementNumber;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date agreementDate;

	private Allottee allottee;

	private Asset asset;

	private String tenderNumber;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date tenderDate;

	private String councilNumber;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date councilDate;

	private Double bankGuaranteeAmount;

	private Date bankGuaranteeDate;

	private Double securityDeposit;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date securityDepositDate;

	private StatusEnum status;

	private NatureOfAllotmentEnum natureOfAllotment;

	private Double registrationFee;

	private String caseNo;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date commencementDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date expiryDate;

	private String orderDetails;

	private Double rent;

	private String tradelicenseNumber;

	private PaymentCycleEnum paymentCycle;

	private String tinNumber;

	public Double getRegistrationFee() {
		return registrationFee;
	}

	public void setRegistrationFee(Double registrationFee) {
		this.registrationFee = registrationFee;
	}

	public String getTinNumber() {
		return tinNumber;
	}

	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}

	@JsonProperty("order_no")
	private String orderNo;

	@JsonProperty("order_date")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date orderDate;

	@JsonProperty("rr_reading_no")
	private String rrReadingNo;

	@JsonProperty("remarks")
	private String remarks;

	@JsonProperty("solvency_certificate_no")
	private String solvencyCertificateNo;

	@JsonProperty("solvency_certificate_date")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date solvencyCertificateDate;

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
		return "Agreement [id=" + id + ", agreementNumber=" + agreementNumber + ", agreementDate=" + agreementDate
				+ ", allottee=" + allottee + ", asset=" + asset + ", tenderNumber=" + tenderNumber + ", tenderDate="
				+ tenderDate + ", councilNumber=" + councilNumber + ", councilDate=" + councilDate
				+ ", bankGuaranteeAmount=" + bankGuaranteeAmount + ", bankGuaranteeDate=" + bankGuaranteeDate
				+ ", securityDeposit=" + securityDeposit + ", securityDepositDate=" + securityDepositDate + ", status="
				+ status + ", natureOfAllotment=" + natureOfAllotment + ", registrationFee=" + registrationFee
				+ ", caseNo=" + caseNo + ", commencementDate=" + commencementDate + ", expiryDate=" + expiryDate
				+ ", orderDetails=" + orderDetails + ", rent=" + rent + ", tradelicenseNumber=" + tradelicenseNumber
				+ ", paymentCycle=" + paymentCycle + ", tinNumber=" + tinNumber + ", orderNo=" + orderNo
				+ ", orderDate=" + orderDate + ", rrReadingNo=" + rrReadingNo + ", remarks=" + remarks
				+ ", solvencyCertificateNo=" + solvencyCertificateNo + ", solvencyCertificateDate="
				+ solvencyCertificateDate + "]";
	}

}

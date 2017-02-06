package org.egov.lams.model;



import com.fasterxml.jackson.annotation.JsonProperty;

public class Allottee {
	

	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("address")
	private String address;
	
	@JsonProperty("contact_no")
	private Long contactNo;
	
	@JsonProperty("aadhaar_no")
	private String aadhaarNo;
	
	@JsonProperty("pan_no")
	private String panNo;
	
	@JsonProperty("emailid")
	private String emailId;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getContactNo() {
		return contactNo;
	}

	public void setContactNo(Long contactNo) {
		this.contactNo = contactNo;
	}

	public String getAadhaarNo() {
		return aadhaarNo;
	}

	public void setAadhaarNo(String aadhaarNo) {
		this.aadhaarNo = aadhaarNo;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Override
	public String toString() {
		return "Allottee [id=" + id + ", name=" + name + ", address=" + address + ", contactNo=" + contactNo
				+ ", aadhaarNo=" + aadhaarNo + ", panNo=" + panNo + ", emailId=" + emailId + "]";
	}
	
	
	

}

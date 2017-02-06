package org.egov.lams.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Asset {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("category")
	private String category;	
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("code")
	private String code;	
	
	@JsonProperty("locality")
	private String locality;
	
	@JsonProperty("street")
	private String street;	
	
	@JsonProperty("zone")
	private String zone;	
	
	@JsonProperty("ward")
	private String ward;	
	
	@JsonProperty("block")
	private String block;	
	
	@JsonProperty("electionward")
	private String electionward;

	

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
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
	public String getElectionward() {
		return electionward;
	}
	public void setElectionward(String electionward) {
		this.electionward = electionward;
	}	
	

}

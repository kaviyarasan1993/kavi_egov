package org.egov.lams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Asset {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("category")
	private Long category;

	@JsonProperty("name")
	private String name;

	@JsonProperty("code")
	private String code;

	@JsonProperty("locality")
	private Long locality;

	@JsonProperty("street")
	private String street;

	@JsonProperty("zone")
	private String zone;

	@JsonProperty("ward")
	private Long ward;

	@JsonProperty("block")
	private String block;

	@JsonProperty("electionward")
	private Long electionward;
	
	@JsonProperty("door_no")
	private Long doorNo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
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

	public Long getLocality() {
		return locality;
	}

	public void setLocality(Long locality) {
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

	public Long getWard() {
		return ward;
	}

	public void setWard(Long ward) {
		this.ward = ward;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public Long getElectionward() {
		return electionward;
	}

	public void setElectionward(Long electionward) {
		this.electionward = electionward;
	}

	public Long getDoorNo() {
		return doorNo;
	}

	public void setDoorNo(Long doorNo) {
		this.doorNo = doorNo;
	}

}

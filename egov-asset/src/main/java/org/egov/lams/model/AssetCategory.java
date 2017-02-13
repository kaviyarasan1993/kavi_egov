package org.egov.lams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssetCategory {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("code")
	private String code;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "AssetCategory [id=" + id + ", name=" + name + ", code=" + code + "]";
	}
	
	
	
	

}

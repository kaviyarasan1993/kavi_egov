package org.egov.lams.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AssetResponse {
	  
	@JsonProperty("ResposneInfo")
	private ResponseInfo resposneInfo = null;

	@JsonProperty("Assets")
	private List<Asset> assets;
}

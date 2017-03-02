package org.egov.lams.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertiesManager {
	
	 @Autowired
	 private Environment environment;
	 
	/* @Value("${egov.services.asset_service.hostname}")
	 private String assetServiceHostName;
	 
	 public String getAssetServiceHostName() {
		return assetServiceHostName;
	}
	 */
	 

}

package org.egov.lams.contract;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProcessInstanceRequest {
	
	@JsonProperty("requestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("processInstances")
	private List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();

	@JsonProperty("processInstance")
	private ProcessInstance processInstance = null;

}

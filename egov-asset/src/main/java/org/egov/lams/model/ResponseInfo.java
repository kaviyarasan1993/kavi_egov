package org.egov.lams.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseInfo {

	@JsonProperty("api_id")
	private String apiId;
	
	@JsonProperty("ver")
	private String ver;	
	
	@JsonProperty("ts")
	private Date ts;	
	
	@JsonProperty("action")
	private String action;	
	
	@JsonProperty("did")
	private String did;	
	
	@JsonProperty("key")
	private String key;	
	
	@JsonProperty("msg_id")
	private String msgId;
	
	@JsonProperty("requester_id")
	private String requesterId;
	
	
	public ResponseInfo() {
	}
	public ResponseInfo(String apiId, String ver, Date ts, String action, String did, String key, String msgId,
			String requesterId) {
		this.apiId = apiId;
		this.ver = ver;
		this.ts = ts;
		this.action = action;
		this.did = did;
		this.key = key;
		this.msgId = msgId;
		this.requesterId = requesterId;
	}
	
	

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(String requesterId) {
		this.requesterId = requesterId;
	}
	
	
}

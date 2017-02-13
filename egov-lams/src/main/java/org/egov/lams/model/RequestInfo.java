package org.egov.lams.model;

import java.util.Date;

public class RequestInfo {

	private String apiId;	
	
	private String ver;	
	
	private Date ts;	
	
	private String action;	
	
	private String did;	
	
	private String key;	
	
	private String msgId;	
	
	private String requesterId;	
	
	private String authToken;

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

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	@Override
	public String toString() {
		return "RequestInfo [apiId=" + apiId + ", ver=" + ver + ", ts=" + ts + ", action=" + action + ", did=" + did
				+ ", key=" + key + ", msgId=" + msgId + ", requesterId=" + requesterId + ", authToken=" + authToken
				+ "]";
	}
	
	
		
}

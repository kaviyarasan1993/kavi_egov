package org.egov.user.web.contract.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
//This class is serialized to Redis
public class User implements Serializable {
	private static final long serialVersionUID = -1053170163821651014L;
	private Long id;
	private String userName;
	private String name;
	private String mobileNumber;
	private String emailId;
	private String locale;
	private String type;
	private List<Role> roles;
	private boolean active;
	private String tenantId;
}
package org.egov.web.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.domain.model.OtpRequestType;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Getter
@AllArgsConstructor
public class Otp {
	private static final String USER_REGISTRATION = "register";
	private static final String PASSWORD_RESET = "passwordreset";
	private String mobileNumber;
    private String tenantId;
    private String type;

    @JsonIgnore
    public OtpRequestType getTypeOrDefault() {
    	return isEmpty(type) ? OtpRequestType.REGISTER : mapToDomainType();
	}

	private OtpRequestType mapToDomainType() {
		if(USER_REGISTRATION.equalsIgnoreCase(type)) {
			return OtpRequestType.REGISTER;
		} else if (PASSWORD_RESET.equalsIgnoreCase(type)) {
			return OtpRequestType.PASSWORD_RESET;
		}
		return null;
	}
}

package org.egov.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;
import org.egov.user.domain.exception.InvalidUserCreateException;
import org.egov.user.domain.exception.InvalidUserUpdateException;
import org.egov.user.domain.model.enums.BloodGroup;
import org.egov.user.domain.model.enums.Gender;
import org.egov.user.domain.model.enums.GuardianRelation;
import org.egov.user.domain.model.enums.UserType;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
	private Long id;
	private String tenantId;
	private String username;
	private String title;
	private String password;
	private String salutation;
	private String guardian;
	private GuardianRelation guardianRelation;
	private String name;
	private Gender gender;
	private String mobileNumber;
	private String emailId;
	private String altContactNumber;
	private String pan;
	private String aadhaarNumber;
	private Address permanentAddress;
	private Address correspondenceAddress;
	private Boolean active;
	private List<Role> roles = new ArrayList<>();
	private Date dob;
	private Date passwordExpiryDate;
	private String locale = "en_IN";
	private UserType type;
	private BloodGroup bloodGroup;
	private String identificationMark;
	private String signature;
	private String photo;
	private Boolean accountLocked;
	private Date lastModifiedDate;
	private Date createdDate;
	private String otpReference;
	private Long createdBy;
	private Long lastModifiedBy;
	private Long loggedInUserId;
	private boolean otpValidationMandatory;
	private boolean mobileValidationMandatory = true;

	public void validateNewUser() {
		if (isUsernameAbsent()
				|| isNameAbsent()
				|| isMobileNumberAbsent()
				|| isActiveIndicatorAbsent()
				|| isTypeAbsent()
				|| isPermanentAddressInvalid()
				|| isCorrespondenceAddressInvalid()
				|| isRolesAbsent()
				|| isOtpReferenceAbsent()
				|| isTenantIdAbsent()
				|| isPasswordAbsent()) {
			throw new InvalidUserCreateException(this);
		}
	}

	public void validateUserModification() {
		if (isPermanentAddressInvalid()
				|| isCorrespondenceAddressInvalid()
				|| isTenantIdAbsent()
				) {
			throw new InvalidUserUpdateException(this);
		}
	}

	public boolean isCorrespondenceAddressInvalid() {
		return correspondenceAddress != null && correspondenceAddress.isInvalid();
	}

	public boolean isPermanentAddressInvalid() {
		return permanentAddress != null && permanentAddress.isInvalid();
	}

	public boolean isOtpReferenceAbsent() {
		return otpValidationMandatory && isEmpty(otpReference);
	}

	public boolean isTypeAbsent() {
		return isEmpty(type);
	}

	public boolean isActiveIndicatorAbsent() {
		return isEmpty(active);
	}

	public boolean isMobileNumberAbsent() {
		return mobileValidationMandatory && isEmpty(mobileNumber);
	}

	public boolean isNameAbsent() {
		return isEmpty(name);
	}

	public boolean isUsernameAbsent() {
		return isEmpty(username);
	}

	public boolean isTenantIdAbsent() {
		return isEmpty(tenantId);
	}
	
	public boolean isPasswordAbsent(){
		return isEmpty(password);
	}

	public boolean isRolesAbsent() {
		return CollectionUtils.isEmpty(roles) || roles.stream().anyMatch(r -> isEmpty(r.getCode()));
	}

	public boolean isIdAbsent() {
		return id == null;
	}

	public void nullifySensitiveFields() {
		username = null;
		mobileNumber = null;
		password = null;
		passwordExpiryDate = null;
		roles = null;
	}

	public boolean isLoggedInUserDifferentFromUpdatedUser() {
		return !id.equals(loggedInUserId);
	}

	public void setRoleToCitizen() {
		type = UserType.CITIZEN;
		roles = Collections.singletonList(Role.getCitizenRole());
	}

	public void updatePassword(String newPassword) {
		password = newPassword;
	}

	public OtpValidationRequest getOtpValidationRequest() {
		return OtpValidationRequest.builder()
				.mobileNumber(mobileNumber)
				.tenantId(tenantId)
				.otpReference(otpReference)
				.build();
	}

	public List<Address> getAddresses() {
		final ArrayList<Address> addresses = new ArrayList<>();
		if (correspondenceAddress != null && correspondenceAddress.isNotEmpty()) {
			addresses.add(correspondenceAddress);
		}
		if (permanentAddress != null && permanentAddress.isNotEmpty()) {
			addresses.add(permanentAddress);
		}
		return addresses;
	}

	public void setDefaultPasswordExpiry(int expiryInDays) {
		if (passwordExpiryDate == null) {
			passwordExpiryDate = DateUtils.addDays(new Date(), expiryInDays);
		}
	}

	public void setActive(boolean isActive) {
		active = isActive;
	}
}


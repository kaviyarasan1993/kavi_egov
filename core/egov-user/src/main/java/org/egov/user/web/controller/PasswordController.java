package org.egov.user.web.controller;

import org.egov.user.domain.service.UserService;
import org.egov.user.web.contract.LoggedInUserUpdatePasswordRequest;
import org.egov.user.web.contract.NonLoggedInUserUpdatePasswordRequest;
import org.egov.user.web.contract.UpdatePasswordResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("password")
public class PasswordController {

	private UserService userService;

	public PasswordController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/_update")
	public UpdatePasswordResponse updatePassword(@RequestBody LoggedInUserUpdatePasswordRequest request) {
		userService.updatePasswordForLoggedInUser(request.toDomain());
		return new UpdatePasswordResponse(null);
	}

	@PostMapping("/nologin/_update")
	public UpdatePasswordResponse updatePasswordForNonLoggedInUser(
			@RequestBody NonLoggedInUserUpdatePasswordRequest request) {
		userService.updatePasswordForNonLoggedInUser(request.toDomain());
		return new UpdatePasswordResponse(null);
	}
}

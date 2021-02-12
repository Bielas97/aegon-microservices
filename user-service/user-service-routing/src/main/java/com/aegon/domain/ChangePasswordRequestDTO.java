package com.aegon.domain;

import com.aegon.requests.ChangePasswordRequest;

public class ChangePasswordRequestDTO {

	public String currentPassword;

	public String newPassword;

	public String newPasswordConfirmation;

	public ChangePasswordRequest toDomain() {
		return ChangePasswordRequest.builder()
				.currentPassword(ApplicationUserPassword.valueOf(currentPassword))
				.newPassword(ApplicationUserPassword.valueOf(newPassword))
				.passwordConfirmation(ApplicationUserPassword.valueOf(newPasswordConfirmation))
				.build();
	}
}

package com.aegon.requests;

import com.aegon.domain.ApplicationUserPassword;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChangePasswordRequest {

	private final ApplicationUserPassword currentPassword;

	private final ApplicationUserPassword newPassword;

	private final ApplicationUserPassword passwordConfirmation;

}

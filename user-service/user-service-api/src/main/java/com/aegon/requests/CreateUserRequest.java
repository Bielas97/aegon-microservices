package com.aegon.requests;

import com.aegon.domain.ApplicationUser;
import com.aegon.domain.ApplicationUserPassword;
import com.aegon.domain.ApplicationUserRole;
import com.aegon.domain.ApplicationUsername;
import com.aegon.util.lang.Email;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserRequest {

	private final ApplicationUsername username;

	private final Email email;

	private final Set<ApplicationUserRole> roles;

	private final ApplicationUserPassword password;

	private final ApplicationUserPassword passwordConfirmation;

	public ApplicationUser toUser() {
		return ApplicationUser.builder()
				.username(username)
				.password(password)
				.email(email)
				.roles(roles)
				.build();
	}

}

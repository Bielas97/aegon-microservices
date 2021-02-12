package com.aegon.domain;

import com.aegon.requests.CreateUserRequest;
import com.aegon.util.lang.Email;
import java.util.Set;

public class CreateUserRequestDTO {

	public String username;

	public String email;

	public Set<ApplicationUserRole> roles;

	public String password;

	public String passwordConfirmation;

	public CreateUserRequest toDomain() {
		return CreateUserRequest.builder()
				.username(ApplicationUsername.valueOf(username))
				.password(ApplicationUserPassword.valueOf(password))
				.passwordConfirmation(ApplicationUserPassword.valueOf(passwordConfirmation))
				.email(Email.valueOf(email))
				.roles(roles)
				.build();
	}

}

package com.aegon.domain;

import com.aegon.util.lang.DomainObject;
import com.aegon.util.lang.Email;
import com.aegon.util.lang.Preconditions;
import java.util.Set;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class ApplicationUser implements DomainObject<ApplicationUserId> {

	private final ApplicationUserId id;

	private final ApplicationUsername username;

	private final Email email;

	private final Set<ApplicationUserRole> roles;

	private final ApplicationUserPassword password;

	public ApplicationUser(ApplicationUserId id,
			ApplicationUsername username,
			Email email,
			Set<ApplicationUserRole> roles,
			ApplicationUserPassword password) {
		this.id = id;
		this.username = Preconditions.requireNonNull(username);
		this.email = Preconditions.requireNonNull(email);
		this.roles = Preconditions.requireNonEmpty(roles);
		this.password = Preconditions.requireNonNull(password);
	}

	public ApplicationUser copyWith(ApplicationUserPassword changedPassword) {
		return new ApplicationUser(
				id,
				username,
				email,
				roles,
				changedPassword
		);
	}
}

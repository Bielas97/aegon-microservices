package com.aegon.domain;

import java.util.Set;
import lombok.Builder;

@Builder
public class ApplicationUserDTO {

	public String id;

	public String username;

	public String email;

	public Set<ApplicationUserRole> roles;

	public static ApplicationUserDTO from(ApplicationUser domainUser) {
		return ApplicationUserDTO.builder()
				.id(domainUser.getId().getInternal())
				.email(domainUser.getEmail().getInternal())
				.roles(domainUser.getRoles())
				.username(domainUser.getUsername().getInternal())
				.build();
	}

}

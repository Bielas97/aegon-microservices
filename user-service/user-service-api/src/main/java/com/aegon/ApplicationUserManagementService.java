package com.aegon;

import com.aegon.domain.ApplicationUser;
import com.aegon.domain.ApplicationUserId;
import com.aegon.requests.ChangePasswordRequest;
import com.aegon.requests.CreateUserRequest;
import reactor.core.publisher.Mono;

public interface ApplicationUserManagementService {

	Mono<ApplicationUser> register(CreateUserRequest request);

	Mono<ApplicationUser> changePassword(ApplicationUserId userId, ChangePasswordRequest request);

}

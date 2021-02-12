package com.aegon.application;

import com.aegon.ApplicationPasswordValidator;
import com.aegon.ApplicationUserManagementService;
import com.aegon.ApplicationUserRepository;
import com.aegon.domain.ApplicationUser;
import com.aegon.domain.ApplicationUserId;
import com.aegon.requests.ChangePasswordRequest;
import com.aegon.requests.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ApplicationUserManagementServiceImpl implements ApplicationUserManagementService {

	private final ApplicationUserRepository repository;

	private final PasswordEncoder passwordEncoder;

	@Override
	public Mono<ApplicationUser> register(CreateUserRequest request) {
		var validator = ApplicationPasswordValidator.with(request.getPassword(), request.getPasswordConfirmation());
		validator.validate();
		return repository.save(request.toUser());
	}

	@Override
	public Mono<ApplicationUser> changePassword(ApplicationUserId userId, ChangePasswordRequest request) {
		final var userMono = repository.findById(userId);
		return userMono.flatMap(user -> {
			validateChangePasswordRequest(user, request);
			var updatedUser = user.copyWith(request.getNewPassword());
			return repository.update(updatedUser);
		});
	}

	private void validateChangePasswordRequest(ApplicationUser user, ChangePasswordRequest request) {
		final String encodedPassword = user.getPassword().getInternal();
		final String requestNonEncodedPassword = request.getCurrentPassword().getInternal();
		if (!passwordEncoder.matches(requestNonEncodedPassword, encodedPassword)) {
			throw ChangePasswordException.of("Current password is incorrect");
		}
		var validator = ApplicationPasswordValidator.with(request.getNewPassword(), request.getPasswordConfirmation());
		validator.validate();
	}
}

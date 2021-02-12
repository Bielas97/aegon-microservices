package com.aegon;

import com.aegon.domain.ApplicationUserDTO;
import com.aegon.domain.ApplicationUserId;
import com.aegon.domain.ApplicationUsername;
import com.aegon.domain.ChangePasswordRequestDTO;
import com.aegon.domain.CreateUserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import static com.aegon.util.spring.webflux.ResponseResolver.toMonoServerResponse;

@Component
@RequiredArgsConstructor
public class UserRoutingHandler {

	private final ApplicationUserManagementService userManagement;

	private final ApplicationUserRepository userRepository;

	public Mono<ServerResponse> register(ServerRequest serverRequest) {
		final var createUserDtoMono = serverRequest.bodyToMono(CreateUserRequestDTO.class);
		return toMonoServerResponse(createUserDtoMono.flatMap(requestDTO -> {
					final var domainUserMono = userManagement.register(requestDTO.toDomain());
					return domainUserMono.map(ApplicationUserDTO::from);
				}
		));
	}

	public Mono<ServerResponse> changePassword(ServerRequest serverRequest) {
		final var id = serverRequest.pathVariable("id");
		final var requestMono = serverRequest.bodyToMono(ChangePasswordRequestDTO.class);
		return toMonoServerResponse(requestMono.flatMap(request -> {
			final var domainUserMono = userManagement.changePassword(ApplicationUserId.valueOf(id), request.toDomain());
			return domainUserMono.map(ApplicationUserDTO::from);
		}));
	}

	public Mono<ServerResponse> findByUsername(ServerRequest serverRequest) {
		final var username = serverRequest.pathVariable("username");
		final var domainUserMono = userRepository.findByUsername(ApplicationUsername.valueOf(username));
		final var userDTOMono = domainUserMono.map(ApplicationUserDTO::from);
		return toMonoServerResponse(userDTOMono);
	}

	public Mono<ServerResponse> findById(ServerRequest serverRequest) {
		final var id = serverRequest.pathVariable("id");
		final var domainUserMono = userRepository.findById(ApplicationUserId.valueOf(id));
		final var userDTOMono = domainUserMono.map(ApplicationUserDTO::from);
		return toMonoServerResponse(userDTOMono);
	}

}

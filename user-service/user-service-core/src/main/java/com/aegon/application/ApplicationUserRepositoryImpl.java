package com.aegon.application;

import com.aegon.ApplicationUserServiceException;
import com.aegon.domain.ApplicationUser;
import com.aegon.domain.ApplicationUserId;
import com.aegon.ApplicationUserRepository;
import com.aegon.domain.ApplicationUsername;
import com.aegon.domain.MongoRoleDocument;
import com.aegon.domain.MongoUserDocument;
import com.aegon.infrastructure.MongoRoleRepository;
import com.aegon.infrastructure.MongoUserRepository;
import com.aegon.infrastructure.ReactiveApplicationUserMapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ApplicationUserRepositoryImpl implements ApplicationUserRepository {

	private final MongoUserRepository mongoUserRepository;

	private final MongoRoleRepository mongoRoleRepository;

	private final ReactiveApplicationUserMapper userMapper;

	private final PasswordEncoder passwordEncoder;

	@Override
	public Mono<ApplicationUser> findByUsername(ApplicationUsername username) {
		final var userDocumentMono = mongoUserRepository.findByUsername(username.getInternal());
		return userMapper.map(userDocumentMono)
				.switchIfEmpty(Mono.error(ApplicationUserServiceException.of("User not found")));
	}

	@Override
	public Mono<ApplicationUser> findById(ApplicationUserId id) {
		final var userDocumentMono = mongoUserRepository.findById(id.getInternal());
		return userMapper.map(userDocumentMono)
				.switchIfEmpty(Mono.error(ApplicationUserServiceException.of("User not found")));
	}

	@Override
	public Mono<ApplicationUser> save(ApplicationUser user) {
		return Mono.just(user).flatMap(this::saveApiUserWithRoles);
	}

	@Override
	public Mono<ApplicationUserId> delete(ApplicationUserId id) {
		return mongoUserRepository.deleteById(id.getInternal())
				.flatMap(unused -> Mono.just(id));
	}

	@Override
	public Mono<ApplicationUser> update(ApplicationUser domainUser) {
		return Mono.just(domainUser).flatMap(this::updateDomainUser);
	}

	private Mono<ApplicationUser> saveApiUserWithRoles(ApplicationUser domainUser) {
		return saveRoles(domainUser)
				.collectList()
				.flatMap(roles -> saveDomainUserWithRolesAttached(domainUser, roles));
	}

	private Flux<MongoRoleDocument> saveRoles(ApplicationUser domainUser) {
		final Set<MongoRoleDocument> mongoRoles = domainUser.getRoles()
				.stream()
				.map(MongoRoleDocument::from)
				.collect(Collectors.toSet());
		return Flux.fromIterable(mongoRoles)
				.flatMap(mongoRole ->
						mongoRoleRepository.findByName(mongoRole.getName())
								.switchIfEmpty(Mono.defer(() -> mongoRoleRepository.save(mongoRole)))
				);
	}

	private Mono<ApplicationUser> saveDomainUserWithRolesAttached(ApplicationUser domainUser, List<MongoRoleDocument> roles) {
		final var encodedPassword = passwordEncoder.encode(domainUser.getPassword().getInternal());
		final var roleIds = roles.stream().map(MongoRoleDocument::getId).collect(Collectors.toSet());
		final var mongoUser = MongoUserDocument.builder()
				.username(domainUser.getUsername().getInternal())
				.email(domainUser.getEmail().getInternal())
				.password(encodedPassword)
				.roleIds(roleIds)
				.build();
		final var saved = mongoUserRepository.save(mongoUser);
		return userMapper.map(saved);
	}

	private Mono<ApplicationUser> updateDomainUserWithRolesAttached(ApplicationUser domainUser, List<MongoRoleDocument> roles) {
		final var encodedPassword = passwordEncoder.encode(domainUser.getPassword().getInternal());
		final var roleIds = roles.stream().map(MongoRoleDocument::getId).collect(Collectors.toSet());
		final var mongoUser = MongoUserDocument.builder()
				.id(domainUser.getId().getInternal())
				.username(domainUser.getUsername().getInternal())
				.email(domainUser.getEmail().getInternal())
				.password(encodedPassword)
				.roleIds(roleIds)
				.build();
		final var saved = mongoUserRepository.save(mongoUser);
		return userMapper.map(saved);
	}

	private Mono<ApplicationUser> updateDomainUser(ApplicationUser domainUser) {
		return saveRoles(domainUser)
				.collectList()
				.flatMap(roleDocuments -> updateDomainUserWithRolesAttached(domainUser, roleDocuments));
	}
}

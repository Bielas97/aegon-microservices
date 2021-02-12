package com.aegon.infrastructure;

import com.aegon.domain.ApplicationUser;
import com.aegon.domain.ApplicationUserId;
import com.aegon.domain.ApplicationUserPassword;
import com.aegon.domain.ApplicationUsername;
import com.aegon.domain.MongoRoleDocument;
import com.aegon.domain.MongoUserDocument;
import com.aegon.util.lang.Email;
import com.aegon.util.lang.Mapper;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReactiveApplicationUserMapper implements Mapper<Mono<MongoUserDocument>, Mono<ApplicationUser>> {

	private final MongoRoleRepository roleRepository;

	@Override
	public Mono<ApplicationUser> map(Mono<MongoUserDocument> userDocument) {
		return userDocument.flatMap(user -> Flux.fromIterable(user.getRoleIds())
				.map(roleRepository::findById)
				.flatMap(roleDocumentMono -> roleDocumentMono.map(MongoRoleDocument::getName))
				.collectList()
				.map(roles -> ApplicationUser.builder()
						.id(ApplicationUserId.valueOf(user.getId()))
						.username(ApplicationUsername.valueOf(user.getUsername()))
						.password(ApplicationUserPassword.valueOf(user.getPassword()))
						.email(Email.valueOf(user.getEmail()))
						.roles(new HashSet<>(roles))
						.build())
		);
	}
}

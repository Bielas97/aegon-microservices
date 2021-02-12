package com.aegon.infrastructure;

import com.aegon.domain.ApplicationUserRole;
import com.aegon.domain.MongoRoleDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MongoRoleRepository extends ReactiveMongoRepository<MongoRoleDocument, String> {

	Mono<MongoRoleDocument> findByName(ApplicationUserRole role);

}

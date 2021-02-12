package com.aegon.infrastructure;

import com.aegon.domain.MongoUserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MongoUserRepository extends ReactiveMongoRepository<MongoUserDocument, String> {

	Mono<MongoUserDocument> findByUsername(String username);

}

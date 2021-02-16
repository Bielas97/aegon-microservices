package com.aegon.infrastructure;

import com.aegon.domain.MongoSectorDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MongoSectorRepository extends ReactiveMongoRepository<MongoSectorDocument, String> {

	Mono<MongoSectorDocument> findByName(String name);

}

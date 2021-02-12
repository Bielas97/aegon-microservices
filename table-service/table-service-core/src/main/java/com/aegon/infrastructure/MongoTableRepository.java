package com.aegon.infrastructure;

import com.aegon.domain.MongoKvTableDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MongoTableRepository extends ReactiveMongoRepository<MongoKvTableDocument, String> {

	Mono<MongoKvTableDocument> findByName(String name);

	@Query(value = "{ 'customerIds': { $in: [?0] } }")
	Mono<MongoKvTableDocument> findByCustomerId(String customerId);

}

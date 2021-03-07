package com.aegon.infrastructure;

import com.aegon.domain.MongoCustomerDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoCustomerRepository extends ReactiveMongoRepository<MongoCustomerDocument, String> {

}

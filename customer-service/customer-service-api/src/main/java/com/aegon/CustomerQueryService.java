package com.aegon;

import com.aegon.domain.Customer;
import com.aegon.domain.CustomerId;
import reactor.core.publisher.Mono;

public interface CustomerQueryService {

	Mono<Customer> query(CustomerId customerId);

}

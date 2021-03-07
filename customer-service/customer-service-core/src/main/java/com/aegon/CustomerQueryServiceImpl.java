package com.aegon;

import com.aegon.domain.Customer;
import com.aegon.domain.CustomerId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerQueryServiceImpl implements CustomerQueryService {

	@Override
	public Mono<Customer> query(CustomerId customerId) {
		return null;
	}
}

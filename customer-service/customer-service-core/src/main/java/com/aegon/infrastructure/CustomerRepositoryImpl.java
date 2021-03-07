package com.aegon.infrastructure;

import com.aegon.CustomerRepository;
import com.aegon.domain.Customer;
import com.aegon.domain.CustomerId;
import com.aegon.domain.MongoCustomerDocument;
import com.aegon.domain.MongoCustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

	private final MongoCustomerRepository mongoCustomerRepository;

	private final MongoCustomerMapper mongoCustomerMapper = MongoCustomerMapper.getInstance();

	@Override
	public Mono<Customer> findById(CustomerId id) {
		return mongoCustomerRepository.findById(id.getInternal())
				.map(mongoCustomerMapper::map)
				.switchIfEmpty(Mono.error(CustomerRepositoryException.of(String.format("Customer with id %s not found", id.getInternal()))));
	}

	@Override
	public Mono<Customer> save(Customer customer) {
		return mongoCustomerRepository.save(MongoCustomerDocument.from(customer))
				.map(mongoCustomerMapper::map)
				.switchIfEmpty(Mono.error(CustomerRepositoryException.of(String.format("Customer with name %s could not be saved", customer.getName()))));
	}

	@Override
	public Mono<CustomerId> delete(CustomerId id) {
		return mongoCustomerRepository.findById(id.getInternal())
				.flatMap(mongoCustomer -> mongoCustomerRepository.delete(mongoCustomer).then(Mono.just(CustomerId.valueOf(mongoCustomer.getId()))));
	}

	@Override
	public Mono<Customer> update(Customer customer) {
		return mongoCustomerRepository.findById(customer.getId().getInternal())
				.flatMap(mongoCustomer -> {
					final var updated = mongoCustomer.update(customer);
					return mongoCustomerRepository.save(updated).map(mongoCustomerMapper::map);
				})
				.switchIfEmpty(Mono.error(CustomerRepositoryException.of(String.format("Customer with id %s could not be updated", customer.getName()))));
	}
}

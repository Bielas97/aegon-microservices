package com.aegon;

import com.aegon.domain.Customer;
import com.aegon.domain.CustomerId;
import com.aegon.domain.CustomerName;
import com.aegon.util.lang.Address;
import com.aegon.util.lang.Email;
import com.aegon.util.lang.EmailMessage;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import static com.aegon.util.spring.webflux.ResponseResolver.toMonoServerResponse;

@Component
@RequiredArgsConstructor
public class CustomerRoutingHandler {

	private final CustomerRepository customerRepository;

	private final CustomerQueryService customerQueryService;

	public Mono<ServerResponse> test(ServerRequest request) {
		return toMonoServerResponse(Mono.just("hello world"));
	}

	Mono<ServerResponse> getCustomerById(ServerRequest request) {
//		final CustomerId customerId = CustomerId.valueOf(request.pathVariable("customerId"));
//		final Mono<Customer> customerMono = customerQueryService.query(customerId);
//		final Mono<CustomerDTO> customerDTOMono = customerMono.flatMap(CustomerDTO::from);
//		return toMonoServerResponse(customerDTOMono);
//		return null;
		final Address address = Address.builder()
				.withCountry("Poland")
				.withApartmentNumber(2)
				.withCity("Warsaw")
				.withPostalCode("0000")
				.withStreet("llorens")
				.withStreetNumber(22)
				.build();
		final CustomerName name = CustomerName.builder()
				.firstName("Kuba")
				.middleName("Karol")
				.lastName("Bielawski")
				.build();
		final Customer customer = Customer.builder()
				.name(name)
				.birthDate(LocalDate.now())
				.email(Email.valueOf("jakub.bielawski@wawasoft.com"))
				.address(address)
				.build();

//		final EmailMessage msg = EmailMessage.builder()
//				.topic("topic")
//				.msg("message")
//				.build();
//		customer.sendEmail(msg);

		final Mono<Customer> save = customerRepository.save(customer);

		return toMonoServerResponse(save);
	}

	public Mono<ServerResponse> addCustomer(ServerRequest request) {
		return null;
	}

	public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
		return null;
	}

	public <T extends ServerResponse> Mono<T> getCustomersFromTable(ServerRequest request) {
		return null;
	}

	public <T extends ServerResponse> Mono<T> updateCustomer(ServerRequest request) {
		return null;
	}


}

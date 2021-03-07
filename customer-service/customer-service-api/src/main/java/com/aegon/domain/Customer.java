package com.aegon.domain;

import com.aegon.CustomerUpdateRequest;
import com.aegon.util.lang.Address;
import com.aegon.util.lang.DomainObject;
import com.aegon.util.lang.Email;
import com.aegon.util.lang.EmailMessage;
import com.aegon.util.lang.Preconditions;
import com.aegon.util.lang.eventsourcing.EventBus;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Builder
public class Customer implements DomainObject<CustomerId> {

	private final CustomerId id;

	@Getter
	private final CustomerName name;

	@Getter
	private final Email email;

	@Getter
	private final Address address;

	@Getter
	private final LocalDate birthDate;

	private Customer(CustomerId id, CustomerName name, Email email, Address address, LocalDate birthDate) {
		this.id = id;
		this.name = Preconditions.requireNonNull(name);
		this.email = Preconditions.requireNonNull(email);
		this.address = Preconditions.requireNonNull(address);
		this.birthDate = Preconditions.requireNonNull(birthDate);
	}

	public Customer update(CustomerUpdateRequest request) {
		final CustomerName updatedName = name.equals(request.name()) ? name : request.name();
		final Email updatedEmail = email.equals(request.email()) ? email : request.email();
		final Address updatedAddress = address.equals(request.address()) ? address : request.address();
		final LocalDate updatedBirthDate = birthDate.equals(request.birthDate()) ? birthDate : request.birthDate();
		return Customer.builder()
				.id(id)
				.name(updatedName)
				.email(updatedEmail)
				.address(updatedAddress)
				.birthDate(updatedBirthDate)
				.build();
	}

	public void sendEmail(EmailMessage message) {
		final var event = new EmailMessageEvent(email, message);
		EventBus.post(event);
	}

	@Override
	public CustomerId getId() {
		return id;
	}
}

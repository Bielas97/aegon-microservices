package com.aegon.domain;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MongoCustomerDocument {

	@Id
	private String id;

	private CustomerNameEmbeddedDocument name;

	private String email;

	private AddressEmbeddedDocument address;

	private LocalDate dateOfBirth;

	public static MongoCustomerDocument from(Customer customer) {
		final AddressEmbeddedDocument address = AddressEmbeddedDocument.from(customer.getAddress());
		return MongoCustomerDocument.builder()
				.name(CustomerNameEmbeddedDocument.from(customer.getName()))
				.email(customer.getEmail().getInternal())
				.address(address)
				.dateOfBirth(customer.getBirthDate())
				.build();
	}

	public MongoCustomerDocument update(Customer customer) {
		final AddressEmbeddedDocument updatedAddress = address.update(customer.getAddress());
		return MongoCustomerDocument.builder()
				.id(id)
				.name(name.update(customer.getName()))
				.email(customer.getEmail().getInternal())
				.address(updatedAddress)
				.dateOfBirth(customer.getBirthDate())
				.build();
	}

}

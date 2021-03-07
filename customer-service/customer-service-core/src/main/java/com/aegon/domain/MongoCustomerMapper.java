package com.aegon.domain;

import com.aegon.util.lang.Address;
import com.aegon.util.lang.Email;
import com.aegon.util.lang.Mapper;

public class MongoCustomerMapper implements Mapper<MongoCustomerDocument, Customer> {

	private static final MongoCustomerMapper INSTANCE = new MongoCustomerMapper();

	private MongoCustomerMapper() {

	}

	public static MongoCustomerMapper getInstance() {
		return INSTANCE;
	}

	@Override
	public Customer map(MongoCustomerDocument mongoCustomerDocument) {
		final CustomerName customerName = CustomerName.builder()
				.firstName(mongoCustomerDocument.getName().getFirstName())
				.middleName(mongoCustomerDocument.getName().getMiddleName())
				.lastName(mongoCustomerDocument.getName().getLastName())
				.build();
		final Address address = Address.builder()
				.withCountry(mongoCustomerDocument.getAddress().getCountry())
				.withCity(mongoCustomerDocument.getAddress().getCity())
				.withStreet(mongoCustomerDocument.getAddress().getStreet().getStreet())
				.withStreetNumber(mongoCustomerDocument.getAddress().getStreet().getNumber())
				.withApartmentNumber(mongoCustomerDocument.getAddress().getApartmentNumber())
				.withPostalCode(mongoCustomerDocument.getAddress().getPostalCode())
				.build();
		return Customer.builder()
				.id(CustomerId.valueOf(mongoCustomerDocument.getId()))
				.name(customerName)
				.email(Email.valueOf(mongoCustomerDocument.getEmail()))
				.address(address)
				.birthDate(mongoCustomerDocument.getDateOfBirth())
				.build();
	}
}

package com.aegon.domain;

import com.aegon.util.lang.Preconditions;
import com.aegon.util.lang.ValueObject;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerName extends ValueObject {

	private final String firstName;

	private final String middleName;

	private final String lastName;

	private CustomerName(String firstName, String middleName, String lastName) {
		this.firstName = Preconditions.requireNonNull(firstName);
		this.middleName = middleName;
		this.lastName = Preconditions.requireNonNull(lastName);
	}

	public CustomerName changeFirstName(String firstName) {
		return CustomerName.builder()
				.firstName(firstName)
				.middleName(middleName)
				.lastName(lastName)
				.build();
	}

	public CustomerName changeMiddleName(String middleName) {
		return CustomerName.builder()
				.firstName(firstName)
				.middleName(middleName)
				.lastName(lastName)
				.build();
	}

	public CustomerName changeLastName(String lastName) {
		return CustomerName.builder()
				.firstName(firstName)
				.middleName(middleName)
				.lastName(lastName)
				.build();
	}

	@Override
	public String toString() {
		return firstName + " " + middleName + " " + lastName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CustomerName that = (CustomerName) o;
		return Objects.equals(firstName, that.firstName) && Objects.equals(middleName, that.middleName) && Objects.equals(lastName, that.lastName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, middleName, lastName);
	}
}

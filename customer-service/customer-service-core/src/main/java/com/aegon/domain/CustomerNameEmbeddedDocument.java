package com.aegon.domain;

import com.aegon.util.lang.ValueObject;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
class CustomerNameEmbeddedDocument extends ValueObject {

	private final String firstName;

	private final String middleName;

	private final String lastName;

	public static CustomerNameEmbeddedDocument from(CustomerName name) {
		return CustomerNameEmbeddedDocument.builder()
				.firstName(name.getFirstName())
				.middleName(name.getMiddleName())
				.lastName(name.getLastName())
				.build();
	}

	public CustomerNameEmbeddedDocument update(CustomerName name) {
		return CustomerNameEmbeddedDocument.builder()
				.firstName(name.getFirstName())
				.middleName(name.getMiddleName())
				.lastName(name.getLastName())
				.build();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CustomerNameEmbeddedDocument that = (CustomerNameEmbeddedDocument) o;
		return Objects.equals(firstName, that.firstName) && Objects.equals(middleName, that.middleName) && Objects.equals(lastName, that.lastName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, middleName, lastName);
	}
}

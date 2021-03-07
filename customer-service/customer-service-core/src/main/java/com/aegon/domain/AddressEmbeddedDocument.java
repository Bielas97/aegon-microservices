package com.aegon.domain;

import com.aegon.util.lang.Address;
import com.aegon.util.lang.ValueObject;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
class AddressEmbeddedDocument extends ValueObject {

	private final String country;

	private final String city;

	private final StreetEmbeddedDocument street;

	private final Integer apartmentNumber;

	private final String postalCode;

	public static AddressEmbeddedDocument from(Address address) {
		return AddressEmbeddedDocument.builder()
				.country(address.getCountry())
				.city(address.getCity())
				.street(StreetEmbeddedDocument.builder()
						.street(address.getStreet())
						.number(address.getStreetNumber())
						.build())
				.apartmentNumber(address.apartmentNumber().orElse(null))
				.postalCode(address.postalCode().orElse(null))
				.build();
	}

	public AddressEmbeddedDocument update(Address address) {
		return AddressEmbeddedDocument.builder()
				.country(address.getCountry())
				.city(address.getCity())
				.street(StreetEmbeddedDocument.builder()
						.street(address.getStreet())
						.number(address.getStreetNumber())
						.build())
				.apartmentNumber(address.apartmentNumber().orElse(null))
				.postalCode(address.postalCode().orElse(null))
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
		AddressEmbeddedDocument that = (AddressEmbeddedDocument) o;
		return Objects.equals(country, that.country) && Objects.equals(city, that.city) && Objects.equals(street, that.street) && Objects.equals(apartmentNumber, that.apartmentNumber) && Objects.equals(postalCode, that.postalCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(country, city, street, apartmentNumber, postalCode);
	}
}

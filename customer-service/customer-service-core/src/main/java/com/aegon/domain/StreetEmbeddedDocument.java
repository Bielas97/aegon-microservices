package com.aegon.domain;

import com.aegon.util.lang.ValueObject;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
class StreetEmbeddedDocument extends ValueObject {

	private final String street;

	private final Integer number;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		StreetEmbeddedDocument that = (StreetEmbeddedDocument) o;
		return Objects.equals(street, that.street) && Objects.equals(number, that.number);
	}

	@Override
	public int hashCode() {
		return Objects.hash(street, number);
	}
}

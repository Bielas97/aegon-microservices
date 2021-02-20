package com.aegon.domain;

import com.aegon.requests.AddNewTableRequest;
import java.util.StringJoiner;

public class AddNewTableRequestDTO {

	public String name;

	public Integer maxPlaces;

	public String sectorName;

	public AddNewTableRequest toDomain() {
		final StringJoiner fullTableName = new StringJoiner("@", sectorName, name);
		return AddNewTableRequest.builder()
				.name(TableName.from(fullTableName.toString()))
				.maxPlaces(maxPlaces)
				.build();
	}

}

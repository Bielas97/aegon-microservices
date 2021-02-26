package com.aegon.domain;

import com.aegon.requests.AddNewTableRequest;
import java.util.StringJoiner;

public class AddNewTableRequestDTO {

	public String name;

	public Integer maxPlaces;

	public String sectorName;

	public AddNewTableRequest toDomain() {
		final StringJoiner fullTableNameJoiner = new StringJoiner("@");
		fullTableNameJoiner.add(sectorName).add(name);
		return AddNewTableRequest.builder()
				.name(TableName.from(fullTableNameJoiner.toString()))
				.maxPlaces(maxPlaces)
				.build();
	}

}

package com.aegon.domain;

import com.aegon.requests.AddNewTableRequest;

public class AddNewTableRequestDTO {

	public String name;

	public Integer maxPlaces;

	public String sectorName;

	public AddNewTableRequest toDomain() {
		return AddNewTableRequest.builder()
				.name(TableName.valueOf(name))
				.maxPlaces(maxPlaces)
				.sectorName(SectorName.valueOf(sectorName))
				.build();
	}

}

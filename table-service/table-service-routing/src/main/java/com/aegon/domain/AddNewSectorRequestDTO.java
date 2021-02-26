package com.aegon.domain;

import com.aegon.requests.AddNewSectorRequest;

public class AddNewSectorRequestDTO {

	public String name;

	public Integer maxTables;

	public AddNewSectorRequest toDomain() {
		return new AddNewSectorRequest(SectorName.valueOf(name), maxTables);
	}

}

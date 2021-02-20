package com.aegon.requests;

import com.aegon.domain.SectorName;
import com.aegon.domain.TableName;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddNewTableRequest {

	private final Integer maxPlaces;

	private final TableName name;

	public AddNewTableRequest copyWithSector(SectorName sectorName) {
		final TableName of = TableName.of(sectorName, name.getTableNumber());
		return AddNewTableRequest.builder()
				.maxPlaces(maxPlaces)
				.name(of)
				.build();
	}

}

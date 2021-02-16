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

	private final SectorName sectorName;

}

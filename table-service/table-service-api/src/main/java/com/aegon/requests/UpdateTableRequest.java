package com.aegon.requests;

import com.aegon.domain.SectorId;
import com.aegon.domain.TableName;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateTableRequest {

	private final TableName tableName;

	private final Integer maxPlaces;

	private final SectorId sectorId;

}

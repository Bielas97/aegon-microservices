package com.aegon.domain;

import lombok.Builder;

@Builder
public class SectorDTO {

	public String id;

	public String name;

	public Integer maxTables;

	public Integer assignedTablesNumber;

	public static SectorDTO from(Sector sector) {
		return SectorDTO.builder()
				.id(sector.getId().getInternal())
				.name(sector.getName().getInternal())
				.maxTables(sector.getMaxTables())
				.assignedTablesNumber(sector.getTablesAmount())
				.build();
	}

}

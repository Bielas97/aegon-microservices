package com.aegon.domain;

import com.aegon.proxy.CustomerId;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public class TableDTO {

	public String id;

	public String name;

	public Integer maxPlaces;

	public String sectorId;

	public Set<String> customers;

	public static TableDTO from(Table table) {
		return TableDTO.builder()
				.id(table.getId().getInternal())
				.name(table.getName().getStringValue())
				.sectorId(table.getSectorId().getInternal())
				.maxPlaces(table.getMaxPlaces())
				.customers(table.getCustomers().stream().map(CustomerId::getInternal).collect(Collectors.toSet()))
				.build();
	}
}

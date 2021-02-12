package com.aegon.domain;

import com.aegon.proxy.CustomerId;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public class TableDTO {

	public String tableId;

	public String name;

	public Integer maxPlaces;

	public Set<String> customers;

	public static TableDTO from(Table table) {
		return TableDTO.builder()
				.tableId(table.getId().getInternal())
				.name(table.getName().getInternal())
				.maxPlaces(table.getMaxPlaces())
				.customers(table.getCustomers().stream().map(CustomerId::getInternal).collect(Collectors.toSet()))
				.build();
	}
}

package com.aegon.infrastructure;

import com.aegon.domain.MongoKvTableDocument;
import com.aegon.domain.SectorId;
import com.aegon.domain.Table;
import com.aegon.domain.TableId;
import com.aegon.domain.TableName;
import com.aegon.proxy.CustomerId;
import com.aegon.util.lang.Mapper;
import java.util.stream.Collectors;

public class TableMapper implements Mapper<MongoKvTableDocument, Table> {

	private static final TableMapper INSTANCE = new TableMapper();

	private TableMapper() {

	}

	public static TableMapper getInstance() {
		return INSTANCE;
	}

	@Override
	public Table map(MongoKvTableDocument document) {
		return Table.builder()
				.id(TableId.valueOf(document.getId()))
				.name(TableName.from(document.getName()))
				.maxPlaces(document.getMaxPlaces())
				.sectorId(SectorId.valueOf(document.getSectorId()))
				.customers(document.getCustomerIds().stream().map(CustomerId::valueOf).collect(Collectors.toSet()))
				.build();
	}
}

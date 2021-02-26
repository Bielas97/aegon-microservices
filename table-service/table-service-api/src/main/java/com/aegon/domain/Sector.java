package com.aegon.domain;

import com.aegon.util.lang.DomainObject;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
public class Sector implements DomainObject<SectorId> {

	private final SectorId id;

	@Getter
	private final SectorName name;

	@Getter
	private final Integer maxTables;

	private final Set<Table> tables;

	public void addTable(Table table) {
		this.tables.add(table);
	}

	public void addTables(Collection<Table> tables) {
		this.tables.addAll(tables);
	}

	public void removeTable(TableId tableId) {
		this.tables.removeIf(table -> table.getId().equals(tableId));
	}

	public Set<TableId> getTableIds() {
		return tables.stream().map(Table::getId).collect(Collectors.toSet());
	}

	public Integer getTablesAmount() {
		return tables.size();
	}

	private Integer numberOfFreePlaces() {
		return maxTables - tables.size();
	}

	public boolean isAnyPlaceLeft() {
		if (numberOfFreePlaces() < 0) {
			throw SectorFreePlacesException.err();
		}
		return numberOfFreePlaces() > 0;
	}

	@Override
	public SectorId getId() {
		return id;
	}
}

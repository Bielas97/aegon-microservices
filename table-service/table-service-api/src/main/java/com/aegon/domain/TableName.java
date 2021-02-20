package com.aegon.domain;

import com.aegon.util.lang.Preconditions;
import java.util.StringJoiner;
import lombok.Getter;

@Getter
public class TableName {

	private final SectorName sectorName;

	private final Integer tableNumber;

	private TableName(SectorName sector, Integer number) {
		this.sectorName = Preconditions.requireNonNull(sector);
		this.tableNumber = Preconditions.requireNonNull(number);
	}

	public static TableName of(SectorName sector, Integer tableNumber) {
		return new TableName(sector, tableNumber);
	}

	public static TableName from(String fullTableName) {
		final String[] sectorAndTableNumber = fullTableName.split("@");
		return of(SectorName.valueOf(sectorAndTableNumber[0]), Integer.valueOf(sectorAndTableNumber[1]));
	}

	public String getStringValue() {
		final var joiner = new StringJoiner("@");
		return joiner
				.add(sectorName.getInternal())
				.add(tableNumber.toString())
				.toString();
	}
}

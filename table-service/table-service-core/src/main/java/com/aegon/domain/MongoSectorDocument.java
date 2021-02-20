package com.aegon.domain;

import com.aegon.util.lang.SimpleId;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "table_sectors")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MongoSectorDocument {

	@Id
	private String id;

	@Indexed(unique = true)
	private String name;

	private Integer maxTables;

	private Set<String> tableIds;

	public void update(Sector sector) {
		this.name = sector.getName().getInternal();
		this.maxTables = sector.getMaxTables();
		updateTableIds(sector.getTableIds().stream().map(SimpleId::getInternal).collect(Collectors.toSet()));
	}

	private void updateTableIds(Set<String> tableIds) {
		this.tableIds = tableIds;
	}
}

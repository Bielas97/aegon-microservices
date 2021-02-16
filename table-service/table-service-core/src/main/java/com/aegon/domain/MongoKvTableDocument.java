package com.aegon.domain;

import com.aegon.proxy.CustomerId;
import com.aegon.util.lang.SimpleId;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "kv_tables")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MongoKvTableDocument {

	@Id
	private String id;

	@Indexed(unique = true)
	private String name;

	private Integer maxPlaces;

	private String sectorId;

	private Set<String> customerIds;

	public Set<String> getCustomerIds() {
		return Set.copyOf(customerIds);
	}

	public void update(Table table) {
		this.name = table.getName().getInternal();
		this.maxPlaces = table.getMaxPlaces();
		this.sectorId = table.getSectorId().getInternal();
		this.customerIds = table.getCustomers().stream().map(SimpleId::getInternal).collect(Collectors.toSet());
	}
}

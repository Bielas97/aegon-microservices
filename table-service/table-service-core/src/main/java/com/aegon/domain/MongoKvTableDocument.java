package com.aegon.domain;

import com.aegon.util.lang.SimpleId;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
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

	@NotNull
	private Integer maxPlaces;

	@NotNull
	private String sectorId;

	@NotNull
	private Set<String> customerIds = new HashSet<>();

	public Set<String> getCustomerIds() {
		return Set.copyOf(customerIds);
	}

	public void update(Table table) {
		this.name = table.getName().getStringValue();
		this.maxPlaces = table.getMaxPlaces();
		this.sectorId = table.getSectorId().getInternal();
		this.customerIds = table.getCustomers().stream().map(SimpleId::getInternal).collect(Collectors.toSet());
	}
}

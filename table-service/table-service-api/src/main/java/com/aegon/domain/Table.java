package com.aegon.domain;

import com.aegon.proxy.CustomerId;
import com.aegon.requests.UpdateTableRequest;
import com.aegon.util.lang.DomainObject;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class Table implements DomainObject<TableId> {

	private final TableId id;

	private final TableName name;

	private final Integer maxPlaces;

	private final SectorId sectorId;

	private final Set<CustomerId> customers;

	public Set<CustomerId> getCustomers() {
		return Set.copyOf(customers);
	}

	public void addCustomer(CustomerId id) {
		this.customers.add(id);
	}

	public void deleteCustomer(CustomerId id) {
		this.customers.remove(id);
	}

	public Table update(UpdateTableRequest request) {
		return Table.builder()
				.id(id)
				.name(request.getTableName())
				.maxPlaces(request.getMaxPlaces())
				.sectorId(request.getSectorId())
				.customers(Set.copyOf(customers))
				.build();
	}
}
